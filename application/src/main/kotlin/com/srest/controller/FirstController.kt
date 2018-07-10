package com.srest.controller

import com.srest.framework.request.HttpMethod
import com.srest.framework.util.Controller
import com.srest.framework.util.FileReader
import com.srest.framework.util.RequestMapping

@Controller
internal class FirstController {

    @RequestMapping(HttpMethod.GET, "/web", true)
    fun getWeb(): String {
        return getPage()
    }

    @RequestMapping(HttpMethod.GET, "/content/user")
    fun getHello(): String {
        return "<h3 class=\"m-2\">user!</h3>"
    }

    @RequestMapping(HttpMethod.GET, "/content/spring")
    fun getSpring(): String {
        return "<h3 class=\"m-2\">spring!</h3>"
    }

    fun getPage(): String {
        val webPage = FileReader.loadFileText("index.html")
        val baseScript = FileReader.loadFileText("framework.js")
        return webPage.replace("#{baseScript}", baseScript)
    }
}