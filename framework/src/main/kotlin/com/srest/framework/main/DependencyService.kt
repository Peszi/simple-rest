package com.srest.framework.main

import com.srest.framework.annotation.Component
import com.srest.framework.annotation.web.WebController
import com.srest.framework.bean.InternalFileLoader
import com.srest.framework.request.HttpMethod
import com.srest.framework.request.Request
import com.srest.framework.response.ContentType
import com.srest.framework.response.Response
import com.srest.framework.util.*
import kotlin.reflect.KClass
import kotlin.reflect.full.cast

internal class DependencyService(
        baseClass: KClass<*>
): RequestListener {

    private val beansBuffer: MutableMap<String, Any> = mutableMapOf()
    private val beansHandlers: MutableList<MethodEntry> = mutableListOf()

    private val webPagesBuffer: MutableMap<String, WebPage> = mutableMapOf()
    private val webControllerHandlers: MutableList<WebEntry> = mutableListOf()

    init {
        PageLoader.initLoader(beansBuffer)
        ClassInjector.scanForClasses(baseClass).forEach { lookupComponent(it) }

        // Setup pages components
        webPagesBuffer.forEach{
            val pageName = it.key; val webPage = it.value
            webControllerHandlers.add(WebEntry(pageName, pageName))
            PageLoader.loadPageEndpoints(pageName, webPage).forEach {
                webPage.addComponent(it)
                webControllerHandlers.add(WebEntry(pageName, it.endpoint))
            }
        }

        // Load pages files
        val loaderBeanName = InternalFileLoader::class.simpleName
        val loaderBean = beansBuffer[loaderBeanName]?.let {
            val internalFileLoader = InternalFileLoader::class.cast(it)
            beansHandlers.filter { it.bean == loaderBeanName }.forEach {
                internalFileLoader.loadInternalFile(it.requestEndpoint)
            }
        }


        beansBuffer.forEach{ Logger.log.info("Component injected '${it.value::class.simpleName}'") }
        beansHandlers.forEach { Logger.log.info("Request bind [${it.requestMethod.name}] '${it.requestEndpoint}'") }
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
            beansHandlers.addAll(UrlMapper.getMappingHandlers(objectClass))
        }
        // Store web controllers and mappings
        if (ClassInjector.isAnnotated(objectClass, WebController::class)) {
            val webController = objectClass.getAnnotation(WebController::class.java)
            webPagesBuffer[webController.url] = WebPage.build(objectClass.simpleName, webController)

            beansHandlers.addAll(PageLoader.loadIncludedFiles(webController.dir + "/static"))
            beansHandlers.addAll(PageLoader.loadIncludedFiles(webController.dir))
        }
    }

    // Get response
    override fun onResponse(request: Request): Response {
        Logger.log.info("got request [${request.method}] '${request.endpoint.getEndpoint()}'")
        val endpoint = request.endpoint.getEndpoint()
        // Controllers
        val handler = beansHandlers.firstOrNull { it.requestEndpoint == endpoint && it.requestMethod == request.method }
        if (handler != null) {
            val bean = beansBuffer[handler.bean]
            if (bean == null) Logger.log.warn("cannot get ${handler.bean} bean!")
            else return ClassInjector.invokeMethodForResponse(bean, handler, request)
        }
        // Web controllers
        webControllerHandlers.firstOrNull { it.endpoint == endpoint }.let {
            if (it == null) return@let
            val webPage = webPagesBuffer[it.page] ?: return@let
            return if (request.method == HttpMethod.GET) webPage.getWebPageResponse(endpoint) else webPage.getComponentResponse(endpoint)
        }
        // Framework JS
        if (request.method == HttpMethod.GET && endpoint == "/framework.js") {
            return Response(ContentType.JS_TYPE, FileReader.loadFileText("framework.js") ?: "alert('no framework')")
        }
        // lookup TODO
        Logger.log.warn("cannot find response!")
        return Response(ContentType.HTML_TYPE, PageData.PAGE_NOT_FOUND)
    }

    fun getBean(name: String): Any {
        return beansBuffer[name] ?: throw RuntimeException("bean not exists!")
    }
}