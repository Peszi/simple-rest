package com.srest.framework.util

internal object MethodManager {

    fun getMethodByName(targetClass: Class<*>, methodName: String) =
            targetClass.declaredMethods.firstOrNull { it.name == methodName }
}