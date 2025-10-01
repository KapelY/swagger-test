package com.example.config

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.swagger.v3.core.util.Json
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(basePackages = ["com.example"])
open class AppConfig {

    @Bean
    open fun gson(): Gson {
        return GsonBuilder()
            .setPrettyPrinting()
            .create()
    }

    @Bean
    open fun objectMapper(): ObjectMapper {
        // Use the pre-configured mapper from Swagger that handles OpenAPI models correctly
        return Json.mapper().apply {
            configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            setSerializationInclusion(JsonInclude.Include.NON_NULL)
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }
}