package com.sundayting.com.common.widget

import java.util.*

data class InstanceMessage(
    val message: String,
    val id: String = UUID.randomUUID().toString()
)