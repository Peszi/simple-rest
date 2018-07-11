package com.srest.framework.annotation

import com.srest.framework.util.Controller

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Controller
annotation class WebComponent(
        val htmlFile: String,
        val jsFile: String = ""
)