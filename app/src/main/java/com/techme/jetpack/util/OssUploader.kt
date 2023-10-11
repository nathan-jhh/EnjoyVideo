package com.techme.jetpack.util

import android.util.Log
import com.alibaba.sdk.android.oss.ClientConfiguration
import com.alibaba.sdk.android.oss.OSSClient
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback
import com.alibaba.sdk.android.oss.common.OSSLog
import com.alibaba.sdk.android.oss.common.auth.OSSAuthCredentialsProvider
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider
import com.alibaba.sdk.android.oss.model.PutObjectRequest

object OssUploader {
    private const val TAG = "OssUploader"
    private var oss: OSSClient
    private const val ALIYUN_BUCKET_URL =
        "https://pipijoke.oss-cn-hangzhou.aliyuncs.com/"
    private const val BUCKET_NAME = "pipijoke"
    private const val END_POINT = "http://oss-cn-hangzhou.aliyuncs.com"
    private const val AUTH_SERVER_URL = "http://123.56.232.18:7080/"

    init {
        val credentialProvider: OSSCredentialProvider = OSSAuthCredentialsProvider(
            AUTH_SERVER_URL
        )

        //该配置类如果不设置，会有默认配置，具体可看该类
        val conf = ClientConfiguration()
        conf.connectionTimeout = 15 * 1000//连接超时，默认15秒
        conf.socketTimeout = 15 * 1000 // socket超时，默认15秒
        conf.maxConcurrentRequest = 5//最大并发请求数，默认5个
        conf.maxErrorRetry = 2// 失败后最大重试次数，默认2次
        OSSLog.disableLog()//这个开启会支持写入手机sd卡中的一份日志文件位置在SDCard_path\0SSLog\logs.csv
        oss = OSSClient(
            AppGlobals.getApplication(), END_POINT, credentialProvider, conf
        )
    }

    fun upload(bytes: ByteArray?): String {
        val objectKey = System.currentTimeMillis().toString()
        val request = PutObjectRequest(BUCKET_NAME, objectKey, bytes)
        return upload(request)
    }

    fun upload(filePath: String): String {
        val objectKey = filePath.substring(
            filePath.lastIndexOf("/") + 1
        )
        val request = PutObjectRequest(BUCKET_NAME, objectKey, filePath)
        return upload(request)
    }

    private fun upload(putRequest: PutObjectRequest): String {
        putRequest.progressCallback = OSSProgressCallback { _, currentSize, totalSize ->
            Log.e(TAG, "upload currentSize: $currentSize totalSize: $totalSize")
        }
        val result = oss.putObject(putRequest)
        return if (result.statusCode == 200) {
            ALIYUN_BUCKET_URL + putRequest.objectKey
        } else {
            println(TAG + result.serverCallbackReturnBody)
            ""
        }
    }
}
