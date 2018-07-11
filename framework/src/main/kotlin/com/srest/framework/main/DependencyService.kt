package com.srest.framework.main

import com.srest.framework.annotation.Component
import com.srest.framework.annotation.WebComponent
import com.srest.framework.annotation.web.ChildComponent
import com.srest.framework.annotation.web.WebController
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
        beansBuffer.forEach{ Logger.log.info("Component injected '${it.value::class.simpleName}'") }
        controllerHandlers.forEach { Logger.log.info("Request bind [${it.httpMethod.name}] '${it.mapping}'") }

        webPagesBuffer.forEach{
            val pageName = it.key
            PageInitializer.getPageEndpoints(beansBuffer, it.key, it.value.childComponents).forEach {
                webControllerHandlers.add(WebEntry(pageName, it)) // TODO web entry class
                Logger.log.info("> $it")
            }
        }
    }

    // Init
    private fun lookupComponent(objectClass: Class<*>) {
        // Inject components
        if (ClassInjector.isAnnotated(objectClass, Component::class)) {
            beansBuffer[objectClass.simpleName] = objectClass.newInstance()
        }
        // Store mapping handlers
        if (ClassInjector.isAnnotated(objectClass, Controller::class)) {
            controllerHandlers.addAll(UrlMapper.getMappingHandlers(objectClass))
        }
        // Store web controllers and mappings
        if (ClassInjector.isAnnotated(objectClass, WebController::class)) {
            val webController = objectClass.getAnnotation(WebController::class.java)
            webPagesBuffer[webController.url] = WebPage.build(webController, webController.childComponents)
            Logger.log.info("Web " + objectClass.canonicalName)
            //


            // Add webControllerHandlers

            // Add controllerHandlers
            // if not exists create data endpoints
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

        // lookup TODO

        return Response(ContentType.HTML_TYPE, Pages.NOT_FOUND)
    }

    fun getBean(name: String): Any {
        return beansBuffer[name] ?: throw RuntimeException("bean not exists!")
    }
}