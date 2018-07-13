package com.srest.framework.response

import com.srest.framework.request.HttpMethod

class ResponseParams(
        var method: HttpMethod,
        var contentType: String
)