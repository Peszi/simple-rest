package com.srest.framework.main

import com.srest.framework.annotation.web.ChildComponent

internal class ComponentEntry(
        val endpoint: String,
        val bean: String,
        val htmlFile: String
) {
    companion object {
        fun build(endpoint: String, childComponent: ChildComponent) =
                ComponentEntry(endpoint, childComponent.component.simpleName ?: "", childComponent.htmlFile)
    }
}