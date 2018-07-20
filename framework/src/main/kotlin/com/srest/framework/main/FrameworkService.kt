package com.srest.framework.main

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.srest.framework.bean.InternalPageFilesService
import com.srest.framework.bean.InternalPageService
import com.srest.framework.inject.ClassInjector
import com.srest.framework.inject.ClassLoader
import com.srest.framework.inject.MappingLoader
import com.srest.framework.inject.WebPageLoader
import com.srest.framework.request.Request
import com.srest.framework.response.ContentType
import com.srest.framework.response.ContentType.TEXT_HTML
import com.srest.framework.response.ErrorResponse
import com.srest.framework.response.Response
import com.srest.framework.util.*
import kotlin.reflect.KClass

internal class FrameworkService(
        baseClass: KClass<*>
): RequestListener {

    private val beansBuffer: MutableMap<String, Any> = mutableMapOf()
    private val beansMappers: MutableList<MethodEntry> = mutableListOf()
    private val jsonMapper = ObjectMapper().registerKotlinModule()

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
        val mapper = getMapper(request)
        mapper?.run {
            getBean(beanName)?.let {
                ClassInjector.invokeMethod(jsonMapper, it, mapper, request)?.let { return it }
            } ?: Logger.log.warn("issue with $beanName beanName!")
        }
        Logger.log.warn("Cannot find response!")
        return getErrorResponse(request)
    }

    // Not Found Response
    private fun getErrorResponse(request: Request): Response {
        val errorResponse = ErrorResponse.buildNotFound(request.endpoint)
        val isHtmlRequest = request.isAccepting(TEXT_HTML)
        val errorType = if (isHtmlRequest) ContentType.TEXT_HTML else ContentType.APP_JSON
        val errorData = if (isHtmlRequest) PageData.getErrorPage(errorResponse) else jsonMapper.writeValueAsString(errorResponse)
        return Response.build(errorType, errorData, errorResponse.code)
    }

    private fun getMapper(request: Request) = beansMappers
            .firstOrNull { it.requestEndpoint == request.endpoint && it.requestMethod == request.method }

    private fun getBean(beanName: String) = beansBuffer[beanName]

}