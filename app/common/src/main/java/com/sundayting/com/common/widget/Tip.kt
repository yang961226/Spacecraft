package com.sundayting.com.common.widget

import java.util.*

data class Tip(
    val tip: String,
    val uuid: String = UUID.randomUUID().toString(),
)