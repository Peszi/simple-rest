package com.srest.controller

import com.srest.framework.request.HttpMethod
import com.srest.framework.request.Request
import com.srest.framework.util.Controller
import com.srest.framework.util.RequestMapping

@Controller
internal class TestComponent {

    @RequestMapping(HttpMethod.GET, "/test")
    fun getWeb(): String {
        return "web arg="
    }
}