package com.waes.assignment.scalableweb;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.waes.assignment.scalableweb.model.DiffResult;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ScalableWebApplication.class)
@AutoConfigureMockMvc
public class DiffControllerIT {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String DIFF_API_ROOT_PATH = "/v1/diff";
    private final String FILES_PATH = Paths.get("src", "integration-test", "resources").toFile().getAbsolutePath();

    @Autowired
    private MockMvc mvc;

    @Test
    @DirtiesContext
    public void givenValidBase64EncodedJson_whenPutLeftDiffPart_thenStatus201() throws Exception {
        // Setup
        byte[] validBase64EncodedJson = Base64.getEncoder().encode(Files.readAllBytes(Paths.get(FILES_PATH, "valid_json")));

        // Test and results verification
        mvc.perform(put(DIFF_API_ROOT_PATH + "/1/left")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .content(validBase64EncodedJson))
                .andExpect(status().isCreated());
    }

    @Test
    @DirtiesContext
    public void givenValidBase64EncodedJson_whenPutRightDiffPart_thenStatus201() throws Exception {
        // Setup
        byte[] validBase64EncodedJson = Base64.getEncoder().encode(Files.readAllBytes(Paths.get(FILES_PATH, "valid_json")));

        // Test and results verification
        mvc.perform(put(DIFF_API_ROOT_PATH + "/1/right")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .content(validBase64EncodedJson))
                .andExpect(status().isCreated());
    }

    @Test
    public void givenInvalidBase64EncodedJson_whenPutLeftDiffPart_thenStatus400() throws Exception {
        // Setup
        byte[] invalidBase64EncodedJson = Base64.getEncoder().encode(Files.readAllBytes(Paths.get(FILES_PATH, "invalid_json")));
        String expectedErrorMessage = "Request body is not a valid JSON";

        // Test and results verification
        MvcResult result = mvc.perform(put(DIFF_API_ROOT_PATH + "/1/left")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .content(invalidBase64EncodedJson))
                .andExpect(status().isBadRequest())
                .andReturn();
        Assert.assertEquals(expectedErrorMessage, result.getResponse().getErrorMessage());
    }

    @Test
    public void givenInvalidBase64EncodedJson_whenPutRightDiffPart_thenStatus400() throws Exception {
        // Setup
        byte[] invalidBase64EncodedJson = Base64.getEncoder().encode(Files.readAllBytes(Paths.get(FILES_PATH, "invalid_json")));
        String expectedErrorMessage = "Request body is not a valid JSON";

        // Test and results verification
        MvcResult result = mvc.perform(put(DIFF_API_ROOT_PATH + "/1/right")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .content(invalidBase64EncodedJson))
                .andExpect(status().isBadRequest())
                .andReturn();
        Assert.assertEquals(expectedErrorMessage, result.getResponse().getErrorMessage());
    }

    @Test
    public void givenValidNotBase64EncodedJson_whenPutLeftDiffPart_thenStatus400() throws Exception {
        // Setup
        byte[] validNotBase64EncodedJson = Files.readAllBytes(Paths.get(FILES_PATH, "valid_json"));
        String expectedErrorMessage = "Request body is not BASE64 encoded";

        // Test and results verification
        MvcResult result = mvc.perform(put(DIFF_API_ROOT_PATH + "/1/left")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .content(validNotBase64EncodedJson))
                .andExpect(status().isBadRequest())
                .andReturn();
        Assert.assertEquals(expectedErrorMessage, result.getResponse().getErrorMessage());
    }

    @Test
    public void givenValidNotBase64EncodedJson_whenPutRightDiffPart_thenStatus400() throws Exception {
        // Setup
        byte[] validNotBase64EncodedJson = Files.readAllBytes(Paths.get(FILES_PATH, "valid_json"));
        String expectedErrorMessage = "Request body is not BASE64 encoded";

        // Test and results verification
        MvcResult result = mvc.perform(put(DIFF_API_ROOT_PATH + "/1/right")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .content(validNotBase64EncodedJson))
                .andExpect(status().isBadRequest())
                .andReturn();
        Assert.assertEquals(expectedErrorMessage, result.getResponse().getErrorMessage());
    }

