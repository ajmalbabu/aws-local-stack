package com.localstack.model

import java.io.Serializable


data class Application(val id: String, val applicant: Applicant, val coApplicant: Applicant) : Serializable {
    lateinit var status: ApplicationStatus
}
data class Applicant(val firstName: String, val lastName: String, val ssn: String, val payStubImage: String) : Serializable

data class AppMessageResponse(val message: String, val status: ApplicationStatus) : Serializable

enum class ApplicationStatus {
    RECEIVED, PENDING, FUNDING
}
