package com.sundayting.com.common.web

import java.io.Serializable

data class WebViewBean(
    val loadUrl: String,
    val title: String,
) : Serializable