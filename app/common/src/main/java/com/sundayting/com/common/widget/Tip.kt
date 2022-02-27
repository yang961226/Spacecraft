package com.sundayting.com.common.widget

import java.util.*

data class Tip(
    val content: String,
    val uuid: String = UUID.randomUUID().toString(),
)