package com.srest.framework

import com.srest.framework.main.DependencyManager
import com.srest.framework.main.RequestListener
import com.srest.framework.main.ResponseService
import com.srest.framework.main.RequestService
import com.srest.framework.properties.PropertiesService
import com.srest.framework.request.Request
import com.srest.framework.response.Response
import com.srest.framework.util.Logger
import kotlin.reflect.KClass

class RestFramework(
        baseClass: KClass<*>
): RequestListener {

    private val propertiesService: PropertiesService = PropertiesService()
    private val dependencyManager: DependencyManager = DependencyManager(baseClass)
    private val requestService: RequestService = RequestService(propertiesService.getServerPort(), this)

    fun start() {
        Logger.log.info("listening started on :${propertiesService.getServerPort()}")
        requestService.beginListening()
    }

    override fun onResponse(request: Request): Response {
        Logger.log.info("got request [${request.method}] '${request.endpoint.getEndpoint()}'")
        return ResponseService.getResponseResult(request)
    }

    companion object {
        inline fun <reified T : kotlin.Any> runFramework() {
            RestFramework(T::class).start()
        }
    }
}