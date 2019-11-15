package com.waes.assignment.scalableweb.repository;

import com.waes.assignment.scalableweb.model.Diff;

public interface DiffRepository {

    void save(Diff t);

    Diff find(Long id);
}