    @Test
    public void givenInvalidNotBase64EncodedJson_whenPutLeftDiffPart_thenStatus400() throws Exception {
        // Setup
        byte[] invalidNotBase64EncodedJson = Files.readAllBytes(Paths.get(FILES_PATH, "invalid_json"));
        String expectedErrorMessage1 = "Request body is not BASE64 encoded";
        String expectedErrorMessage2 = "Request body is not a valid JSON";

        // Test and results verification
        MvcResult result = mvc.perform(put(DIFF_API_ROOT_PATH + "/1/left")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .content(invalidNotBase64EncodedJson))
                .andExpect(status().isBadRequest())
                .andReturn();
        Assert.assertTrue(result.getResponse().getErrorMessage().contains(expectedErrorMessage1));
        Assert.assertTrue(result.getResponse().getErrorMessage().contains(expectedErrorMessage2));
    }

    @Test
    public void givenInvalidNotBase64EncodedJson_whenPutRightDiffPart_thenStatus400() throws Exception {
        // Setup
        byte[] invalidNotBase64EncodedJson = Files.readAllBytes(Paths.get(FILES_PATH, "invalid_json"));
        String expectedErrorMessage1 = "Request body is not BASE64 encoded";
        String expectedErrorMessage2 = "Request body is not a valid JSON";

        // Test and results verification
        MvcResult result = mvc.perform(put(DIFF_API_ROOT_PATH + "/1/right")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .content(invalidNotBase64EncodedJson))
                .andExpect(status().isBadRequest())
                .andReturn();
        Assert.assertTrue(result.getResponse().getErrorMessage().contains(expectedErrorMessage1));
        Assert.assertTrue(result.getResponse().getErrorMessage().contains(expectedErrorMessage2));
    }

