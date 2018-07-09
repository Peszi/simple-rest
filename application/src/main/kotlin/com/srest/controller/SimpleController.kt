package com.srest.controller

import com.srest.framework.request.HttpMethod
import com.srest.framework.util.Controller
import com.srest.framework.util.RequestMapping

@Controller
internal class SimpleController {

    @RequestMapping(HttpMethod.GET, "/user")
    fun getHello(): String {
        return "User"
    }

    @RequestMapping(HttpMethod.GET)
    fun getHello2(): String {
        return "Index"
    }

    @RequestMapping(HttpMethod.GET, "/bieda")
    fun getSpring(): String {
        return "<html><h1>spring!</h1></html>"
    }
}