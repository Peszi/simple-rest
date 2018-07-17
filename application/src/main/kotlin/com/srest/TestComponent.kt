package com.srest

import com.srest.framework.annotation.Autowire
import com.srest.framework.request.HttpMethod
import com.srest.framework.util.Controller
import com.srest.framework.util.RequestMapping

@Controller
internal class TestControllerA(
        var controller: TestControllerB
) {

    fun getName() = "\"${this::class.simpleName}\""

    @RequestMapping(HttpMethod.GET, "/a")
    fun getTest() = "= ${controller.getName()}"
}

@Controller
internal class TestControllerB{

    @Autowire
    lateinit var autoController: TestControllerA

    fun getName() = "\"${this::class.simpleName}\""

    @RequestMapping(HttpMethod.GET, "/b")
    fun getTest() = "= ${autoController.getName()}"
}