package com.srest.framework.bean

import com.srest.framework.request.Request
import com.srest.framework.response.ContentType
import com.srest.framework.response.ResponseEntity
import com.srest.framework.response.ResponseParams
import com.srest.framework.util.FileReader

internal class InternalFileLoader {

    private var filesBuffer: MutableMap<String, String> = mutableMapOf()

    fun loadInternalFile(path: String) {
        println("PATH " + path)
        FileReader.loadFileText(path)?.let { filesBuffer[path] = it }
    }

    fun getInternalFileResponse(request: Request, responseParams: ResponseParams): String {
        println("loading '${request.endpoint.getEndpoint()}'")
        responseParams.contentType = ContentType.getTypeForExtension(request.endpoint.getEndpoint().split(".")[1])
        return FileReader.loadFileText(request.endpoint.getEndpoint()) ?: throw RuntimeException("Cannot find ${request.endpoint.getEndpoint()} file!")
    }

}