    @Test
    public void givenEmptyContent_whenPutLeftDiffPart_thenStatus400() throws Exception {
        // Setup
        String expectedErrorMessage = "Required request body is missing";

        // Test and results verification
        MvcResult result = mvc.perform(put(DIFF_API_ROOT_PATH + "/1/left")
                .contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(status().isBadRequest())
                .andReturn();
        Assert.assertEquals(expectedErrorMessage, result.getResponse().getErrorMessage());
    }

    @Test
    public void givenEmptyContent_whenPutRightDiffPart_thenStatus400() throws Exception {
        // Setup
        String expectedErrorMessage = "Required request body is missing";

        // Test and results verification
        MvcResult result = mvc.perform(put(DIFF_API_ROOT_PATH + "/1/right")
                .contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(status().isBadRequest())
                .andReturn();
        Assert.assertEquals(expectedErrorMessage, result.getResponse().getErrorMessage());
    }

    @Test
    public void givenNonexistingDiffId_whenGetDiffResults_thenStatus400() throws Exception {
        // Setup
        String expectedErrorMessage = "Diff with id '1' doesn't exist";

        // Test and results verification
        MvcResult result = mvc.perform(get(DIFF_API_ROOT_PATH + "/1"))
                .andExpect(status().isBadRequest())
                .andReturn();
        Assert.assertEquals(expectedErrorMessage, result.getResponse().getErrorMessage());
    }

    @Test
    @DirtiesContext
    public void givenDiffWithoutLeftPart_whenGetDiffResults_thenStatus400() throws Exception {
        // Setup
        String expectedErrorMessage = "Missing left part of diff. diffId: '1'";
        byte[] validBase64EncodedJson = Base64.getEncoder().encode(Files.readAllBytes(Paths.get(FILES_PATH, "valid_json")));
        mvc.perform(put(DIFF_API_ROOT_PATH + "/1/right")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .content(validBase64EncodedJson));

        // Test and results verification
        MvcResult result = mvc.perform(get(DIFF_API_ROOT_PATH + "/1"))
                .andExpect(status().isBadRequest())
                .andReturn();
        Assert.assertEquals(expectedErrorMessage, result.getResponse().getErrorMessage());
    }

    @Test
    @DirtiesContext
    public void givenDiffWithoutRightPart_whenGetDiffResults_thenStatus400() throws Exception {
        // Setup
        String expectedErrorMessage = "Missing right part of diff. diffId: '1'";
        byte[] validBase64EncodedJson = Base64.getEncoder().encode(Files.readAllBytes(Paths.get(FILES_PATH, "valid_json")));
        mvc.perform(put(DIFF_API_ROOT_PATH + "/1/left")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .content(validBase64EncodedJson));

        // Test and results verification
        MvcResult result = mvc.perform(get(DIFF_API_ROOT_PATH + "/1"))
                .andExpect(status().isBadRequest())
                .andReturn();
        Assert.assertEquals(expectedErrorMessage, result.getResponse().getErrorMessage());
    }

    @Test
    @DirtiesContext
    public void givenDiffWithEqualSizeAndContentParts_whenGetDiffResults_thenStatus200() throws Exception {
        // Setup
        DiffResult expectedDiffResult = new DiffResult(1l, "Left and right are equal", Collections.emptyList());
        byte[] validBase64EncodedJson = Base64.getEncoder().encode(Files.readAllBytes(Paths.get(FILES_PATH, "valid_json")));
        mvc.perform(put(DIFF_API_ROOT_PATH + "/1/left")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .content(validBase64EncodedJson));
        mvc.perform(put(DIFF_API_ROOT_PATH + "/1/right")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .content(validBase64EncodedJson));

        // Test and results verification
        mvc.perform(get(DIFF_API_ROOT_PATH + "/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedDiffResult)));
    }

    @Test
    @DirtiesContext
    public void givenDiffWithDifferentSizeParts_whenGetDiffResults_thenStatus200() throws Exception {
        // Setup
        DiffResult expectedDiffResult = new DiffResult(1l, "Left and right have different sizes", Collections.EMPTY_LIST);
        byte[] validBase64EncodedJson = Base64.getEncoder().encode(Files.readAllBytes(Paths.get(FILES_PATH, "valid_json")));
        byte[] equalSizeDifferentContentJson = Base64.getEncoder().encode(Files.readAllBytes(Paths.get(FILES_PATH, "different_size_json")));
        mvc.perform(put(DIFF_API_ROOT_PATH + "/1/left")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .content(validBase64EncodedJson));
        mvc.perform(put(DIFF_API_ROOT_PATH + "/1/right")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .content(equalSizeDifferentContentJson));

        // Test and results verification
        mvc.perform(get(DIFF_API_ROOT_PATH + "/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedDiffResult)));
    }

    @Test
    @DirtiesContext
    public void givenDiffWithEqualSizeAndDifferentContentParts_whenGetDiffResults_thenStatus200() throws Exception {
        // Setup
        String difference1 = "Difference found at position 18. Difference size: 4 character/s";
        String difference2 = "Difference found at position 75. Difference size: 2 character/s";
        String difference3 = "Difference found at position 202. Difference size: 2 character/s";
        DiffResult expectedDiffResult = new DiffResult(1l, "Left and right have the same size but have 3 differences", Arrays.asList(difference1, difference2, difference3));
        byte[] validBase64EncodedJson = Base64.getEncoder().encode(Files.readAllBytes(Paths.get(FILES_PATH, "valid_json")));
        byte[] equalSizeDifferentContentJson = Base64.getEncoder().encode(Files.readAllBytes(Paths.get(FILES_PATH, "equal_size_different_content_json")));
        mvc.perform(put(DIFF_API_ROOT_PATH + "/1/left")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .content(validBase64EncodedJson));
        mvc.perform(put(DIFF_API_ROOT_PATH + "/1/right")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .content(equalSizeDifferentContentJson));

        // Test and results verification
        mvc.perform(get(DIFF_API_ROOT_PATH + "/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedDiffResult)));
    }
}
