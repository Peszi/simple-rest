package com.srest.controller

import com.srest.framework.annotation.web.ChildComponent
import com.srest.framework.annotation.web.ComponentRequest
import com.srest.framework.annotation.WebComponent
import com.srest.framework.annotation.web.WebController
import com.srest.framework.request.HttpMethod
import com.srest.framework.util.FileReader
import com.srest.framework.util.RequestMapping

@WebController(
        "/index", "web", [
    (ChildComponent("/user", UserController::class)), // dynamic component
    (ChildComponent("/spring", UserController::class))
])
internal class FirstController {

    @ComponentRequest(UserController::class)
    fun getUser(): Map<String, Int> {
        return mapOf("data" to 0)
    }

    @RequestMapping(HttpMethod.GET, "/web", true)
    fun getWeb(): String {
        return getPage()
    }
//
//    @RequestMapping(HttpMethod.GET, "/content/user")
//    fun getHello(): String {
//        return "<h3 class=\"m-2\">user!</h3>"
//    }
//
//    @RequestMapping(HttpMethod.GET, "/content/spring")
//    fun getSpring(): String {
//        return "<h3 class=\"m-2\">spring!</h3>"
//    }

    fun getPage(): String {
        val webPage = FileReader.loadFileText("index.html")
        val baseScript = FileReader.loadFileText("framework.js")
        return webPage.replace("#{baseScript}", baseScript)
    }
}

@WebComponent("")
internal class UserController {


}