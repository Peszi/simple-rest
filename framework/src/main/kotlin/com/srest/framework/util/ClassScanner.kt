package com.srest.framework.util

import java.io.File
import kotlin.reflect.KClass

internal object ClassScanner {

    fun <T : kotlin.Any> scanForClasses(baseClass: KClass<T>): List<Class<*>> {
        val annotatedClasses = mutableListOf<Class<*>>()
        val packageName = baseClass.qualifiedName!!.substringBeforeLast(".")
        val path = baseClass.java.classLoader.getResource(packageName.replace(".", "/")).path
        fun scanForAnnotatedFiles(currentPath: String, classPackage: String) {
            File(currentPath).listFiles().forEach {
                if (it.isDirectory) { scanForAnnotatedFiles(it.path, classPackage + "." + it.name)
                } else {
                    try {
                        val objectClass = Class.forName("$classPackage.${it.name.substringBeforeLast(".")}")
                        if (objectClass.isAnnotation) annotatedClasses.add(objectClass)
                    } catch (e: ClassNotFoundException) {
                        println("Class rejected!")
                    }
                }
            }
        }
        scanForAnnotatedFiles(path, packageName)
        return annotatedClasses
    }

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