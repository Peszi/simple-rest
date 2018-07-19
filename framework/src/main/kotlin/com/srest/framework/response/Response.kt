package com.srest.framework.response

import com.srest.framework.request.Request
import com.srest.framework.response.ResponseConstants.SERVER_NAME
import java.io.BufferedOutputStream
import java.io.BufferedWriter
import java.io.File
import java.io.FileInputStream
import java.nio.charset.Charset

class Response(
        var contentType: String,
        var contentData: ByteArray = ByteArray(0)
) {

    companion object {

        fun build(contentType: String, stringContent: String) = Response(
                contentType, stringContent.toByteArray(Charsets.UTF_8)
        )

        fun getHeadersArray(response: Response) = (
                "HTTP/1.1 200 OK\r\n" +
                "Server: $SERVER_NAME\r\n" +
                "Content-Type: ${response.contentType}\r\n" +
                "Content-Length: ${response.contentData.size}\r\n" + "\r\n"
        ).toByteArray(Charsets.UTF_8)
    }
}