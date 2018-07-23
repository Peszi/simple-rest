package com.srest.framework.properties

import com.srest.framework.util.Logger
import java.lang.NullPointerException
import java.util.*
import kotlin.reflect.KClass

internal class PropertiesService(
        baseClass: KClass<*>
) {

    private val propertiesData = Properties()

    init {
        loadProperties(baseClass)
    }

    private fun loadProperties(baseClass: KClass<*>) {
        baseClass.java.classLoader.getResourceAsStream(PropertiesConstants.PROPERTIES_FILE)?.let {
            propertiesData.load(it)
            it.close()
        } ?: Logger.log.error("Cannot find properties file path=\"${PropertiesConstants.PROPERTIES_FILE}\"!")
    }

    fun getServerPort() = propertiesData.getProperty(PropertiesConstants.SERVER_PORT_KEY)?.toInt()
            ?: PropertiesConstants.SERVER_PORT_DEFAULT

}