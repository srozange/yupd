package io.github.yupd.domain.model;

import io.github.yupd.infrastructure.utils.DiffUtils;
import io.github.yupd.infrastructure.utils.StringUtils;

public record GitFileUpdateResult(String originalContent, String newContent) {

    public boolean updated() {
        return !StringUtils.equalsIgnoreTrailingWhiteSpaces(originalContent, newContent);
    }

    public String generateDiff() {
        return DiffUtils.generateDiff(originalContent, newContent);
    }
}
