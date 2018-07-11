package com.srest.framework.annotation.web

import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class ComponentRequest(
        val component: KClass<*>
)