package com.example.routes
import com.example.model.UserRequest
import com.example.service.UserService
import com.example.swagger.SwaggerGenerator
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import org.springframework.stereotype.Component
import spark.Spark.*

@Component
class SparkRoutes(
    private val userService: UserService,
    private val swaggerGenerator: SwaggerGenerator,
    private val gson: Gson,
    private val objectMapper: ObjectMapper
) {

    fun initialize() {
        port(4567)

        // CORS
        before({ _, res ->
            res.header("Access-Control-Allow-Origin", "*")
            res.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
            res.header("Access-Control-Allow-Headers", "Content-Type")
            res.type("application/json")
        })

        options("/*", { _, _ -> "OK" })

        // Swagger documentation endpoints
        get("/api-docs", { _, res ->
            try {
                res.type("application/json")
                val openAPI = swaggerGenerator.generateOpenAPI()
                // Use Jackson ObjectMapper from swagger to serialize OpenAPI object
                objectMapper.writeValueAsString(openAPI)
            } catch (e: Exception) {
                println("ERROR in /api-docs endpoint: ${e.message}")
                e.printStackTrace()
                res.status(500)
                res.type("application/json")
                gson.toJson(mapOf("error" to "Failed to load OpenAPI specification: ${e.message}"))
            }
        })

        get("/swagger-ui", { _, res ->
            res.type("text/html")
            swaggerUI()
        })

        // User API endpoints
        get("/api/users", { _, res ->
            try {
                val users = userService.getAllUsers()
                gson.toJson(users)
            } catch (e: Exception) {
                res.status(500)
                gson.toJson(mapOf("error" to e.message))
            }
        })

        get("/api/users/:id", { req, res ->
            try {
                val id = req.params(":id").toLong()
                val user = userService.getUserById(id)
                gson.toJson(user)
            } catch (e: NoSuchElementException) {
                res.status(404)
                gson.toJson(mapOf("error" to e.message))
            } catch (e: Exception) {
                res.status(500)
                gson.toJson(mapOf("error" to e.message))
            }
        })

        post("/api/users", { req, res ->
            try {
                val userRequest = gson.fromJson(req.body(), UserRequest::class.java)
                val user = userService.createUser(userRequest)
                res.status(201)
                gson.toJson(user)
            } catch (e: Exception) {
                res.status(400)
                gson.toJson(mapOf("error" to (e.message ?: "Invalid request")))
            }
        })

        put("/api/users/:id", { req, res ->
            try {
                val id = req.params(":id").toLong()
                val userRequest = gson.fromJson(req.body(), UserRequest::class.java)
                val user = userService.updateUser(id, userRequest)
                gson.toJson(user)
            } catch (e: NoSuchElementException) {
                res.status(404)
                gson.toJson(mapOf("error" to e.message))
            } catch (e: Exception) {
                res.status(400)
                gson.toJson(mapOf("error" to e.message))
            }
        })

        delete("/api/users/:id", { req, res ->
            try {
                val id = req.params(":id").toLong()
                userService.deleteUser(id)
                res.status(204)
                ""
            } catch (e: NoSuchElementException) {
                res.status(404)
                gson.toJson(mapOf("error" to e.message))
            } catch (e: Exception) {
                res.status(500)
                gson.toJson(mapOf("error" to e.message))
            }
        })
    }

    private fun swaggerUI(): String {
        return """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <title>Swagger UI</title>
                <link rel="stylesheet" href="https://unpkg.com/swagger-ui-dist@5.9.0/swagger-ui.css">
            </head>
            <body>
                <div id="swagger-ui"></div>
                <script src="https://unpkg.com/swagger-ui-dist@5.9.0/swagger-ui-bundle.js"></script>
                <script>
                    window.onload = function() {
                        SwaggerUIBundle({
                            url: "/api-docs",
                            dom_id: '#swagger-ui',
                            presets: [
                                SwaggerUIBundle.presets.apis,
                                SwaggerUIBundle.SwaggerUIStandalonePreset
                            ]
                        });
                    }
                </script>
            </body>
            </html>
        """.trimIndent()
    }
}