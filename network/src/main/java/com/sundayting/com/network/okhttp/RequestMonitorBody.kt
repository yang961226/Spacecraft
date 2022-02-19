package com.sundayting.com.network.okhttp

import okhttp3.RequestBody
import okio.*

/**
 * 上传进度监听器
 */
internal class RequestMonitorBody internal constructor(
    private val delegate: RequestBody,
    private val updateProgress: (hasWrittenLen: Long, totalLen: Long) -> Unit
) : RequestBody() {

    @Throws(IOException::class)
    override fun contentLength() = delegate.contentLength()

    override fun contentType() = delegate.contentType()

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        val countingSink = CountingSink(sink).buffer()
        delegate.writeTo(countingSink)
        countingSink.flush()
    }

    inner class CountingSink(
        delegate: Sink
    ) : ForwardingSink(delegate) {

        //当前写入的字节数
        private var bytesWritten = 0L

        @Throws(IOException::class)
        override fun write(source: Buffer, byteCount: Long) {
            bytesWritten += byteCount
            //刷新进度
            updateProgress(bytesWritten, contentLength())
            super.write(source, byteCount)
        }
    }
}
