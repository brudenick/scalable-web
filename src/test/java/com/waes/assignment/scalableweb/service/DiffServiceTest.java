package com.waes.assignment.scalableweb.service;

import com.waes.StaticAppender;
import com.waes.assignment.scalableweb.exception.DiffException;
import com.waes.assignment.scalableweb.model.Diff;
import com.waes.assignment.scalableweb.model.DiffPart;
import com.waes.assignment.scalableweb.model.DiffResult;
import com.waes.assignment.scalableweb.repository.DiffRepository;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class DiffServiceTest {

    @Mock
    private DiffRepository diffRepository;

    @InjectMocks
    private DiffService diffService;

    public DiffServiceTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void givenNonexistentDiffIdWithLeftPart_putDiffPart_shouldCreateOneAndSaveIt() {
        // Setup
        StaticAppender.clearEvents();
        Long diffId = 1l;
        byte[] content = "content".getBytes();

        // Test
        diffService.putDiffPart(1l, content, DiffPart.LEFT);

        // Result verification
        Mockito.verify(diffRepository, Mockito.times(1)).save(any(Diff.class));
        assertEquals(String.format("[DEBUG] Diff doesn't exist. diffId: '%s'", diffId), StaticAppender.getEvents().get(0).toString());
        assertEquals(String.format("[DEBUG] Diff created. diffId: '%s'", diffId), StaticAppender.getEvents().get(1).toString());
        assertEquals(String.format("[DEBUG] Left part will be created. diffId: '%s'", diffId), StaticAppender.getEvents().get(2).toString());
        assertEquals(String.format("[DEBUG] Diff successfully saved. diffId: '%s'", diffId), StaticAppender.getEvents().get(3).toString());
    }

    @Test
    void givenNonexistentDiffIdWithRightPart_putDiffPart_shouldCreateOneAndSaveIt() {
        // Setup
        StaticAppender.clearEvents();
        Long diffId = 1l;
        byte[] content = "content".getBytes();

        // Test
        diffService.putDiffPart(1l, content, DiffPart.RIGHT);

        // Result verification
        Mockito.verify(diffRepository, Mockito.times(1)).save(any(Diff.class));
        assertEquals(String.format("[DEBUG] Diff doesn't exist. diffId: '%s'", diffId), StaticAppender.getEvents().get(0).toString());
        assertEquals(String.format("[DEBUG] Diff created. diffId: '%s'", diffId), StaticAppender.getEvents().get(1).toString());
        assertEquals(String.format("[DEBUG] Right part will be created. diffId: '%s'", diffId), StaticAppender.getEvents().get(2).toString());
        assertEquals(String.format("[DEBUG] Diff successfully saved. diffId: '%s'", diffId), StaticAppender.getEvents().get(3).toString());
    }

    @Test
    void givenExistentDiffIdWithLeftPart_putDiffPart_shouldUpdateAndSaveIt() {
        // Setup
        StaticAppender.clearEvents();
        Long diffId = 1l;
        byte[] content = "content".getBytes();
        Diff existentDiff = new Diff(1l);
        existentDiff.setLeft("left".getBytes());
        existentDiff.setRight(null);
        Mockito.when(diffRepository.find(diffId)).thenReturn(existentDiff);

        // Test
        diffService.putDiffPart(1l, content, DiffPart.LEFT);

        // Result verification
        Mockito.verify(diffRepository, Mockito.times(1)).save(any(Diff.class));
        assertEquals(String.format("[DEBUG] Diff already exists. diffId: '%s'", diffId), StaticAppender.getEvents().get(0).toString());
        assertEquals(String.format("[DEBUG] Left part will be updated. diffId: '%s'", diffId), StaticAppender.getEvents().get(1).toString());
        assertEquals(String.format("[DEBUG] Diff successfully saved. diffId: '%s'", diffId), StaticAppender.getEvents().get(2).toString());
    }

    @Test
    void givenExistentDiffIdWithRightPart_putDiffPart_shouldUpdateAndSaveIt() {
        // Setup
        StaticAppender.clearEvents();
        Long diffId = 1l;
        byte[] content = "content".getBytes();
        Diff existentDiff = new Diff(1l);
        existentDiff.setRight("right".getBytes());
        existentDiff.setLeft(null);
        Mockito.when(diffRepository.find(diffId)).thenReturn(existentDiff);

        // Test
        diffService.putDiffPart(1l, content, DiffPart.RIGHT);

        // Result verification
        Mockito.verify(diffRepository, Mockito.times(1)).save(any(Diff.class));
        assertEquals(String.format("[DEBUG] Diff already exists. diffId: '%s'", diffId), StaticAppender.getEvents().get(0).toString());
        assertEquals(String.format("[DEBUG] Right part will be updated. diffId: '%s'", diffId), StaticAppender.getEvents().get(1).toString());
        assertEquals(String.format("[DEBUG] Diff successfully saved. diffId: '%s'", diffId), StaticAppender.getEvents().get(2).toString());
    }

    @Test
    void givenExistentDiffIdWithEqualParts_getDiffResults_shouldCalculateDiff() throws DiffException {
        // Setup
        Long diffId = 1l;
        Diff diffWithEqualParts = new Diff(diffId);
        diffWithEqualParts.setRight(Base64.encodeBase64("equal".getBytes()));
        diffWithEqualParts.setLeft(Base64.encodeBase64("equal".getBytes()));
        String expected = String.format("Left and right are equal", diffId);
        Mockito.when(diffRepository.find(diffId)).thenReturn(diffWithEqualParts);

        // Test
        DiffResult result = diffService.getDiffResults(diffId);

        // Result verification
        assertEquals(expected, result.getMessage());
    }

    @Test
    void givenExistentDiffIdWithSameSizeDifferentContentParts_getDiffResults_shouldCalculateDiff() throws DiffException {
        // Setup
        Long diffId = 1l;
        Diff diffWithSameSizeDifferentContentParts = new Diff(diffId);
        diffWithSameSizeDifferentContentParts.setRight(Base64.encodeBase64("a".getBytes()));
        diffWithSameSizeDifferentContentParts.setLeft(Base64.encodeBase64("b".getBytes()));
        String expected = String.format("Left and right have the same size but have %s differences", 1);
        Mockito.when(diffRepository.find(diffId)).thenReturn(diffWithSameSizeDifferentContentParts);

        // Test
        DiffResult result = diffService.getDiffResults(diffId);

        // Result verification
        assertEquals(expected, result.getMessage());
    }

    @Test
    void givenExistentDiffIdWithDifferentSizeParts_getDiffResults_shouldCalculateDiff() throws DiffException {
        // Setup
        Long diffId = 1l;
        Diff diffWithDifferentSizeParts = new Diff(diffId);
        diffWithDifferentSizeParts.setRight(Base64.encodeBase64("abc".getBytes()));
        diffWithDifferentSizeParts.setLeft(Base64.encodeBase64("ab".getBytes()));
        String expected = String.format("Left and right have different sizes");
        Mockito.when(diffRepository.find(diffId)).thenReturn(diffWithDifferentSizeParts);

        // Test
        DiffResult result = diffService.getDiffResults(diffId);

        // Result verification
        assertEquals(expected, result.getMessage());
    }

    @Test
    void givenNonExistentDiffId_getDiffResults_shouldThrowDiffException() {
        // Setup
        Long diffId = 1l;

        // Test
        try {
            diffService.getDiffResults(diffId);
        } catch (DiffException e) {
            assertEquals(String.format("Diff with id '%s' doesn't exist", diffId), e.getMessage());
        }
    }

    @Test
    void givenExistentDiffIdWithoutLeftPart_getDiffResults_shouldThrowDiffException() {
        // Setup
        Long diffId = 1l;
        Diff diffWithoutLeftPart = new Diff(diffId);
        diffWithoutLeftPart.setRight("right".getBytes());
        diffWithoutLeftPart.setLeft(null);
        Mockito.when(diffRepository.find(diffId)).thenReturn(diffWithoutLeftPart);

        // Test and result verification
        try {
            diffService.getDiffResults(diffId);
        } catch (DiffException e) {
            assertEquals(String.format("Missing left part of diff. diffId: '%s'", diffId), e.getMessage());
        }
    }

    @Test
    void givenExistentDiffIdWithoutRightPart_getDiffResults_shouldThrowDiffException() {
        // Setup
        Long diffId = 1l;
        Diff diffWithoutLeftPart = new Diff(diffId);
        diffWithoutLeftPart.setLeft("left".getBytes());
        diffWithoutLeftPart.setRight(null);
        Mockito.when(diffRepository.find(diffId)).thenReturn(diffWithoutLeftPart);

        // Test and result verification
        try {
            diffService.getDiffResults(diffId);
        } catch (DiffException e) {
            assertEquals(String.format("Missing right part of diff. diffId: '%s'", diffId), e.getMessage());
        }
    }
}