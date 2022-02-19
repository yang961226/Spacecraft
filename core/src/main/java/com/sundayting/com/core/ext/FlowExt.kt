package com.sundayting.com.core.ext

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*

/**
 * 转成[SharedFlow]，同时只在有订阅者时发送数据，且不重复之前发过的数据（即一次性，不包含过去状态）
 */
fun <T> Flow<T>.shareWhileObserved(coroutineScope: CoroutineScope) = shareIn(
    scope = coroutineScope,
    started = SharingStarted.WhileSubscribed(),
    replay = 0
)

/**
 * 转变成不可变流
 */
fun <T> MutableStateFlow<T>.immutable(): StateFlow<T> {
    return this
}

/**
 * 转变成不可变流
 */
fun <T> MutableSharedFlow<T>.immutable(): SharedFlow<T> {
    return this
}

