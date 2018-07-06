package com.srest.framework.main

import com.srest.framework.request.Request
import com.srest.framework.response.ContentType
import com.srest.framework.response.Response
import com.srest.framework.util.RequestLogger
import java.io.*
import java.net.ServerSocket
import java.net.Socket

internal class RequestService(
        serverPort: Int
) {

    private val serverSocket: ServerSocket = ServerSocket(serverPort)

    init {
        requestLoop()
    }

    private fun requestLoop() {
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
            RequestLogger.log.error("cannot read request data!")
        }
    }

    private fun getRequest(inputBuffer: BufferedReader): Request {
        var line = inputBuffer.readLine()
        val request = Request.build(line)
        while (true) {
            line = inputBuffer.readLine()
            if (line == null || line.isEmpty()) break
            request.appendHeader(line)
        }
        RequestLogger.log.info("read full request!")
        return request
    }

    private fun prepareResponse(request: Request): Response {
        // TODO
        RequestLogger.log.info("Request METHOD " + request.method.name)
        RequestLogger.log.info("Request ENDPOINT " + request.endpoint.layers.map{ "'$it'" })

        return Response(ContentType.HTML_TYPE, "<html><p>data</p></html>")
    }

}