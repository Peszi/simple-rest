package com.srest.framework.annotation

import com.srest.framework.annotation.web.ChildComponent
import com.srest.framework.util.Controller

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Controller
annotation class WebComponent