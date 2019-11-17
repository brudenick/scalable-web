package com.waes.assignment.scalableweb.controller;

import com.waes.assignment.scalableweb.controller.validation.Base64EncodedConstraint;
import com.waes.assignment.scalableweb.controller.validation.ValidJsonConstraint;
import com.waes.assignment.scalableweb.exception.DiffException;
import com.waes.assignment.scalableweb.model.DiffPart;
import com.waes.assignment.scalableweb.model.DiffResult;
import com.waes.assignment.scalableweb.service.DiffService;
import com.waes.assignment.scalableweb.swagger.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/diff")
@AllArgsConstructor
@Slf4j
@Tag(name = "Diff Controller", description = "Diff Controller")
@Validated
public class DiffController {

    /**
     * Diff Service singleton bean.
     */
    private final DiffService diffService;

    /**
     * @param diffId      The id of the diff to be created/updated
     * @param requestBody JSON base64 encoded binary data
     * @return HTTP 201 : When diff with given {id} is successfully created/updated
     * HTTP 400 : If request body is empty
     * HTTP 400 : If request body is not base64 encoded
     * HTTP 400 : If request body is not a valid JSON
     * @see
     */
    @Operation(
            summary = "Creates/updates a diff object associated to the given {id} and sets the LEFT part of it with the content of the request body.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Diff with given {id} was successfully created/updated"),
                    @ApiResponse(responseCode = "400", description = "If request body is empty, not base64 encoded or not a valid JSON",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE))
            })
    @PutMapping(value = "/{id}/left", consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity putLeftDiffPart(@Parameter(description = "Diff id", required = true, example = "123") @PathVariable("id") Long diffId,
                                          @Parameter(description = "JSON base64 encoded binary data", required = true, example = "e30=")
                                          @RequestBody @Base64EncodedConstraint @ValidJsonConstraint byte[] requestBody) {
        log.debug("Left diff part received. id: '{}' ; content: '{}'", diffId, requestBody);
        diffService.putDiffPart(diffId, requestBody, DiffPart.LEFT);
        log.info("Left diff part successfully saved. diffId: '{}'", diffId);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    /**
     * @param diffId      The id of the diff to be created/updated
     * @param requestBody JSON base64 encoded binary data
     * @return HTTP 201 : When diff with given {id} is successfully created/updated
     * HTTP 400 : If request body is empty
     * HTTP 400 : If request body is not base64 encoded
     * HTTP 400 : If request body is not a valid JSON
     */
    @Operation(
            summary = "Creates/updates a diff object associated to the given {id} and sets the RIGHT part of it with the content of the request body.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Diff with given {id} was successfully created/updated"),
                    @ApiResponse(responseCode = "400", description = "If request body is empty, not base64 encoded or not a valid JSON",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE))
            })
    @PutMapping(value = "/{id}/right", consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity putRightDiffPart(@Parameter(description = "Diff id", required = true, example = "123") @PathVariable("id") Long diffId,
                                           @Parameter(description = "JSON base64 encoded binary data", required = true, example = "e30=")
                                           @RequestBody @Base64EncodedConstraint @ValidJsonConstraint byte[] requestBody) {
        log.debug("Right diff part received. id: '{}' ; content: '{}'", diffId, requestBody);
        diffService.putDiffPart(diffId, requestBody, DiffPart.RIGHT);
        log.info("Right diff part successfully saved. diffId: '{}'", diffId);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    /**
     * @param diffId The id of the diff to be created/updated
     * @return HTTP 200 : Diff results successfully retrieved
     * HTTP 400 : If diff with given {id} doesn't exist
     * HTTP 400 : If left/right part of diff associated to given {id} is missing
     */
    @Operation(
            summary = "Gets the results of the diff associated to the given {id}.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Diff results successfully retrieved"),
                    @ApiResponse(responseCode = "400", description = "If diff with given {id} doesn't exist or left/right part of diff associated to given {id} is missing.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DiffResult> getDiffResults(@Parameter(description = "Diff id", required = true, example = "123") @PathVariable("id") Long diffId) throws DiffException {
        log.debug("GetDiffResults called with id: '{}'", diffId);
        DiffResult diffResults = diffService.getDiffResults(diffId);
        log.debug("getDiffResults response: {}", diffResults);
        log.info("Diff results successfully retrieved. diffId: '{}'", diffId);
        return new ResponseEntity<>(diffResults, HttpStatus.OK);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleException(ConstraintViolationException exception, HttpServletResponse res) throws IOException {
        String violatedConstraints = exception.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .map(s -> s.replace("Content", "Request body"))
                .collect(Collectors.joining(" ; "));

        res.sendError(HttpStatus.BAD_REQUEST.value(), violatedConstraints);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleException(HttpServletResponse res) throws IOException {
        res.sendError(HttpStatus.BAD_REQUEST.value(), "Required request body is missing");
    }

    @ExceptionHandler(DiffException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleException(DiffException e, HttpServletResponse res) throws IOException {
        log.error(e.getMessage());
        res.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }
}