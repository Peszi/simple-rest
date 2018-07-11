package com.srest.framework

import com.srest.framework.main.DependencyService
import com.srest.framework.main.RequestListener
import com.srest.framework.main.RequestService
import com.srest.framework.properties.PropertiesService
import com.srest.framework.request.Request
import com.srest.framework.response.Response
import com.srest.framework.util.Logger
import kotlin.reflect.KClass

class RestFramework(
        baseClass: KClass<*>
) {

    private val propertiesService: PropertiesService = PropertiesService()
    private val dependencyService: DependencyService = DependencyService(baseClass)
    private val requestService: RequestService = RequestService(propertiesService.getServerPort(), dependencyService)

    fun start() {
        Logger.log.info("listening started on :${propertiesService.getServerPort()}")
        requestService.beginListening()
    }

    companion object {
        inline fun <reified T : kotlin.Any> runFramework() {
            RestFramework(T::class).start()
        }
    }
}