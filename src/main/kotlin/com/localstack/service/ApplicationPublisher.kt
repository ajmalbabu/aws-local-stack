package com.localstack.service

import com.localstack.config.AWSConfig
import com.localstack.model.AppMessageResponse
import com.localstack.model.Application
import com.localstack.model.ApplicationStatus
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.sqs.SqsClient
import software.amazon.awssdk.services.sqs.model.SendMessageRequest

@Service
class ApplicationPublisher(val sqsClient: SqsClient, val objectMapper: ObjectMapper, val awsConfig: AWSConfig) {
    fun create(application: Application): AppMessageResponse {
        sqsClient.sendMessage(SendMessageRequest.builder()
                .queueUrl(awsConfig.applicationSqsUrl)
                .messageBody(objectMapper.writeValueAsString(application))
                .delaySeconds(10)
                .build());
        return AppMessageResponse("Successfully received application. Work in progress", ApplicationStatus.RECEIVED)
    }
}
