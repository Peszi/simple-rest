package com.srest.framework.util

object PageData {

    const val PAGE_NOT_FOUND =
           "<!DOCTYPE html>\n" +
                   "<html lang=\"en\" style=\"height: 100%; display: flex; align-items: center; justify-content: center\">\n" +
                   "<head>\n" +
                   "    <meta charset=\"UTF-8\">\n" +
                   "    <title>Not found</title>\n" +
                   "</head>\n" +
                   "<body>\n" +
                   "    <div>\n" +
                   "        <p style=\"margin: 0; font-weight: bold; font-size: 3.6rem\">404</p>\n" +
                   "        <p style=\"margin: 0; font-size: 1.2rem; text-align: center\">not found!</p>\n" +
                   "    </div>\n" +
                   "</body>\n" +
                   "</html>"

    const val COMPONENT_NOT_FOUND =
            "<p>404 component not found!</p>"

    fun getEmptyComponent(component: String) = "<div class=\"rounded border border-danger py-1\" style=\"border-style: dashed !important\"><p class=\"m-0 text-center\">'$component' not exists!</p></div>"

}