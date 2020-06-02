package com.localstack.config

import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.AmazonSQSClientBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.sqs.SqsClient
import java.net.URI

@Configuration
@Profile("localDev")
class LocalStackInitialization(private val config: AWSConfig) {

    @Bean
    fun s3Client(): S3Client {
        return S3Client.builder()
                .endpointOverride(URI.create("http://localhost:4572"))
                .region(Region.of(config.region)).build()
    }

    @Bean
    fun dynamoDbClient(): DynamoDbClient {
        return DynamoDbClient.builder()
                .endpointOverride(URI.create("http://localhost:4569"))
                .region(Region.of(config.region)).build()
    }

    @Bean
    fun sqsClient(): SqsClient {
        return SqsClient.builder()
                .endpointOverride(URI.create("http://localhost:4576"))
                .region(Region.of(config.region)).build()
    }

    @Bean
    fun sqsClient1(): AmazonSQS {
        val endpointConfig: AwsClientBuilder.EndpointConfiguration = AwsClientBuilder.EndpointConfiguration("http://localhost:4576", "us-east-1")

        return AmazonSQSClientBuilder.standard().withEndpointConfiguration(endpointConfig).build()
    }

}
