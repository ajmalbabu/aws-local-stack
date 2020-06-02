package com.localstack.service

import com.localstack.config.AWSConfig
import com.localstack.model.Applicant
import com.localstack.model.Application
import org.apache.commons.io.IOUtils
import org.springframework.stereotype.Service
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectResponse
import java.net.URI
import java.net.URL
import java.nio.ByteBuffer


@Service
class FileUploader(private val s3Client: S3Client, private val awsConfig: AWSConfig) {
    fun uploadFile(app: Application) {
        if (app.applicant.payStubImage.isNotEmpty()) {
            var response1 = upload(app.applicant.payStubImage,  "${app.id}-applicant")
        }
        if (app.coApplicant.payStubImage.isNotEmpty()) {
           var response2 = upload(app.coApplicant.payStubImage, "${app.id}-coapplicant")
        }

    }

    private fun upload(url: String, type: String): PutObjectResponse {
        val byteArrayImage = IOUtils.toByteArray(URI(url))
        val key = awsConfig.fileObjectKeyPrefix + type
        return s3Client.putObject(PutObjectRequest.builder().bucket(awsConfig.bucketName).key(key).metadata(mapOf("id" to type)).build(),
                RequestBody.fromByteBuffer(ByteBuffer.wrap(byteArrayImage)))
    }
}
