package com.srest.framework.util

object PageData {

    const val PAGE_NOT_FOUND =
            "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <title>NotFound</title>\n" +
                    "</head>\n" +
                    "<p style=\"margin: 24px\">404 not found</p>\n" +
                    "</html>"

    const val COMPONENT_NOT_FOUND =
            "<p>404 component not found!</p>"

    fun getComponentNotFoundPage(component: String): String = "<p>'$component' not found!</p>"

    fun getEmptyComponent(component: String) = "<div class=\"rounded border border-danger py-1\" style=\"border-style: dashed !important\"><p class=\"m-0 text-center\">'$component' not exists!</p></div>"

}