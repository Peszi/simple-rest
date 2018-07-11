package com.srest.framework.util

import com.srest.framework.response.ContentType
import com.srest.framework.response.Response
import java.io.File
import java.lang.reflect.Method
import kotlin.reflect.KClass

internal object ClassInjector {

    @JvmStatic
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
                        if (objectClass.annotations.isNotEmpty()) annotatedClasses.add(objectClass)
                    } catch (e: ClassNotFoundException) {
                        println("Class rejected!")
                    }
                }
            }
        }
        scanForAnnotatedFiles(path, packageName)
        return annotatedClasses
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

    @JvmStatic
    fun invokeMethodResult(bean: Any, method: Method): Response {
        val result = method.invoke(bean) // TODO args
        if (result is String) return Response(ContentType.HTML_TYPE, result)
        // map object to Json
        return Response(ContentType.JSON_TYPE, "{ \"error\": \"json not supported\" }")
    }

}