package com.srest.framework.main

import com.srest.framework.request.Request
import com.srest.framework.response.ContentType
import com.srest.framework.response.Response
import com.srest.framework.util.Logger
import com.srest.framework.util.PageData
import com.srest.framework.util.StreamUtility
import java.io.*
import java.net.ServerSocket
import java.net.Socket

internal class RequestService(
        serverPort: Int,
        private val requestListener: RequestListener
) {

    private val serverSocket: ServerSocket = ServerSocket(serverPort)

    fun beginListening() {
        while (true) {
            val client = serverSocket.accept()
            processRequest(client)
            client.close()
        }
    }

    private fun processRequest(client: Socket) {
        try {
            val inputBuffer = BufferedInputStream(client.getInputStream())
            val outputBuffer = BufferedOutputStream(client.getOutputStream())
            StreamUtility.readRequest(inputBuffer)
                    ?.run { StreamUtility.writeResponse(outputBuffer, prepareResponse(this)) }
            outputBuffer.close()
            inputBuffer.close()
        } catch (e: IOException) {
            Logger.log.error("cannot read request data!")
        }
    }

    private fun prepareResponse(request: Request?): Response {
        if (request != null) return requestListener.onResponse(request)
        Logger.log.warn("corrupted request!")
        return Response.build(ContentType.HTML_TYPE, PageData.PAGE_NOT_FOUND) // TODO incorrect request
    }
}