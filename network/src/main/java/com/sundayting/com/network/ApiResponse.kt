package com.sundayting.com.network

import com.mastercom.mtcorex.network.exception.HttpExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

sealed class ApiResponse<T> {

    companion object {
        fun <T> success(response: Response<T>) = Success(response)
        fun <T> serverError(response: Response<T>) = Failure.ServerError(response)
        fun <T> exception(exception: Throwable): Failure.Exception<T> =
            Failure.Exception(exception)

        inline fun <T> of(
            crossinline block: () -> Response<T>
        ): ApiResponse<T> = try {
            val response = block()
            if (response.isSuccessful) {
                success(response)
            } else {
                serverError(response)
            }
        } catch (t: Throwable) {
            exception(t)
        }
    }


    /**
     * 网络请求成功
     */
    class Success<T>(response: Response<T>) : ApiResponse<T>() {
        val responseBody = response.body()!!
    }

    /**
     * 网络请求失败
     */
    sealed class Failure<T> : ApiResponse<T>() {
        /**
         * HTTP协议错误
         */
        data class ServerError<T>(val response: Response<T>) : Failure<T>() {
            val responseErrorMessage: String = response.errorBody()?.string().orEmpty().run {
                if (isEmpty()) response.message()
                else this
            }
        }

        /**
         * 网络请求出现异常
         */
        data class Exception<T>(val exception: Throwable) : Failure<T>() {
            // TODO: 2021/11/5 后续弄成可配置化
            val exceptionMessage = HttpExceptionHandler.parseExceptionMessage(exception)
        }

    }
}

/**
 * 网络请求成功时操作
 */
inline fun <reified T> ApiResponse<T>.onSuccess(action: (ApiResponse.Success<T>) -> Unit): ApiResponse<T> {
    if (this is ApiResponse.Success) action(this)
    return this
}

/**
 * 网络成功时的操作，支持阻塞+协程上下文切换
 * @param context CoroutineContext action代码库运行的协程上下文
 * @param action Function1<Success<T>, Unit> 成功时操作
 */
suspend inline fun <reified T> ApiResponse<T>.suspendOnSuccess(
    context: CoroutineContext = Dispatchers.Main,
    crossinline action: suspend (ApiResponse.Success<T>) -> Unit
): ApiResponse<T> {
    if (this is ApiResponse.Success) {
        withContext(context) {
            action(this@suspendOnSuccess)
        }
    }
    return this
}

/**
 * 网络请求中，服务器已响应但HTTP协议出现错误时（例如404，502）操作
 */
inline fun <reified T> ApiResponse<T>.onServerError(action: (ApiResponse.Failure.ServerError<T>) -> Unit): ApiResponse<T> {
    if (this is ApiResponse.Failure.ServerError) action(this)
    return this
}

/**
 * 网络请求中，出现异常时（例如解析JSON异常，超时异常，连接网络失败异常等）操作
 */
inline fun <reified T> ApiResponse<T>.onException(action: (ApiResponse.Failure.Exception<T>) -> Unit): ApiResponse<T> {
    if (this is ApiResponse.Failure.Exception) action(this)
    return this
}

/**
 * 网络请求失败（包括[onServerError]和[onException]两种情况）
 */
inline fun <reified T> ApiResponse<T>.onFailure(action: (errorMsg: String?) -> Unit): ApiResponse<T> {
    onServerError {
        action(it.responseErrorMessage)
    }.onException {
        action(it.exceptionMessage)
    }
    return this
}

inline fun <reified T> ApiResponse<T>.onFinish(action: () -> Unit): ApiResponse<T> {
    action()
    return this
}