package com.srest.framework

import com.srest.framework.main.DependencyManager
import com.srest.framework.main.RequestService
import com.srest.framework.properties.PropertiesService
import com.srest.framework.util.RequestLogger
import kotlin.reflect.KClass

class RestFramework(
        baseClass: KClass<*>
) {

    private val dependencyManager: DependencyManager = DependencyManager(baseClass)
    private val propertiesService: PropertiesService = PropertiesService()

    private val requestService: RequestService = RequestService(propertiesService.getServerPort(), dependencyManager)


    fun start() {
        requestService.beginListening()
        RequestLogger.log.info("Framework is started!")
    }

    companion object {
        inline fun <reified T : kotlin.Any> runFramework() {
            val restFramework = RestFramework(T::class)
            restFramework.start()
        }
    }
}