# local-stack application

This Github lets one build application that connects to various AWS services using [LocalStack](https://github.com/localstack/localstack). With LocalStack there is no need to have AWS connectivity, all AWS operations are available locally without even having internet or network or wifi connection.
* This spring-boot based Kotlin application provides a REST end-point to user to submit a loan application, 
* Upon receiving the application, it puts it into an SQS queue for processing
* A listener listens for the application message in JSON format and writes it into Dynamo DB table
* Also listener writes it into and S3 bucket.

All these operations are performed without having an internet and AWS connectivity, purely in Offline mode.  

# Setup

Download LocalStack docker image, the best way is through pip/pip3 install, once installed go to a terminal to current dir of this project and perform below command to bring local stack up.

docker-compose up

Now create following AWS resources needed for this example application to run: Create SQS queue, Dynamo table and S3 bucket on LocalStack using below commands.

```
# create a SQS queue
aws --endpoint-url=http://localhost:4576 sqs create-queue --queue-name application_queue

# Create DynamoDB tables

aws --endpoint-url=http://localhost:4569 dynamodb create-table --table-name applications --attribute-definitions AttributeName=appId,AttributeType=S --key-schema AttributeName=appId,KeyType=HASH --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5

# Create S3 bucket to store images
aws --endpoint-url=http://localhost:4572 s3api create-bucket --bucket application


```

Run LocalStackMain.kt from the spring-boot application:  Before running, provide following parameters in the 'Environment Variables' section AWS_ACCESS_KEY_ID=123;AWS_SECRET_ACCESS_KEY=123

Bring up swagger ui on browser with http://localhost:8080/swagger-ui.html

Go to application controller post method and make a POST by pressing execute button with below application content

```
{
  "id": "string",
  "status": "RECEIVED",
  "applicant": {
    "firstName": "string",
    "lastName": "string",
    "ssn": "string",
    "payStubImage": "http://localhost:8080/a1.jpg"
  },
  "coApplicant": {
    "firstName": "string",
    "lastName": "string",
    "ssn": "string",
    "payStubImage": "http://localhost:8080/a2.jpg"
  }
}
```

Execute below to view up-loaded S3 document names once application process the above message, wait a minute to complete processing of message
---
```
aws --endpoint-url=http://localhost:4572 s3 ls s3://application
``` 

# Appendix:
Below are set of handy SQS, DynamoDB and S3 commands not needed for this application

```
# list a queue
aws --endpoint-url=http://localhost:4576 sqs list-queues
# list the queue attributes
aws --endpoint-url=http://localhost:4576 sqs get-queue-attributes --queue-url http://localhost:4576/queue/application_queue --attribute-names All
aws --endpoint-url=http://localhost:4576 sqs send-message --queue-url app.received --message-body "{\"appId\": 123,\"firstName\": \"Brian\",\"lastName\": \"Yeh\"}"

aws --endpoint-url=http://localhost:4569 dynamodb put-item --table-name applications --item file://item.json --return-consumed-capacity TOTAL
aws --endpoint-url=http://localhost:4569 dynamodb get-item --table-name applications --key file://key.json
aws --endpoint-url=http://localhost:4569 dynamodb delete-table --table-name applications

```
