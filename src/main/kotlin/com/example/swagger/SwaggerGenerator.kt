package com.example.swagger

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.parser.OpenAPIV3Parser
import org.springframework.stereotype.Component
import java.io.InputStreamReader

@Component
class SwaggerGenerator() {

    private var cachedOpenAPI: OpenAPI? = null

    fun generateOpenAPI(): OpenAPI {
        cachedOpenAPI?.let { return it }

        val resourceStream = javaClass.classLoader.getResourceAsStream("swagger-config.yaml")
            ?: throw IllegalStateException("swagger-config.yaml not found in resources")

        val content = InputStreamReader(resourceStream).use { it.readText() }
        val parseResult = OpenAPIV3Parser().readContents(content, null, null)

        cachedOpenAPI = parseResult.openAPI
            ?: throw IllegalStateException("Failed to parse OpenAPI specification")

        return cachedOpenAPI!!
    }
}