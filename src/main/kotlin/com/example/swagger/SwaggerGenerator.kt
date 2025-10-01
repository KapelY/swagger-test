package com.example.swagger

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.parser.OpenAPIV3Parser
import org.springframework.stereotype.Component
import java.io.InputStreamReader

@Component
class SwaggerGenerator() {

    private var cachedOpenAPI: OpenAPI? = null

    fun generateOpenAPI(): OpenAPI {
        if (cachedOpenAPI != null) {
            return cachedOpenAPI!!
        }

        try {
            println("Attempting to load swagger-config.yaml from classpath...")

            // Try to load from YAML first
            val resourceStream = javaClass.classLoader.getResourceAsStream("swagger-config.yaml")

            if (resourceStream == null) {
                println("ERROR: swagger-config.yaml not found in classpath!")
                println("Available resources:")
                val resources = javaClass.classLoader.getResources("")
                while (resources.hasMoreElements()) {
                    println("  - ${resources.nextElement()}")
                }
                throw IllegalStateException("swagger-config.yaml not found in resources")
            }

            val content = InputStreamReader(resourceStream).use { it.readText() }
            println("Loaded swagger config, length: ${content.length} bytes")
            println("First 200 chars: ${content.take(200)}")

            val parser = OpenAPIV3Parser()
            val parseResult = parser.readContents(content, null, null)

            if (parseResult.messages != null && parseResult.messages.isNotEmpty()) {
                println("Swagger parsing messages: ${parseResult.messages}")
            }

            if (parseResult.openAPI == null) {
                println("ERROR: Parser returned null OpenAPI object")
                throw IllegalStateException("Failed to parse OpenAPI specification - parser returned null")
            }

            cachedOpenAPI = parseResult.openAPI
            println("Successfully parsed OpenAPI spec")

            return cachedOpenAPI!!
        } catch (e: Exception) {
            println("ERROR loading swagger config: ${e.message}")
            e.printStackTrace()
            throw e
        }
    }
}