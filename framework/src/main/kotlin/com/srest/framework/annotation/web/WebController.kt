package com.srest.framework.annotation.web

import com.srest.framework.util.Controller

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Controller
annotation class WebController(
        val url: String,
        val dir: String,
        val childComponents: Array<ChildComponent> = arrayOf()
)