package com.srest.framework.inject

import com.fasterxml.jackson.module.kotlin.isKotlinClass
import com.srest.framework.annotation.Component
import com.srest.framework.annotation.RestController
import com.srest.framework.annotation.web.WebController
import com.srest.framework.util.Controller
import com.srest.framework.util.Logger
import java.io.File
import kotlin.reflect.KClass
import java.util.zip.ZipEntry
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.InputStream
import java.util.zip.ZipInputStream
import java.util.ArrayList



internal object ClassLoader {

    private val INTERNAL_ANNOTATIONS = listOf("Metadata", "Target", "Retention")
    private val REQUIRED_ANNOTATIONS = listOf(Component::class, Controller::class, RestController::class, WebController::class)

    @JvmStatic
    fun scanForClasses(baseClass: KClass<out Any>, vararg additionalBeans: KClass<out Any>): List<AnnotatedClass> {
        val annotatedClasses = mutableListOf<AnnotatedClass>()
        // load additional beans
        additionalBeans.forEach {
            val objectClass = it.java
            val annotations = ClassLoader.getAnnotations(objectClass, REQUIRED_ANNOTATIONS)
            if (annotations.isNotEmpty()) annotatedClasses.add(AnnotatedClass(objectClass, annotations))
        }
        // load package beans
        fun addAnnotatedClass(annotatedClass: Class<*>) {
            if (!annotatedClass.isAnnotation && annotatedClass.annotations.isNotEmpty()) {
                val annotations = ClassLoader.getAnnotations(annotatedClass, REQUIRED_ANNOTATIONS)
                if (annotations.isNotEmpty()) {
                    Logger.log.info("CLASS ${annotatedClass.canonicalName}")
                    annotatedClasses.add(AnnotatedClass(annotatedClass, annotations))
                }
            }
        }
        Logger.log.info("Trying to load classes from Jar..")
        loadClassesFromJar(baseClass) { addAnnotatedClass(this) }
        Logger.log.info("Trying to load classes from resources..")
        loadClassesFromResources(baseClass) { addAnnotatedClass(this) }
        return annotatedClasses
    }

    private fun loadClassesFromResources(baseClass: KClass<out Any>, block: Class<*>.() -> Unit) {
        val packageName = baseClass.qualifiedName!!.substringBeforeLast(".")
        val path = baseClass.java.classLoader.getResource(packageName.replace(".", "/")).path
        fun scanForAnnotatedFiles(currentPath: String, classPackage: String) {
            File(currentPath).listFiles().forEach {
                if (it.isDirectory) { scanForAnnotatedFiles(it.path, classPackage + "." + it.name)
                } else {
                    try {
                        Class.forName("$classPackage.${it.name.substringBeforeLast(".")}").block()
                    } catch (e: ClassNotFoundException) {
                        println("Cannot load class $classPackage")
                    }
                }
            }
        }
        try {
            scanForAnnotatedFiles(path, packageName)
        } catch (e: IllegalStateException) {}
    }

    private fun loadClassesFromJar(baseClass: KClass<out Any>, block: Class<*>.() -> Unit) {
        val packageName = baseClass.qualifiedName!!.substringBeforeLast(".")
        val packagePath = packageName.replace(".","/")
        try {
            val zip = ZipInputStream(this::class.java.protectionDomain.codeSource.location.openStream())
            while (true) {
                val entry = zip.nextEntry ?: break
                if (entry.isDirectory || !entry.name.startsWith(packagePath)) continue
                val className = entry.name.replace('/', '.').substringBefore(".class")
                try {
                    baseClass.java.classLoader.loadClass(className).block()
                } catch (e: ClassNotFoundException) {
                    println("Cannot load class ${entry.name}")
                }
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }

    @JvmStatic
    private fun getAnnotations(targetClass: Class<*>, targetAnnotations: List<KClass<out Annotation>>): List<KClass<out Annotation>> {
        val classAnnotations = mutableListOf<KClass<out Annotation>>()
        fun checkAnnotations(annotations: List<Annotation>) {
            annotations.filterNot {
                it.annotationClass.simpleName in INTERNAL_ANNOTATIONS
            }.forEach {
                val annotation = it.annotationClass
                targetAnnotations.filter { annotation == it }.forEach { classAnnotations.add(annotation) }
                return checkAnnotations(annotation.annotations)
            }
        }
        checkAnnotations(targetClass.annotations.toList())
        return classAnnotations
    }

    @JvmStatic
    fun isAnnotated(targetClass: Class<*>, annotationClass: KClass<out Annotation>): Boolean {
        fun checkAnnotations(annotations: List<Annotation>): Boolean {
            for (annotationEntry in annotations) {
                val annotation = annotationEntry.annotationClass
                if (annotation.simpleName == "Metadata" || annotation.simpleName == "Target" ||
                        annotation.simpleName == "Retention") continue
                if (annotation.simpleName == annotationClass.simpleName) return true
                return checkAnnotations(annotation.annotations)
            }
            return false
        }
        return checkAnnotations(targetClass.annotations.toList())
    }

}