package com.srest.framework.main

import com.srest.framework.annotation.web.ChildComponent
import com.srest.framework.annotation.web.WebController
import com.srest.framework.response.ContentType
import com.srest.framework.response.Response
import com.srest.framework.util.FileReader
import com.srest.framework.util.Pages

const val LIVE_RELOAD = true

internal class WebPage(
        val dir: String,
        val url: String,
        val bean: String,
        val childComponents: Array<ChildComponent>
) {

    private var components: MutableMap<String, String> = mutableMapOf()

    private var pageBuffer: String = loadPageFile()
    private var componentsBuffer: MutableMap<String, String> = mutableMapOf()

    fun addComponent(component: ComponentEntry) { // TODO
        components[component.endpoint] = component.htmlFile
        loadComponentFile(component.htmlFile)?.let { componentsBuffer[component.endpoint] = it }
    }

    // Responses

    fun getComponentResponse(endpoint: String) =
            if (endpoint != url) Response(ContentType.HTML_TYPE, getComponentData(endpoint)) else Response(ContentType.HTML_TYPE, "")

    fun getWebPageResponse(endpoint: String) =
            Response(ContentType.HTML_TYPE, getPageData())

    // Get string data

    private fun getPageData(): String {
        return if (LIVE_RELOAD) loadPageFile() else pageBuffer
    }

    private fun getComponentData(endpoint: String): String { // TODO
        if (LIVE_RELOAD && components[endpoint] != null) return loadComponentFile(components[endpoint]!!) ?: "<div>component file not exists!</div>"
        return componentsBuffer[endpoint] ?: Pages.getComponentNotFoundPage(endpoint)
    }

    // Load from file

    private fun loadPageFile(): String = FileReader.loadFileText("$dir/index.html") ?: throw RuntimeException("index.html file not exists!")

    private fun loadComponentFile(path: String): String? = FileReader.loadFileText("$dir/$path")

    companion object {
        fun build(component: String, webController: WebController) =
                WebPage(webController.dir, webController.url, component, webController.childComponents)
    }

    //        val baseScript = FileReader.loadFileText("$dir/framework.js") ?: ""
//        return webPage?.replace("#{baseScript}", baseScript)
}