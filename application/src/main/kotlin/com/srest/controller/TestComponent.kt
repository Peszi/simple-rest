package com.srest.controller

import com.srest.framework.request.HttpMethod
import com.srest.framework.util.Controller
import com.srest.framework.util.RequestMapping

@Controller
internal class TestComponent {

    @RequestMapping(HttpMethod.POST, "/web", true)
    fun getWeb(): String {
        return "web"
    }
}