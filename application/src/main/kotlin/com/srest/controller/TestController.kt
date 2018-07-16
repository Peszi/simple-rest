package com.srest.controller

import com.srest.TestComponent
import com.srest.framework.request.HttpMethod
import com.srest.framework.util.Controller
import com.srest.framework.util.RequestMapping

@Controller
internal class TestController(
        private val testComponent: TestComponent
) {

    @RequestMapping(HttpMethod.GET, "/test")
    fun getWeb(): String {
        testComponent.printTest()
        return "web arg="
    }
}