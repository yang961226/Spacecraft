package com.sundayting.com.core.widget

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 管理APP专用的（app-specific files） 内部存储空间目录 和 外部存储空间目录下的文件，读写均不需要权限，详情可以参考https://developer.android.google.cn/training/data-storage/app-specific#external
 */

object AppSpecificFileManager {

    /**
     * 应用专属存储空间访问器
     */
    internal interface AppSpecificFileAccessor {

        /**
         * 打开文件
         * @param fileName String 文件名
         */
        fun openFile(fileName: String): File {
            return File(dir(), fileName)
        }

        /**
         * 打开缓存文件
         * @param fileName String 文件名
         */
        fun openCacheFile(fileName: String): File {
            return File(cacheDir(), fileName)
        }

        /**
         * 存储的目录
         * @return File 目录
         */
        fun dir(): File

        /**
         * 缓存的目录
         * @return File 目录
         */
        fun cacheDir(): File

        /**
         * 创建缓存文件
         * @param fileName String 文件名
         * @param suffix String? 后缀，可选
         * @return File 新创建的缓存文件
         */
        fun createTempFile(fileName: String, suffix: String? = null): File


    }

    /**
     * 应用专用（内部）存储空间访问器
     */
    @Singleton
    class InternalAccessor @Inject constructor(
        @ApplicationContext private val context: Context
    ) : AppSpecificFileAccessor {

        override fun dir(): File = context.filesDir

        override fun cacheDir(): File = context.cacheDir

        override fun createTempFile(fileName: String, suffix: String?): File {
            return File.createTempFile(fileName, suffix, context.cacheDir)
        }

        /**
         * 获取包含 filesDir 目录中所有文件名称的数组
         * @return Array<String> 文件名称的数组
         */
        fun fileList(): Array<String> {
            return context.fileList()
        }
    }

    /**
     * 应用专用（外部）存储空间访问器
     *
     */
    @Singleton
    class ExternalAccessor @Inject constructor(
        @ApplicationContext private val context: Context
    ) : AppSpecificFileAccessor {

        override fun dir(): File = context.getExternalFilesDir(null)!!

        override fun cacheDir(): File {
            val dir = context.getExternalFilesDir(null)
            checkNotNull(dir) {
                "当前设备无外部存储（SD卡）"
            }
            return dir
        }

        override fun createTempFile(fileName: String, suffix: String?): File {
            val dir = context.externalCacheDir
            checkNotNull(dir) {
                "当前设备无外部存储（SD卡）"
            }
            return File(
                dir, if (suffix != null) "$fileName.$suffix" else fileName
            )
        }

    }

}