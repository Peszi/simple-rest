package com.srest.framework.annotation

import com.srest.framework.request.HttpMethod

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class WebPage(
        val mapping: String = "/"
)