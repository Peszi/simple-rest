package com.srest.framework

import com.srest.framework.util.Controller
import kotlin.reflect.KClass
import kotlin.reflect.jvm.internal.impl.load.kotlin.KotlinClassFinder
import sun.font.LayoutPathImpl.getPath
import java.io.File
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType


class RestFramework{

    companion object {
        inline fun <reified T : kotlin.Any> runFramework() {
            getAnnotatedClasses(T::class)

//            println(Package.getPackage(baseClass.qualifiedName).implementationTitle)
//            val desc = Executors::class.annotations.first() as Description
//            RestFramework()
        }

        inline fun <reified T : kotlin.Any> getAnnotatedClasses(baseClass: KClass<T>) {
            val controllerClasses = mapOf<String, Class<*>>()
            val packageName = baseClass.qualifiedName!!.substringBeforeLast(".")
            println(packageName)
            val url = T::class.java.classLoader.getResource(packageName.replace(".", "/"))
            val dir = File(url.path).listFiles()
            dir.forEach {
                print("> " + it.name)
                try {
                    val repoClass = Class.forName(packageName + "." + it.name.substringBeforeLast("."))
                    if (repoClass.isAnnotationPresent(Controller::class.java)) { controllerClasses.put("", repoClass) }
                } catch (e: ClassNotFoundException) {
                    println("class rejected")
                }
                println()
            }


        }
    }
}