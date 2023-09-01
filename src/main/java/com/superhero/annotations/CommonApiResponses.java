package com.superhero.annotations;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ApiResponse(responseCode = "400", description = "Some parameters are invalid",
    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string")))
@ApiResponse(responseCode = "401", description = "Invalid or empty token",
    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string")))
@ApiResponse(responseCode = "403", description = "Access denied",
    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string")))
@ApiResponse(responseCode = "404", description = "Super heroes not found",
    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string")))
@ApiResponse(responseCode = "500", description = "Internal Server Error",
    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string")))
public @interface CommonApiResponses {

}
