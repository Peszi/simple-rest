package com.srest.framework.response

internal object ResponseConstants {

    const val SERVER_NAME = "CheapSpring/0.0.1"

}

internal object ContentType {

    const val JSON_TYPE = "application/json" // application/json;charset=UTF-8
    const val CSS_TYPE = "text/css"
    const val JS_TYPE = "application/javascript"
    const val HTML_TYPE = "text/html"

    fun getTypeForExtension(extension: String) = when(extension) {
        "html"  -> HTML_TYPE
        "js"    -> JS_TYPE
        "css"   -> CSS_TYPE
        "json"  -> JSON_TYPE
        else    -> HTML_TYPE
    }
}