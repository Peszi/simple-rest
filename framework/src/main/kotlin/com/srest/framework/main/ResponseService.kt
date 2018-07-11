package com.srest.framework.main

import com.srest.framework.request.Request
import com.srest.framework.response.ContentType
import com.srest.framework.response.Response
import com.srest.framework.util.Logger
import com.srest.framework.util.Pages
import com.srest.framework.util.UrlMapper
import java.lang.reflect.Method

object ResponseService {

    fun getResponseResult(request: Request): Response {
        val endpoint = request.endpoint.getEndpoint()
        // Perfect match
        val handler = controllerHandlers[endpoint] // TODO
        if (handler != null && handler.httpMethod == request.method) {
            val bean = controllerBeans[handler.bean]
            if (bean != null) return invoke(bean, handler.method)
        }
        return Response(ContentType.HTML_TYPE, Pages.NOT_FOUND)
    }

    fun invoke(bean: Any, method: Method): Response {
        val result = method.invoke(bean) // TODO args
        if (result is String) return Response(ContentType.HTML_TYPE, result)
        // map object to Json
        return Response(ContentType.JSON_TYPE, "{ \"error\": \"json not supported\" }")
    }

}