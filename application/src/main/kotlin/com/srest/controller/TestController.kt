package com.srest.controller

import com.srest.framework.annotation.RequestParam
import com.srest.framework.request.HttpMethod
import com.srest.framework.response.ContentType
import com.srest.framework.response.Response
import com.srest.framework.util.Controller
import com.srest.framework.util.RequestMapping
import java.io.File
@Controller
internal class TestControllerA {

    init {
        TestControllerA::class.java.declaredMethods.forEach {
            println(it.parameters.map { it.name }.toList())
        }
    }

    @RequestMapping(HttpMethod.GET, "/files")
    fun getTest(@RequestParam(true) fileName: String): String {
//        var filesList = ""
//        File("application").listFiles()
//                ?.forEach { filesList += "<li><a href=\"${it.name}\" >${it.name}</a></li>" }
//        return if (filesList.isNotEmpty()) "<ul>$filesList</ul>" else "<p>no files</p>"
        return "<p>value = $fileName</p>"
    }

//    @RequestMapping(HttpMethod.GET, "/file.jar", ContentType.OCTET_STREAM_TYPE)
//    fun getFile(response: Response) {
//        response.contentData = File("application/messenger.jar").readBytes()
//    }
}
