package com.srest.framework.properties

import com.srest.framework.util.Logger.log
import java.io.File
import java.io.FileInputStream
import java.util.*

internal class PropertiesService {

    val propertiesData = Properties()

    init {
        loadProperties()
    }

    private fun loadProperties() {
        val propertiesFilePath = PropertiesService::class.java.getResource(PropertiesConstants.PROPERTIES_FILE)?.path
        if (propertiesFilePath == null) { log.warn("Cannot find app properties file!"); return }
        val fos = FileInputStream(File(propertiesFilePath))
        propertiesData.load(fos)
        fos.close()
    }

    fun getServerPort() = propertiesData.getProperty(PropertiesConstants.SERVER_PORT_KEY)?.toInt() ?: PropertiesConstants.SERVER_PORT_DEFAULT

}