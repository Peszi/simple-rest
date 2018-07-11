package com.srest.framework.annotation.web

import javax.annotation.Resource
import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Repeatable
@Retention(AnnotationRetention.SOURCE)
annotation class ChildComponent(
        val mapping: String,
        val component: KClass<*>
)