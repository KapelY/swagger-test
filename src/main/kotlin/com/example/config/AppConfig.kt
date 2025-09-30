package com.example.config

import com.google.gson.Gson
import com.google.gson.GsonBuilder
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
}