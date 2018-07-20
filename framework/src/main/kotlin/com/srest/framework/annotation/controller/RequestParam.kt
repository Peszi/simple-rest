package com.srest.framework.annotation.util

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class RequestParam(
        val required: Boolean = true,
        val name: String = ""
)