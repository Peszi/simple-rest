package com.srest.framework.util

import com.srest.framework.request.Request
import com.srest.framework.response.Response
import java.io.BufferedInputStream
import java.io.BufferedOutputStream

internal object StreamUtility {

    private const val INPUT_BUFFER_SIZE = 128
    private const val OUTPUT_BUFFER_SIZE = 128

    fun readRequest(inputBuffer: BufferedInputStream): Request? {
        val buffer = ByteArray(INPUT_BUFFER_SIZE)
        val stringBuffer = StringBuilder()
        while (true) {
            val bufferCount = inputBuffer.read(buffer)
            if (bufferCount < 0) { break }
            stringBuffer.append(String(buffer, 0, bufferCount))
            if (bufferCount < INPUT_BUFFER_SIZE) break
        }
        if (stringBuffer.isEmpty()) return null
        return Request.build(stringBuffer.split("\r\n"))
    }

    fun writeResponse(outputBuffer: BufferedOutputStream, response: Response) {
        sendBuffer(outputBuffer, Response.getHeadersArray(response))
        sendBuffer(outputBuffer, response.contentData)
    }

   private fun sendBuffer(outputBuffer: BufferedOutputStream, dataBuffer: ByteArray) {
        var offset = 0
        while (true) {
            val packetSize = Math.min(OUTPUT_BUFFER_SIZE, dataBuffer.size - offset)
            outputBuffer.write(dataBuffer, offset, packetSize)
            offset += packetSize
            if (packetSize < OUTPUT_BUFFER_SIZE) break
        }
    }
}