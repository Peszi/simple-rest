package com.srest.framework.main

import com.srest.framework.annotation.web.ChildComponent

internal object PageInitializer {

    fun getPageEndpoints(endpoint: String, webPage: WebPage): List<ComponentEntry> {
        val components = mutableListOf<ComponentEntry>() // ComponentEntry(endpoint, "", "")
        fun getChildComponents(childComponents: Array<ChildComponent>, url: String){
            childComponents.forEach {
                val endpoint = url + it.mapping
                components.add(ComponentEntry.build(endpoint, it))
                getChildComponents(it.childComponents, endpoint)
            }
        }
        getChildComponents(webPage.childComponents, endpoint)
        return components
    }

    fun setupPageLinks(pageData: String) {
//        pageData.inde
    }
}