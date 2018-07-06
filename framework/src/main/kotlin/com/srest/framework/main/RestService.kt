package com.srest.framework.main

import com.srest.framework.properties.PropertiesService
import com.srest.framework.util.RequestLogger

internal class RestService {

    private val propertiesService: PropertiesService
    private val requestService: RequestService

    init {
        RequestLogger.log.info("SRest is started!")
        propertiesService = PropertiesService()
        requestService = RequestService(propertiesService.getServerPort())
    }

}