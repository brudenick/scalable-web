package com.waes.assignment.scalableweb.repository;

import com.waes.assignment.scalableweb.model.Diff;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryDiffRepository implements DiffRepository {

    /**
     * Concurrent diffs map, which uses the diff id as {@code key}.
     */
    private final Map<Long, Diff> diffsMap = new ConcurrentHashMap<>();

    @Override
    public void save(Diff t) {
        diffsMap.put(t.getId(), t);
    }

    @Override
    public Diff find(Long id) {
        return diffsMap.get(id);
    }
}