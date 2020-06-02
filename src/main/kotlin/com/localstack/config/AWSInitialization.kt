package com.localstack.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.sqs.SqsClient

@Configuration
@Profile("!localDev")
class AWSInitialization(private val config: AWSConfig) {

    @Bean
    fun s3Client(): S3Client {
        return S3Client.builder()
                .region(Region.of(config.region)).build()
    }

    @Bean
    fun dynamoDbClient(): DynamoDbClient {
        return DynamoDbClient.builder()
                .region(Region.of(config.region)).build()
    }

    @Bean
    fun sqsClient(): SqsClient {
        return SqsClient.builder()
                .region(Region.of(config.region)).build()
    }
}
