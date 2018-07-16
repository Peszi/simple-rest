package com.srest.framework.bean

import com.srest.framework.annotation.Component
import com.srest.framework.annotation.web.WebController
import com.srest.framework.main.PageContent
import com.srest.framework.request.Request
import com.srest.framework.util.Constants

@Component
internal class InternalPageService {

    private var webPagesMap = mutableMapOf<String, PageContent>()

    fun addWebPage(webController: WebController) {
        webPagesMap[webController.url] = PageContent.build(webController)
    }

    fun getPageContent(request: Request): String {
        if (webPagesMap.size == 1) return webPagesMap.values.first().getPageData(Constants.LIVE_RELOAD)
//        val baseUrl = getBaseUrl(request.endpoint) TODO multiple sites
        return "ERROR multiple sites not supported yet!"
    }

    fun getComponentContent(request: Request): String {
        if (webPagesMap.size == 1) return webPagesMap.values.first().getControllerData(request.endpoint, Constants.LIVE_RELOAD)
//        val baseUrl = getBaseUrl(request.endpoint) TODO multiple sites
        return "ERROR multiple sites not supported yet!"
    }

    // Utility

    private fun getBaseUrl(endpoint: String): String {
        val urlIndex = endpoint.replaceFirst("/", "").indexOf("/")
        if (urlIndex < 0) return endpoint
        return endpoint.substring(0, urlIndex)
    }
}