package com.waes.assignment.scalableweb.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import java.util.List;

@Value
@Schema(name = "Diff result")
public class DiffResult {

    @Schema(description = "Http status code", required = true, example = "123")
    private final Long diffId;

    @Schema(description = "Http status code", required = true, example = "Left and right are equal")
    private final String message;

    @Schema(description = "Http status code", required = true)
    private final List<String> differences;

}
