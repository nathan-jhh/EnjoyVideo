package com.techme.jetpack.pages.publish

import android.content.Context
import android.text.TextUtils
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.techme.jetpack.util.OssUploader

class UploadFileWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    override fun doWork(): Result {
        val filePath = inputData.getString("file")
        return if(TextUtils.isEmpty(filePath)){
            return Result.failure()
        }else{
            val fileUrl = OssUploader.upload(filePath!!)
            val outputData = Data.Builder().putString("fileUrl",fileUrl).build()
            Result.success(outputData)
        }
    }
}