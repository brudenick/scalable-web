package com.waes.assignment.scalableweb.repository;

import com.waes.assignment.scalableweb.model.Diff;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryDiffRepositoryTest {

    private final DiffRepository inMemoryDiffRepository;

    public InMemoryDiffRepositoryTest() {
        this.inMemoryDiffRepository = new InMemoryDiffRepository();
    }

    @Test
    void should_save_diff_and_find_it_by_id() {
        // Setup
        final Diff diff = new Diff(1l);
        diff.setLeft("left".getBytes());
        diff.setRight("right".getBytes());

        // Test
        inMemoryDiffRepository.save(diff);
        Diff result = inMemoryDiffRepository.find(diff.getId());

        // Result verification
        assertEquals(diff, result);
    }
}