package com.srest.framework.response

import com.srest.framework.response.ResponseConstants.SERVER_NAME
import java.io.BufferedWriter

internal class Response(
        val contentType: String,
        val contentData: String
) {

    companion object {

        fun build() {}

        fun getHeaders(response: Response): List<String> = listOf(
                "HTTP/1.1 200 OK",
                "Server: $SERVER_NAME",
                "Content-Type: ${response.contentType}",
                "Content-Length: ${response.contentData.length}"
        )

        fun writeResponse(outputBuffer: BufferedWriter, response: Response) {
            for (header in getHeaders(response))
                outputBuffer.write("$header\r\n")
            outputBuffer.write("\r\n")
            outputBuffer.write(response.contentData)
        }
    }
}