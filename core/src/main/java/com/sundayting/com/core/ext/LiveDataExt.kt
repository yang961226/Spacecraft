package com.sundayting.com.core.ext

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * 转变成不可变LiveData
 */
fun <T> MutableLiveData<T>.immutable(): LiveData<T> {
    return this
}