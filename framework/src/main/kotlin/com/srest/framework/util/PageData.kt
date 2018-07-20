package com.srest.framework.util

import com.srest.framework.response.ErrorResponse

object PageData {

    fun getErrorPage(error: ErrorResponse) =
            "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <title>error</title>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "\t<div style=\"margin: 2rem\">\n" +
                    "\t\t<p style=\"margin: 0.5rem; font-weight: bold; font-size: 2rem\">Error Page</p>\n" +
                    "\t\t<div style=\"margin: 0.5rem\">\n" +
                    "\t\t\t<p style=\"display: inline; margin: 0; font-weight: bold; font-size: 1.5rem\">${error.code}</p>\n" +
                    "\t\t\t<p style=\"display: inline; margin: 0; font-weight: thin; font-size: 1.5rem;\">${error.error}</p>\n" +
                    "\t\t</div>\n" +
                    "        <p style=\"margin: 0.5rem; font-size: 1rem; \">(${error.message}) path=${error.endpoint}</p>\n" +
                    "    </div>\n" +
                    "</body>\n" +
                    "</html>"

    fun getEmptyComponent(component: String) =
            "<div class=\"rounded border border-danger py-1\" style=\"border-style: dashed !important\">" +
                    "<p class=\"m-0 text-center\">'$component' not exists!</p>" +
                    "</div>"

}