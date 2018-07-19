package com.srest.framework.main

import com.srest.framework.bean.InternalPageFilesService
import com.srest.framework.bean.InternalPageService
import com.srest.framework.inject.ClassInjector
import com.srest.framework.inject.ClassLoader
import com.srest.framework.inject.MappingLoader
import com.srest.framework.inject.WebPageLoader
import com.srest.framework.request.HttpMethod
import com.srest.framework.request.Request
import com.srest.framework.response.ContentType
import com.srest.framework.response.Response
import com.srest.framework.util.*
import sun.rmi.runtime.Log
import kotlin.reflect.KClass

internal class FrameworkService(
        baseClass: KClass<*>
): RequestListener {

    private val beansBuffer: MutableMap<String, Any> = mutableMapOf()
    private val beansMappers: MutableList<MethodEntry> = mutableListOf()

    init {
        val targetClasses = ClassLoader.scanForClasses(baseClass,
                InternalPageFilesService::class,
                InternalPageService::class)

        ClassInjector.injectBeans(beansBuffer, targetClasses)
        MappingLoader.addEndpoints(beansMappers, targetClasses)
        WebPageLoader.loadPages(beansBuffer, beansMappers, targetClasses)

        beansBuffer.forEach{ Logger.log.info("Component injected '${it.value::class.simpleName}'") }
        beansMappers.forEach { Logger.log.info("Request bind [${it.requestMethod.name}] '${it.requestEndpoint}'") }
    }

    // Get response
    override fun onResponse(request: Request): Response {
        Logger.log.info("got request [${request.method}] '${request.endpoint}' ${request.params}")
        // Controllers
        beansMappers
                .firstOrNull { it.requestEndpoint == request.endpoint && it.requestMethod == request.method }
                ?.let { val methodEntry = it
                    beansBuffer[it.bean]?.let {
                        ClassInjector.invokeMethod(it, methodEntry, request)?.let { return it }
                    } ?: Logger.log.warn("issue with ${methodEntry.bean} bean!")
                }
        Logger.log.warn("cannot find response!")
        return Response.build(ContentType.HTML_TYPE, PageData.PAGE_NOT_FOUND)
    }
}