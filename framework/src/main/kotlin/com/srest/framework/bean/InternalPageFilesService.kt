package com.srest.framework.bean

import com.srest.framework.annotation.Component
import com.srest.framework.request.Request
import com.srest.framework.response.ContentType
import com.srest.framework.response.Response
import com.srest.framework.util.Constants
import com.srest.framework.util.FileReader

@Component
internal class InternalPageFilesService {

    private var frameworkData: String = ""
    private var filesBuffer: MutableMap<String, String> = mutableMapOf()

    fun getPageFileData(request: Request, response: Response): String {
        response.contentType = ContentType.getTypeForExtension(request.endpoint.split(".")[1])
        val storeFile: Boolean = request.endpoint.contains("/static/") || !Constants.LIVE_RELOAD
        if (storeFile && filesBuffer.containsKey(request.endpoint)) return filesBuffer[request.endpoint]!!
        val fileData = FileReader.loadFileText(request.endpoint) ?: "Cannot load file!"
        if (storeFile) filesBuffer[request.endpoint] = fileData
        return fileData
    }

    fun getFrameworkData(request: Request):String {
        if (!Constants.LIVE_RELOAD && frameworkData.isNotEmpty()) return frameworkData
        val fileData = FileReader.loadFileText(request.endpoint) ?: throw RuntimeException("Cannot load framework.js!")
        if (!Constants.LIVE_RELOAD) frameworkData = fileData
        return fileData
    }

}