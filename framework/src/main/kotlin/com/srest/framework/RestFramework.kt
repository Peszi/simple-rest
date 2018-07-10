package com.srest.framework

import com.srest.framework.main.ResponseManager
import com.srest.framework.main.RequestService
import com.srest.framework.properties.PropertiesService
import com.srest.framework.util.Logger
import kotlin.reflect.KClass

class RestFramework(
        baseClass: KClass<*>
) {

    private val responseManager: ResponseManager = ResponseManager(baseClass)
    private val propertiesService: PropertiesService = PropertiesService()

    private val requestService: RequestService = RequestService(propertiesService.getServerPort(), responseManager)


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