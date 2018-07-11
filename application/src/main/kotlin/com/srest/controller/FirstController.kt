package com.srest.controller

import com.srest.framework.annotation.Component
import com.srest.framework.annotation.web.ChildComponent
import com.srest.framework.annotation.web.ComponentRequest
import com.srest.framework.annotation.WebComponent
import com.srest.framework.annotation.web.WebController
import com.srest.framework.request.HttpMethod
import com.srest.framework.util.Controller
import com.srest.framework.util.FileReader
import com.srest.framework.util.RequestMapping

@WebController("/index", "web", [
    ChildComponent("/user", UserController::class),
    ChildComponent("/spring", SpringController::class)
])
internal class FirstController {

    @ComponentRequest(UserController::class)
    fun getUser(): Map<String, Int> {
        return mapOf("data" to 0)
    }

    @RequestMapping(HttpMethod.GET, "/web", true)
    fun getWeb(): String = getPage()

    fun getPage(): String {
        val webPage = FileReader.loadFileText("index.html")
        val baseScript = FileReader.loadFileText("framework.js")
        return webPage.replace("#{baseScript}", baseScript)
    }
}

@WebComponent("user.html", childComponents = [
    ChildComponent("/john", JohnController::class)
])
internal class UserController

@WebComponent("spring.html")
internal class SpringController

@WebComponent("john.html")
internal class JohnController