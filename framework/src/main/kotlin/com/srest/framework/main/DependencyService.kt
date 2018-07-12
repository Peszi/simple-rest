package com.srest.framework.main

import com.srest.framework.annotation.Component
import com.srest.framework.annotation.web.WebController
import com.srest.framework.request.HttpMethod
import com.srest.framework.request.Request
import com.srest.framework.response.ContentType
import com.srest.framework.response.Response
import com.srest.framework.util.*
import kotlin.reflect.KClass

internal class DependencyService(
        baseClass: KClass<*>
): RequestListener {

    private val beansBuffer: MutableMap<String, Any> = mutableMapOf()
    private val controllerHandlers: MutableList<MethodEntry> = mutableListOf()

    private val webPagesBuffer: MutableMap<String, WebPage> = mutableMapOf()
    private val webControllerHandlers: MutableList<WebEntry> = mutableListOf()

    init {
        ClassInjector.scanForClasses(baseClass).forEach { lookupComponent(it) }

        webPagesBuffer.forEach{
            val pageName = it.key; val webPage = it.value
            webControllerHandlers.add(WebEntry(pageName, pageName))
            PageInitializer.getPageEndpoints(pageName, webPage).forEach {
                webPage.addComponent(it)
                webControllerHandlers.add(WebEntry(pageName, it.endpoint))
            }
        }

        beansBuffer.forEach{ Logger.log.info("Component injected '${it.value::class.simpleName}'") }
        controllerHandlers.forEach { Logger.log.info("Request bind [${it.httpMethod.name}] '${it.mapping}'") }
        webControllerHandlers.forEach { Logger.log.info("Request bind [WEB] '${it.endpoint}'") }
        webPagesBuffer.forEach { Logger.log.info("Web page '${it.key}' '${it.value.dir}'") }
    }

    // Init
    private fun lookupComponent(objectClass: Class<*>) {
        // Inject components
        if (ClassInjector.isAnnotated(objectClass, Component::class)) {
            beansBuffer[objectClass.simpleName] = objectClass.newInstance()
        }
        // Store endpoint handlers
        if (ClassInjector.isAnnotated(objectClass, Controller::class)) {
            controllerHandlers.addAll(UrlMapper.getMappingHandlers(objectClass))
        }
        // Store web controllers and mappings
        if (ClassInjector.isAnnotated(objectClass, WebController::class)) {
            val webController = objectClass.getAnnotation(WebController::class.java)
            webPagesBuffer[webController.url] = WebPage.build(objectClass.simpleName, webController)
        }
    }

    // Get response
    override fun onResponse(request: Request): Response {
        Logger.log.info("got request [${request.method}] '${request.endpoint.getEndpoint()}'")
        val endpoint = request.endpoint.getEndpoint()
        // Controllers
        val handler = controllerHandlers.firstOrNull { it.mapping == endpoint && it.httpMethod == request.method }
        if (handler != null) {
            val bean = beansBuffer[handler.bean]
            if (bean != null) return ClassInjector.invokeMethodResult(bean, handler.method)
        }
        // Web controllers
        webControllerHandlers.firstOrNull { it.endpoint == endpoint }.let {
            if (it == null) return@let
            val webPage = webPagesBuffer[it.page] ?: return@let
            return if (request.method == HttpMethod.GET) webPage.getWebPageResponse(endpoint) else webPage.getComponentResponse(endpoint)
        }
        // Framework JS
        if (request.method == HttpMethod.GET && endpoint == "/framework.js") {
            return Response(ContentType.HTML_TYPE, FileReader.loadFileText("web/framework.js") ?: "alert('no framework')")
        }
        // lookup TODO
        Logger.log.warn("cannot find response!")
        return Response(ContentType.HTML_TYPE, Pages.PAGE_NOT_FOUND)
    }

    fun getBean(name: String): Any {
        return beansBuffer[name] ?: throw RuntimeException("bean not exists!")
    }
}