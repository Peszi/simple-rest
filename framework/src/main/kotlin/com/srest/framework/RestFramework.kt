package com.srest.framework

import kotlin.reflect.KClass
import kotlin.reflect.jvm.internal.impl.load.kotlin.KotlinClassFinder

class RestFramework{

    companion object {
        inline fun <reified T : kotlin.Any> runFramework() {
            val baseClass = T::class
            println(baseClass.qualifiedName)
            println(Package.getPackage(baseClass.qualifiedName).implementationTitle)
//            val desc = Executors::class.annotations.first() as Description
//            RestFramework()
        }
    }
}