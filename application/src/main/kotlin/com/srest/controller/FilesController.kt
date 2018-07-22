package com.srest.controller

import com.srest.framework.annotation.util.RequestParam
import com.srest.framework.request.HttpMethod
import com.srest.framework.response.ContentType
import com.srest.framework.response.Response
import com.srest.framework.util.Controller
import com.srest.framework.util.RequestMapping
import java.io.File
import java.io.FileNotFoundException

const val BASE_DIR = "application/"

@Controller
internal class FilesController {

    @RequestMapping(HttpMethod.GET, "/")
    fun getFilesList(): String {
        var filesList = ""
        File(this::class.java.protectionDomain.codeSource.location.path).parentFile.listFiles()
                .forEach { filesList += "<li><a href=\"/file?name=${it.name}\" >${it.name}</a></li>" }
        return if (filesList.isNotEmpty()) "<ul>$filesList</ul>" else "<p>no files in '$BASE_DIR'</p>"
    }

    @RequestMapping(HttpMethod.GET, "/file", ContentType.APP_OCTET_STREAM_TYPE)
    fun getFile(@RequestParam name: String, response: Response): String {
        try {
            response.headers["Content-Disposition"]= "attachment; filename=$name"
            response.contentData = File(BASE_DIR + name).readBytes()
        } catch (e: FileNotFoundException) {
            response.contentType = ContentType.TEXT_HTML
            return "<p>File '$name' not found!</p>"
        }
        return ""
    }
}