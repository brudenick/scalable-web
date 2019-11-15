package com.waes.assignment.scalableweb.swagger;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.sql.Timestamp;

//Default Spring Boot rest error response
@Schema(name = "Rest error response")
@Getter
public class ErrorResponse {
    @Schema(description = "Response timestamp", required = true, example = "2019-11-15T17:12:12.754+0000")
    private Timestamp timestamp;

    @Schema(description = "Http status code", required = true, example = "400")
    private Integer status;

    @Schema(description = "Error description", required = true, example = "Bad Request")
    private String error;

    @Schema(description = "Exception message", required = true)
    private String message;

    @Schema(description = "Request path", required = true, example = "/v1/diff/1/right")
    private String path;
}