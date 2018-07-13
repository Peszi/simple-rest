package com.srest.framework.main

import com.srest.framework.annotation.web.ChildComponent
import com.srest.framework.bean.InternalFileLoader
import com.srest.framework.request.HttpMethod
import com.srest.framework.request.Request
import com.srest.framework.response.ContentType
import com.srest.framework.response.Response
import com.srest.framework.response.ResponseParams
import com.srest.framework.util.RequestMapping
import java.io.File

internal object PageLoader {

    private val INTERNAL_FILES_EXTENSIONS = listOf("js", "css", "map")

    private val loadMethod = InternalFileLoader::class.java.getDeclaredMethod("getInternalFileResponse", Request::class.javaObjectType, ResponseParams::class.javaObjectType)

    fun initLoader(beansBuffer: MutableMap<String, Any>) {
        beansBuffer[InternalFileLoader::class.simpleName!!] = InternalFileLoader()
        if (loadMethod == null) throw RuntimeException("Cannot load InternalFileLoader bean!")
    }

    fun loadPageEndpoints(endpoint: String, webPage: WebPage): List<ComponentEntry> {
        val components = mutableListOf<ComponentEntry>() // ComponentEntry(endpoint, "", "")
        fun getChildComponents(childComponents: Array<ChildComponent>, url: String){
            childComponents.forEach {
                val endpoint = url + it.mapping
                components.add(ComponentEntry.build(endpoint, it))
                getChildComponents(it.childComponents, endpoint)
            }
        }
        getChildComponents(webPage.childComponents, endpoint)
        return components
    }

    fun loadIncludedFiles(path: String): List<MethodEntry> {
        val filesHandlers = mutableListOf<MethodEntry>()
        println("dir: " + "application/$path")
        val workingDir = File("application/$path")
        if (!workingDir.isDirectory) return filesHandlers
        workingDir.listFiles().forEach {
            val fileExt = it.extension
            if (it.isFile && !INTERNAL_FILES_EXTENSIONS.firstOrNull { it == fileExt }.isNullOrEmpty() ) {
                println("supported internal file " + it.name)
                filesHandlers.add(MethodEntry(InternalFileLoader::class.simpleName!!, loadMethod,
                        "/$path/${it.name}", HttpMethod.GET, ContentType.getTypeForExtension(fileExt)))
            }
        }
        return filesHandlers
    }
}