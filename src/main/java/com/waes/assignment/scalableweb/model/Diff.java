package com.waes.assignment.scalableweb.model;

import lombok.Data;

@Data
public class Diff {

    private final Long id;
    private byte[] left;
    private byte[] right;

}