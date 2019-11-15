package com.waes.assignment.scalableweb.service;

import com.waes.assignment.scalableweb.exception.DiffException;
import com.waes.assignment.scalableweb.model.Diff;
import com.waes.assignment.scalableweb.model.DiffPart;
import com.waes.assignment.scalableweb.model.DiffResult;
import com.waes.assignment.scalableweb.repository.DiffRepository;
import com.waes.assignment.scalableweb.util.DiffUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class DiffService {

    private final DiffRepository diffRepository;

    public void putDiffPart(Long diffId, byte[] base64Content, DiffPart diffPart) {
        Diff diff = diffRepository.find(diffId);
        if (diff == null) {
            log.debug("Diff doesn't exist. diffId: '{}'", diffId);
            diff = new Diff(diffId);
            log.debug("Diff created. diffId: '{}'", diffId);
        } else {
            log.debug("Diff already exists. diffId: '{}'", diffId);
        }
        switch (diffPart) {
            case LEFT:
                if ((diff.getLeft() == null)) {
                    log.debug("Left part will be created. diffId: '{}'", diffId);
                } else {
                    log.debug("Left part will be updated. diffId: '{}'", diffId);
                }
                diff.setLeft(base64Content);
                break;
            case RIGHT:
                if ((diff.getRight() != null)) {
                    log.debug("Right part will be created. diffId: '{}'", diffId);
                } else {
                    log.debug("Right part will be updated. diffId: '{}'", diffId);
                }
                diff.setRight(base64Content);
                break;
        }
        diffRepository.save(diff);
        log.debug("Diff successfully saved. diffId: '{}'", diffId);
    }

    public DiffResult getDiffResults(Long diffId) throws DiffException {
        Diff diff = diffRepository.find(diffId);
        validateDiff(diff, diffId);
        //Diff is valid
        if (diff.getRight().length != diff.getLeft().length) {
            log.debug("Left and right have different sizes. diffId: {}", diffId);
            String sizeMessageTemplate = "%s part size: %s bytes";
            return new DiffResult(
                    diffId, "Left and right have different sizes",
                    Arrays.asList(
                            String.format(sizeMessageTemplate, "Left", diff.getLeft().length),
                            String.format(sizeMessageTemplate, "Right", diff.getRight().length)
                    ));
        }
        log.debug("Left and right have the same size. diffId: {}", diffId);
        List<String> differences = DiffUtil.jsonDiff(new String(Base64.decodeBase64(diff.getLeft())), new String(Base64.decodeBase64(diff.getRight())));
        log.debug("Number of differences between parts: {}", differences.size());
        return new DiffResult(diffId, (differences.size() == 0) ? "Left and right are equal" : String.format("Left and right have the same size but have %s differences", differences.size()),
                differences);
    }

    private void validateDiff(Diff diff, Long diffId) throws DiffException {
        if (diff == null) {
            throw new DiffException(String.format("Diff with id '%s' doesn't exist", diffId));
        }
        if (diff.getLeft() == null) {
            throw new DiffException(String.format("Missing left part of diff. diffId: '%s'", diffId));
        }
        if (diff.getRight() == null) {
            throw new DiffException(String.format("Missing right part of diff. diffId: '%s'", diffId));
        }
    }
}