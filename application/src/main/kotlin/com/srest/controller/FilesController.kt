package com.srest.controller

import com.srest.framework.annotation.util.RequestParam
import com.srest.framework.request.HttpMethod
import com.srest.framework.response.ContentType
import com.srest.framework.response.Response
import com.srest.framework.util.Controller
import com.srest.framework.util.RequestMapping
import java.io.File
import java.io.FileNotFoundException

@Controller
internal class FilesController {

    val BASE_PATH = this::class.java.protectionDomain.codeSource.location.path.substringBeforeLast("/").substringBeforeLast("/") + "/"

    @RequestMapping(HttpMethod.GET, "/")
    fun getFilesList(): String {
        var filesList = ""
        File(BASE_PATH).listFiles()
                .filter { !it.isDirectory }
                .forEach { filesList += getFileString(it.name) }
        return if (filesList.isNotEmpty()) getPage(filesList) else "<p>no files in '$BASE_PATH'</p>"
    }

    @RequestMapping(HttpMethod.GET, "/file", ContentType.APP_OCTET_STREAM_TYPE)
    fun getFile(@RequestParam name: String, response: Response): String {
        try {
            response.headers["Content-Disposition"] = "attachment; filename=$name"
            response.contentData = File(BASE_PATH + name).readBytes()
        } catch (e: FileNotFoundException) {
            response.contentType = ContentType.TEXT_HTML
            return "<p>File '$name' not found!</p>"
        }
        return ""
    }

    fun toShortName(name: String): String {
        val MAX_LEN = 12
        if (name.length > MAX_LEN)
            return "${name.substring(0, MAX_LEN - 3)}..."
        return name
    }

    fun getFileString(fileName: String) =
            "<a href=\"/file?name=$fileName\"><div class=\"file\">\n" +
                    "   <h1 class=\"m-0 image\"><i class=\"fa fa-file-o\" aria-hidden=\"true\"></i></h1>\n" +
                    "   <p class=\"title\">${toShortName(fileName)}</p>\n" +
                    "</div></a>\n"

    fun getPage(filesList: String) = "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <title>Title</title>\n" +
            "    <link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css\">\n" +
            "    <style>\n" +
            "        .m-0 {\n" +
            "            margin: 0;\n" +
            "        }\n" +
            "        .container {\n" +
            "            margin-left: -0.0rem;\n" +
            "            padding: 0.25rem;\n" +
            "            display: flex;\n" +
            "            flex-flow: row;\n" +
            "        }\n" +
            "        .border {\n" +
            "            border: 1px solid lightgrey;\n" +
            "            border-radius: 0.25rem;\n" +
            "        }\n" +
            "        .file {\n" +
            "            margin: 0.25rem;\n" +
            "            padding: 0.5rem;\n" +
            "            border: 1px solid lightgrey;\n" +
            "            border-radius: 0.25rem;\n" +
            "            display: flex;\n" +
            "            flex-flow: column;\n" +
            "            flex-basis: content;\n" +
            "            justify-content: center;\n" +
            "            box-shadow: 2px 2px #888888;\n" +
            "        }\n" +
            "        .file:hover {\n" +
            "            box-shadow: 4px 4px #aaaaaa;\n" +
            "            cursor: pointer;\n" +
            "        }\n" +
            "        .image {\n" +
            "            text-align: center;\n" +
            "            margin-bottom: 0.2rem;\n" +
            "        }\n" +
            "        .title {\n" +
            "            margin: 0;\n" +
            "            padding: 0.1rem 0.25rem;\n" +
            "            color: white !important;\n" +
            "            font-size: 0.8rem;\n" +
            "            background-color: #1d2124;\n" +
            "            border-radius: 1rem;\n" +
            "            text-overflow: ellipsis;\n" +
            "            word-wrap: break-word;\n" +
            "            overflow: hidden;\n" +
            "            text-align: center;\n" +
            "            width: 60px;\n" +
            "            height: 0.9rem;\n" +
            "        }\n" +
            "        a {\n" +
            "            color: black !important;\n" +
            "            text-decoration: none !important;\n" +
            "        }\n" +
            "        a:visited {\n" +
            "            color: #1d2124;\n" +
            "        }\n" +
            "    </style>\n" +
            "</head>\n" +
            "<body style=\"margin: 1rem\">\n" +
            "    <div class=\"container\">\n" +
            "       $filesList\n" +
            "    </div>\n" +
            "</body>\n" +
            "</html>"
}