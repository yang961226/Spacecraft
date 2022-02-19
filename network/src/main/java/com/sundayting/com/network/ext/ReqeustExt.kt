package com.sundayting.com.network.ext

import okhttp3.Request
import okhttp3.RequestBody
import okio.Buffer
import retrofit2.Invocation
import retrofit2.http.GET
import retrofit2.http.POST
import java.io.IOException

internal object Handled

/**
 * 校验当前的请求是否被其他加解密拦截器处理了
 * @return Boolean 返回true说明被处理了，反之则没被处理
 */
internal fun Request.isHandled(): Boolean {
    return tag(Handled::class.java) != null
}

/**
 * 当前的网络请求被加解密拦截器处理后，添加一个标签，防止后续的拦截器继续处理
 */
internal fun Request.Builder.handle() {
    tag(Handled::class.java, Handled)
}

/**
 * 返回某个Retrofit定义在方法上的注解，例如[POST],[GET]
 */
fun <T : Annotation> Request.getMethodAnnotation(annotationClass: Class<T>): T? {
    return tag(Invocation::class.java)?.method()?.getAnnotation(annotationClass)
}

fun <T : Annotation> Request.containMethodAnnotation(annotationClass: Class<T>): Boolean {
    return getMethodAnnotation(annotationClass) != null
}

/**
 * 将请求体的字节流写入字符串
 */
@Throws(IOException::class)
fun RequestBody.writeToString(): String {
    return Buffer().also {
        writeTo(it)
    }.readUtf8()
}
