package com.srest.framework.response

import com.fasterxml.jackson.databind.ObjectMapper
import com.srest.framework.request.Request
import com.srest.framework.response.ResponseConstants.SERVER_NAME
import java.io.BufferedOutputStream
import java.io.BufferedWriter
import java.io.File
import java.io.FileInputStream
import java.nio.charset.Charset

class Response(
        var contentType: String,
        var contentData: ByteArray = ByteArray(0),
        var responseCode: Int = 200,
        var headers: MutableMap<String, String> = mutableMapOf()
) {

    fun setType(type: String) {
        if (contentType == ContentType.TEXT_HTML) contentType = type
    }

    fun setData(stringContent: String) {
        if (contentData.isEmpty()) contentData = stringContent.toByteArray(Charsets.UTF_8)
    }

    companion object {

        fun build(contentType: String, stringContent: String) = Response(
                contentType, stringContent.toByteArray(Charsets.UTF_8)
        )

        fun build(contentType: String, stringContent: String, responseCode: Int) = Response(
                contentType, stringContent.toByteArray(Charsets.UTF_8), responseCode
        )

        fun getHeadersArray(response: Response) = (
                "HTTP/1.1 ${response.responseCode}\r\n" +
                "Server: $SERVER_NAME\r\n" +
                "Content-Type: ${response.contentType}\r\n" +
                "Content-Length: ${response.contentData.size}\r\n" +
                        Response.headersToString(response) + "\r\n"
        ).toByteArray(Charsets.UTF_8)

        fun headersToString(response: Response): String {
            var headers = ""; response.headers.forEach { headers += "${it.key}: ${it.value}\r\n" }; return headers
        }
    }
}