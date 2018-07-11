package com.srest.framework.web

open class WebController(

) {

    var configuration: WebConfiguration? = null

}

data class WebConfiguration(
        val mapping: String,
        val pageDir: String,
        val components: List<WebComponent>
)

data class WebComponent(
        val mapping: String,
        val name: String
)

fun config(block: ConfigurationBuilder.() -> Unit): WebConfiguration = ConfigurationBuilder().apply(block).build()

class ConfigurationBuilder {
    var mapping: String = ""
    var pageDir: String = ""
    var components: MutableList<WebComponent> = mutableListOf()

    fun components(block: Components.() -> Unit) {
        components.addAll(Components().apply(block))
    }

    fun build(): WebConfiguration = WebConfiguration(mapping, pageDir, components)
}

class Components: ArrayList<WebComponent>() {

    fun component(block: ComponentBuilder.() -> Unit) {
        add(ComponentBuilder().apply(block).build())
    }

}

class ComponentBuilder {

    var mapping: String = ""
    var name: String = ""

    fun build() : WebComponent = WebComponent(name, mapping)
}