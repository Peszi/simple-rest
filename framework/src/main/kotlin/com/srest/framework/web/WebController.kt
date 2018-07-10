package com.srest.framework.web

open class WebController(
        val configuration: WebConfiguration
) {

}

class WebConfiguration(
        var mapping: String = "/",
        var pageDir: String = "",
        var component: WebComponent = WebComponent()
) {

    companion object {
        fun config(block: WebConfiguration.() -> Unit): WebConfiguration = WebConfiguration().apply(block)

        fun WebConfiguration.component(block: WebComponent.() -> Unit) { component = WebComponent().apply(block) }
    }
}

class WebComponent(
        var name: String = "component",
        var mapping: String = "/component"
)