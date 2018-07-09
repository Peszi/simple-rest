package com.srest.framework.main

import com.srest.framework.util.Controller
import java.io.File
import kotlin.reflect.KClass

object AnnotationScanner {

    @JvmStatic
    fun <T : kotlin.Any> getAnnotatedClasses(baseClass: KClass<T>): Set<Class<*>> {
        val annotatedClasses = mutableSetOf<Class<*>>()
        val packageName = baseClass.qualifiedName!!.substringBeforeLast(".")
        val path = baseClass.java.classLoader.getResource(packageName.replace(".", "/")).path
        fun scanForAnnotatedFiles(currentPath: String, currentPackage: String) {
            File(currentPath).listFiles().forEach {
                if (it.isDirectory) { scanForAnnotatedFiles(it.path, currentPackage + "." + it.name)
                } else {
                    try {
                        val repoClass = Class.forName(currentPackage + "." + it.name.substringBeforeLast("."))
                        if (repoClass.isAnnotationPresent(Controller::class.java)) annotatedClasses.add(repoClass)
                    } catch (e: ClassNotFoundException) {
                        println("Class rejected!")
                    }
                }
            }
        }
        scanForAnnotatedFiles(path, packageName)
        return annotatedClasses
    }
}