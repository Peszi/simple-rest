package com.srest.framework.response

class ResponseEntity<T>(
        val responseData: T,
        val responseCode: Int

) {
    companion object {
        fun <T> ok(responseData: T) = ResponseEntity(
                responseData, 200
        )
    }
}