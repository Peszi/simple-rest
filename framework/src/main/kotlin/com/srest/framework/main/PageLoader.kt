package com.srest.framework.main

import com.srest.framework.bean.InternalPageFilesService
import com.srest.framework.request.Request
import com.srest.framework.response.ResponseParams

internal object PageLoader {



    private val loadMethod = InternalPageFilesService::class.java.getDeclaredMethod("getInternalFileResponse", Request::class.javaObjectType, ResponseParams::class.javaObjectType)

    init {
        if (loadMethod == null) throw RuntimeException("Cannot get InternalPageFilesService method!")
    }



}