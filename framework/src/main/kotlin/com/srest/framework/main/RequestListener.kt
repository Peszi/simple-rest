package com.srest.framework.main

import com.srest.framework.request.Request
import com.srest.framework.response.Response

internal interface RequestListener {
    fun onResponse(request: Request): Response
}