package com.localstack.controller

import com.localstack.model.AppMessageResponse
import com.localstack.model.Application
import com.localstack.service.ApplicationPublisher
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController("/application")
class ApplicationController(val applicationPublisher: ApplicationPublisher) {
    @PostMapping
    fun create(@RequestBody application: Application): AppMessageResponse {
        return applicationPublisher.create(application)
    }
}
