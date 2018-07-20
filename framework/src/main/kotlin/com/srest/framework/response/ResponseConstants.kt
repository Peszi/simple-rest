package com.srest.framework.response

internal object ResponseConstants {

    const val SERVER_NAME = "CheapSpring/0.0.1"

}

object ContentType {

    const val APP_JSON = "application/json" // application/json;charset=UTF-8
    const val APP_OCTET_STREAM_TYPE = "application/octet-stream"
    const val APP_JS = "application/javascript"
    const val TEXT_HTML = "text/html"
    const val TEXT_CSS = "text/css"

    fun getTypeForExtension(extension: String) = when(extension) {
        "html"  -> TEXT_HTML
        "css"   -> TEXT_CSS
        "js"    -> APP_JS
        "json"  -> APP_JSON
        else    -> TEXT_HTML
    }
}