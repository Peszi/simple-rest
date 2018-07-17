package com.srest.framework

import com.srest.framework.main.FrameworkService
import com.srest.framework.main.RequestService
import com.srest.framework.properties.PropertiesService
import com.srest.framework.util.Logger
import kotlin.reflect.KClass

class RestFramework(
        baseClass: KClass<*>
) {

    private val startTime = System.currentTimeMillis()
    private val propertiesService: PropertiesService = PropertiesService()
    private val frameworkService: FrameworkService = FrameworkService(baseClass)
    private val requestService: RequestService = RequestService(propertiesService.getServerPort(), frameworkService)

    fun start() {
        Logger.log.info("Listening started on :${propertiesService.getServerPort()}")
        Logger.log.info("Started in ${(System.currentTimeMillis() - startTime) / 1000f} seconds")
        requestService.beginListening()
    }

    companion object {
        inline fun <reified T : kotlin.Any> runFramework() {
            RestFramework(T::class).start()
        }
    }
}