package com.mastercom.mtcorex.network.retrofit.converter

import okhttp3.RequestBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.http.Part
import java.lang.reflect.Type

/**
 * 将[RequestBody]带有[Part]注解的参数中的字符串，数字，布尔值转换成text/plain类型
 */
class MultipartTextPlainConverterFactory : Converter.Factory() {

    companion object {
        fun create(): MultipartTextPlainConverterFactory {
            return MultipartTextPlainConverterFactory()
        }
    }

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<out Annotation>,
        methodAnnotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody>? {
        if (parameterAnnotations.find { it is Part } != null && isToTextPlainType(type)) {
            return MultipartTextPlainRequestBodyConverter()
        }
        return null
    }

    private fun isToTextPlainType(type: Type): Boolean {
        val clazz = type as? Class<*> ?: return false
        clazz.run {
            if (equals(String::class.java) || equals(Boolean::class.java) || isNumberType(clazz)) {
                return true
            }
        }
        return false
    }

    // TODO: 2021/12/23 模板代码，思考如何废除 
    private fun isNumberType(clazz: Class<*>): Boolean {
        return clazz.run {
            equals(Int::class.java)
                    || equals(Double::class.java)
                    || equals(Float::class.java)
                    || equals(Long::class.java)
                    || equals(Short::class.java)
        }
    }

}