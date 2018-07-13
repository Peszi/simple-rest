package com.srest.framework.main

import com.srest.framework.request.Request
import com.srest.framework.response.ContentType
import com.srest.framework.response.Response
import com.srest.framework.util.Logger
import com.srest.framework.util.PageData
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
            val inputBuffer = BufferedReader(InputStreamReader(client.getInputStream()))
            val outputBuffer = BufferedWriter(OutputStreamWriter(client.getOutputStream()))
            val request = getRequest(inputBuffer)
            Response.writeResponse(outputBuffer, prepareResponse(request))
            outputBuffer.close()
            inputBuffer.close()
        } catch (e: IOException) {
            Logger.log.error("cannot read request data!")
        }
    }

    private fun getRequest(inputBuffer: BufferedReader): Request? {
//        val charBuffer = mutableListOf<Char>()
//        while (true) {
//            val char = inputBuffer.read(); if (char < 0) break
//            charBuffer.add(char.toChar())
//            if (char == 13) {
//                print(String(charBuffer.toCharArray())); charBuffer.clear()
//            }
//        }
        var line = inputBuffer.readLine() ?: return null
        val request = Request.build(line)
        while (true) {
            line = inputBuffer.readLine()
            if (line == null || line.isEmpty()) break
            request.appendHeader(line)
        }
        return request
    }

    private fun prepareResponse(request: Request?): Response {
        if (request != null) return requestListener.onResponse(request)
        Logger.log.warn("corrupted request!")
        return Response(ContentType.HTML_TYPE, PageData.PAGE_NOT_FOUND) // TODO incorrect request
    }
}

internal interface RequestListener {
    fun onResponse(request: Request): Response
}