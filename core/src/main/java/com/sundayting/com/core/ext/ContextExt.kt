package com.sundayting.com.core.ext

import android.content.Context
import java.io.File
import java.io.FileOutputStream

/**
 * 打开filesDir目录下的文件
 * @param fileName String 文件名
 * @return File 打开的文件
 */
fun Context.openFile(fileName: String): File {
    return File(filesDir, fileName)
}

/**
 * 获取filesDir目录下的文件流（用于写入）
 * @param fileName String 文件名
 * @return FileOutputStream 用于写入的文件流
 * @see [Context.openFileInput]
 */
fun Context.openFileOutput(fileName: String): FileOutputStream {
    //在搭载 Android 7.0（API 级别 24）或更高版本的设备上，除非您将 Context.MODE_PRIVATE 文件模式传递到 openFileOutput()，否则会发生 SecurityException。
    return openFileOutput(fileName, Context.MODE_PRIVATE)
}

/**
 * 创建嵌套目录或打开内部目录，设计同名扩展方法的原因可以参考[openFileOutput]
 * @param dirName String 目录名
 * @return File 打开的目录
 */
fun Context.getDir(dirName: String): File {
    return getDir(dirName, Context.MODE_PRIVATE)
}

/**
 * 打开外部存储空间中的缓存文件
 * @param fileName String 文件名
 */
fun Context.openExternalCacheFile(fileName: String): File {
    return File(externalCacheDir, fileName)
}