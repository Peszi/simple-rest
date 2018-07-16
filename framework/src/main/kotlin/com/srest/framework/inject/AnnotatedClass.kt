package com.srest.framework.inject

import kotlin.reflect.KClass

internal class AnnotatedClass(
        val classObject: Class<*>,
        val annotations: List<KClass<out Annotation>>
)