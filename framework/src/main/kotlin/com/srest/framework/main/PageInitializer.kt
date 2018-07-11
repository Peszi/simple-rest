package com.srest.framework.main

import com.srest.framework.annotation.WebComponent
import com.srest.framework.annotation.web.ChildComponent
import kotlin.reflect.KClass

internal object PageInitializer {

    fun getPageEndpoints(beans: Map<String, Any>, url: String, childComponents: Array<ChildComponent>): List<String> {
        val endpoints = mutableListOf(url)
        fun getChildComponents(component: KClass<*>, url: String){
            val bean = beans[component.simpleName]?.javaClass
            val beanController = bean?.getAnnotation(WebComponent::class.java)
            beanController?.childComponents?.forEach {
                val path = url + it.mapping
                endpoints.add(path)
                getChildComponents(it.component, path)
            }
        }
        childComponents.forEach {
            val path = url + it.mapping
            endpoints.add(path)
            getChildComponents(it.component, path)
        }
        return endpoints
    }

    fun loadWebPage() {

    }

}