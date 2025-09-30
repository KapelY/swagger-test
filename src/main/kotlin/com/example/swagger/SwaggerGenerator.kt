package com.example.swagger

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.PathItem
import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.responses.ApiResponse
import io.swagger.v3.oas.models.responses.ApiResponses
import io.swagger.v3.oas.models.parameters.Parameter
import io.swagger.v3.oas.models.parameters.RequestBody
import io.swagger.v3.oas.models.media.Content
import io.swagger.v3.oas.models.media.MediaType
import io.swagger.v3.oas.models.media.Schema
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.Components
import org.springframework.stereotype.Component


@Component
class SwaggerGenerator {

    fun generateOpenAPI(): OpenAPI {
        val openAPI = OpenAPI()
            .info(
                Info()
                    .title("Spring Core + Spark API")
                    .version("1.0.0")
                    .description("REST API using Spring Core for DI and Spark for HTTP endpoints")
                    .contact(
                        Contact()
                            .name("API Team")
                            .email("team@example.com")
                    )
            )
            .components(
                Components()
                    .addSchemas("User", Schema<Any>()
                        .type("object")
                        .addProperty("id", Schema<Any>().type("integer").format("int64").description("User ID"))
                        .addProperty("name", Schema<Any>().type("string").description("User name"))
                        .addProperty("email", Schema<Any>().type("string").format("email").description("User email"))
                    )
                    .addSchemas("UserRequest", Schema<Any>()
                        .type("object")
                        .addProperty("name", Schema<Any>().type("string").description("User name"))
                        .addProperty("email", Schema<Any>().type("string").format("email").description("User email"))
                        .required(listOf("name", "email"))
                    )
                    .addSchemas("ErrorResponse", Schema<Any>()
                        .type("object")
                        .addProperty("error", Schema<Any>().type("string").description("Error message"))
                    )
            )

        // GET /api/users
        openAPI.path("/api/users", PathItem()
            .get(Operation()
                .summary("Get all users")
                .description("Retrieve a list of all users")
                .operationId("getAllUsers")
                .tags(listOf("Users"))
                .responses(ApiResponses()
                    .addApiResponse("200", ApiResponse()
                        .description("Successfully retrieved list of users")
                        .content(Content().addMediaType("application/json",
                            MediaType().schema(Schema<Any>()
                                .type("array")
                                .items(Schema<Any>().`$ref`("#/components/schemas/User"))
                            )
                        ))
                    )
                    .addApiResponse("500", ApiResponse()
                        .description("Internal server error")
                        .content(Content().addMediaType("application/json",
                            MediaType().schema(Schema<Any>().`$ref`("#/components/schemas/ErrorResponse"))
                        ))
                    )
                )
            )
            .post(Operation()
                .summary("Create a new user")
                .description("Create a new user with the provided information")
                .operationId("createUser")
                .tags(listOf("Users"))
                .requestBody(RequestBody()
                    .required(true)
                    .description("User object to be created")
                    .content(Content().addMediaType("application/json",
                        MediaType().schema(Schema<Any>().`$ref`("#/components/schemas/UserRequest"))
                    ))
                )
                .responses(ApiResponses()
                    .addApiResponse("201", ApiResponse()
                        .description("User created successfully")
                        .content(Content().addMediaType("application/json",
                            MediaType().schema(Schema<Any>().`$ref`("#/components/schemas/User"))
                        ))
                    )
                    .addApiResponse("400", ApiResponse()
                        .description("Invalid input")
                        .content(Content().addMediaType("application/json",
                            MediaType().schema(Schema<Any>().`$ref`("#/components/schemas/ErrorResponse"))
                        ))
                    )
                )
            )
        )

        // GET, PUT, DELETE /api/users/{id}
        openAPI.path("/api/users/{id}", PathItem()
            .get(Operation()
                .summary("Get user by ID")
                .description("Retrieve a single user by their ID")
                .operationId("getUserById")
                .tags(listOf("Users"))
                .addParametersItem(Parameter()
                    .name("id")
                    .`in`("path")
                    .required(true)
                    .description("User ID")
                    .schema(Schema<Any>().type("integer").format("int64"))
                )
                .responses(ApiResponses()
                    .addApiResponse("200", ApiResponse()
                        .description("User found")
                        .content(Content().addMediaType("application/json",
                            MediaType().schema(Schema<Any>().`$ref`("#/components/schemas/User"))
                        ))
                    )
                    .addApiResponse("404", ApiResponse()
                        .description("User not found")
                        .content(Content().addMediaType("application/json",
                            MediaType().schema(Schema<Any>().`$ref`("#/components/schemas/ErrorResponse"))
                        ))
                    )
                )
            )
            .put(Operation()
                .summary("Update user")
                .description("Update an existing user's information")
                .operationId("updateUser")
                .tags(listOf("Users"))
                .addParametersItem(Parameter()
                    .name("id")
                    .`in`("path")
                    .required(true)
                    .description("User ID")
                    .schema(Schema<Any>().type("integer").format("int64"))
                )
                .requestBody(RequestBody()
                    .required(true)
                    .description("Updated user information")
                    .content(Content().addMediaType("application/json",
                        MediaType().schema(Schema<Any>().`$ref`("#/components/schemas/UserRequest"))
                    ))
                )
                .responses(ApiResponses()
                    .addApiResponse("200", ApiResponse()
                        .description("User updated successfully")
                        .content(Content().addMediaType("application/json",
                            MediaType().schema(Schema<Any>().`$ref`("#/components/schemas/User"))
                        ))
                    )
                    .addApiResponse("404", ApiResponse()
                        .description("User not found")
                        .content(Content().addMediaType("application/json",
                            MediaType().schema(Schema<Any>().`$ref`("#/components/schemas/ErrorResponse"))
                        ))
                    )
                    .addApiResponse("400", ApiResponse()
                        .description("Invalid input")
                        .content(Content().addMediaType("application/json",
                            MediaType().schema(Schema<Any>().`$ref`("#/components/schemas/ErrorResponse"))
                        ))
                    )
                )
            )
            .delete(Operation()
                .summary("Delete user")
                .description("Delete a user by their ID")
                .operationId("deleteUser")
                .tags(listOf("Users"))
                .addParametersItem(Parameter()
                    .name("id")
                    .`in`("path")
                    .required(true)
                    .description("User ID")
                    .schema(Schema<Any>().type("integer").format("int64"))
                )
                .responses(ApiResponses()
                    .addApiResponse("204", ApiResponse().description("User deleted successfully"))
                    .addApiResponse("404", ApiResponse()
                        .description("User not found")
                        .content(Content().addMediaType("application/json",
                            MediaType().schema(Schema<Any>().`$ref`("#/components/schemas/ErrorResponse"))
                        ))
                    )
                )
            )
        )

        return openAPI
    }
}