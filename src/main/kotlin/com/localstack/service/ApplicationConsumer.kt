package com.localstack.service

import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.model.Message
import com.localstack.config.AWSConfig
import com.localstack.model.Application
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest
import java.util.*
import javax.annotation.PostConstruct


@Service
internal class ApplicationConsumer(private val sqs: AmazonSQS,
                                   private val awsConfig: AWSConfig,
                                   private val objectMapper: ObjectMapper,
                                   private val fileUploader: FileUploader,
                                   private val dynamo: DynamoDbClient) {


    @PostConstruct
    fun consumeApplicationMessages() {

        readApplicationMessages();
    }

    fun readApplicationMessages() {

        val repeatedTask: TimerTask = object : TimerTask() {
            override fun run() {
                val messages: MutableList<Message>? = sqs.receiveMessage(awsConfig.applicationSqsUrl).getMessages()
                messages?.forEach {
                    val app: Application = objectMapper.readValue(it.body)
                    processApplication(app)
                    sqs.deleteMessage(awsConfig.applicationSqsUrl, it.getReceiptHandle());
                }
            }
        }
        val timer = Timer("Timer")

        val delay = 1000L
        val period = 1000L
        timer.scheduleAtFixedRate(repeatedTask, delay, period)

    }
    fun processApplication(app: Application) {
        val item_values = HashMap<String, AttributeValue>()

        item_values["application"] = AttributeValue.builder().s(objectMapper.writeValueAsString(app)).build()
        item_values["appId"] =  AttributeValue.builder().s(app.id).build()

        val request = PutItemRequest.builder()
                .tableName(awsConfig.table)
                .item(item_values)
                .build()
        dynamo.putItem(request)
        fileUploader.uploadFile(app)
        println(app.id + " written to dynamo successfully!")
    }
}
