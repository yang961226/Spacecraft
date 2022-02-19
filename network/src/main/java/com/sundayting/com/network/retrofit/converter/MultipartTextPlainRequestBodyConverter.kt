package com.mastercom.mtcorex.network.retrofit.converter

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Converter

class MultipartTextPlainRequestBodyConverter : Converter<Any, RequestBody> {

    companion object {
        const val CONTENT_TYPE = "text/plain"
    }

    override fun convert(value: Any): RequestBody {
        return value.toString().toRequestBody(CONTENT_TYPE.toMediaType())
    }

}