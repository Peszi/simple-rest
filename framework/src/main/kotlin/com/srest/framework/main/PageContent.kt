package com.srest.framework.main

import com.srest.framework.annotation.web.WebController
import com.srest.framework.inject.WebPageLoader
import com.srest.framework.util.FileReader
import com.srest.framework.util.PageData

internal class PageContent(
        val pageDir: String,
        val pageComponents: Map<String, ComponentContent>,
        var pageData: String = ""
) {

    fun getPageData(liveReload: Boolean): String {
        if (!liveReload && pageData.isNotEmpty()) return pageData
        val fileData = FileReader.loadFileText("$pageDir/index.html") ?: throw RuntimeException("index.html FILE not exists!")
        if (!liveReload) pageData = fileData
        return fileData
    }

    fun getControllerData(endpoint: String, liveReload: Boolean): String {
        val component = pageComponents[endpoint] ?: return PageData.getEmptyComponent(endpoint)
        if (!liveReload && component.componentData.isNotEmpty()) return component.componentData
        val fileData = FileReader.loadFileText("$pageDir/${component.componentFile}") ?: return PageData.getEmptyComponent(endpoint)
        if (!liveReload) component.componentData = fileData
        return fileData
    }

    companion object {
        fun build(webController: WebController) =
                PageContent(
                        webController.dir,
                        WebPageLoader.getControllerEndpoints(webController.url, webController.childComponents)
                                .map { it.endpoint to ComponentContent(it.htmlFile) }.toMap()
                )
    }
}

internal class ComponentContent(
        val componentFile: String,
        var componentData: String = ""
)