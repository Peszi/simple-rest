package com.srest.controller

import com.srest.framework.request.HttpMethod
import com.srest.framework.util.Controller
import com.srest.framework.util.RequestMapping
import java.io.BufferedReader
import java.io.FileReader
import java.io.InputStreamReader

@Controller
internal class SimpleController {

    fun loadPage(): String {
        var webPage = ""
        val fileReader = FileReader(SimpleController::class.java.classLoader.getResource("static/page.html").path)
        fileReader.readLines().forEach { webPage += it }
        fileReader.close()
        return webPage
    }

    @RequestMapping(HttpMethod.GET)
    fun getIndex(): String {
        return loadPage()
    }

    @RequestMapping(HttpMethod.GET, "/user")
    fun getHello(): String {
        return "User"
    }

    @RequestMapping(HttpMethod.GET, "/index")
    fun getSpring(): String {
        return "<html><h1>spring!</h1></html>"
    }
}