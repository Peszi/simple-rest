package com.srest.framework.annotation.web

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class WebPage(
        val dir: String,
        val mapping: String,
        val controller: WebController = WebController("", "")
)