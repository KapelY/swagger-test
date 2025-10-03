import com.google.gson.Gson
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.servers.Server
import io.swagger.v3.parser.OpenAPIV3Parser
import org.springframework.stereotype.Component
import spark.Spark

@Component
class SwaggerGenerator() {

    private var cachedOpenAPI: OpenAPI? = null

    fun generateOpenAPI(): OpenAPI {
        if (cachedOpenAPI != null) {
            return cachedOpenAPI!!
        }

        val resourceStream = javaClass.classLoader.getResourceAsStream("swagger-config.yaml")
            ?: throw IllegalStateException("swagger-config.yaml not found in resources")

        val content = resourceStream.bufferedReader().use { it.readText() }
        val parseResult = OpenAPIV3Parser().readContents(content, null, null)

        cachedOpenAPI = parseResult.openAPI
            ?: throw IllegalStateException("Failed to parse OpenAPI specification")

        return cachedOpenAPI!!
    }

    fun generateOpenAPIWithDynamicServer(host: String): OpenAPI {
        val openAPI = generateOpenAPI()
        openAPI.servers = listOf(
            Server().url(host).description("Current server")
        )

        return openAPI
    }
}