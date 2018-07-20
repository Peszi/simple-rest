package com.srest.framework.properties

import com.srest.framework.util.Logger.log
import java.util.*

internal class PropertiesService {

    val propertiesData = Properties()

    init {
//        loadProperties()
    }

    private fun loadProperties() {
        val propertiesStream = PropertiesService::class.java.getResourceAsStream(PropertiesConstants.PROPERTIES_FILE)
        if (propertiesStream == null) { log.warn("Cannot find app properties file!"); return }
        propertiesData.load(propertiesStream)
        propertiesStream.close()
    }

    fun getServerPort() = propertiesData.getProperty(PropertiesConstants.SERVER_PORT_KEY)?.toInt() ?: PropertiesConstants.SERVER_PORT_DEFAULT

}