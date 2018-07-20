package com.srest.controller

import com.srest.framework.annotation.RestController
import com.srest.framework.annotation.util.RequestParam
import com.srest.framework.annotation.util.ResponseBody
import com.srest.framework.request.HttpMethod
import com.srest.framework.response.ContentType
import com.srest.framework.response.Response
import com.srest.framework.response.ResponseEntity
import com.srest.framework.util.Controller
import com.srest.framework.util.RequestMapping
import java.io.File

//@RestController
//internal class TestControllerA {
//
//    @RequestMapping(HttpMethod.GET, "/files")
//    fun getTest(@RequestParam(false) fileName: String): String {
////        var filesList = ""
////        File("application").listFiles()
////                ?.forEach { filesList += "<li><a href=\"${it.name}\" >${it.name}</a></li>" }
////        return if (filesList.isNotEmpty()) "<ul>$filesList</ul>" else "<p>no files</p>"
//        return "<p>value = $fileName</p>"
//    }
//
//    @RequestMapping(HttpMethod.GET, "/files2")
//    fun getTest2(@RequestParam(false) fileName: String): ResponseEntity<SomeData> {
////        var filesList = ""
////        File("application").listFiles()
////                ?.forEach { filesList += "<li><a href=\"${it.name}\" >${it.name}</a></li>" }
////        return if (filesList.isNotEmpty()) "<ul>$filesList</ul>" else "<p>no files</p>"
//        return ResponseEntity(SomeData("value123"), 404)
//    }
//
//    @RequestMapping(HttpMethod.GET, "/file.jar", ContentType.APP_OCTET_STREAM_TYPE)
//    fun getFile(response: Response) {
//        response.contentData = File("application/messenger.jar").readBytes()
//    }
//}
//
//internal class SomeData(
//        val name: String
//)
