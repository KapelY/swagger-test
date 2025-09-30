package com.example

import com.example.config.AppConfig
import com.example.routes.SparkRoutes
import org.springframework.context.annotation.AnnotationConfigApplicationContext

fun main() {
    // Initialize Spring context
    val context = AnnotationConfigApplicationContext(AppConfig::class.java)

    // Get SparkRoutes bean and initialize
    val sparkRoutes = context.getBean(SparkRoutes::class.java)
    sparkRoutes.initialize()

    println("Application started!")
    println("API endpoints: http://localhost:4567/api/users")
    println("Swagger UI: http://localhost:4567/swagger-ui")
    println("OpenAPI spec: http://localhost:4567/api-docs")
}