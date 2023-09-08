package io.github.yupd.infrastructure.utils;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StringUtilsTest {

    @Test
    void testEqualsIgnoreTrailingSpaces() {
        assertThat(StringUtils.equalsIgnoreTrailingWhiteSpaces(null, null)).isTrue();
        assertThat(StringUtils.equalsIgnoreTrailingWhiteSpaces("", null)).isTrue();
        assertThat(StringUtils.equalsIgnoreTrailingWhiteSpaces(null, "")).isTrue();
        assertThat(StringUtils.equalsIgnoreTrailingWhiteSpaces("unix line endings\t \n", "unix line endings\n")).isTrue();
        assertThat(StringUtils.equalsIgnoreTrailingWhiteSpaces("windows line endings\t \r\n", "windows line endings\n\n\n\n")).isTrue();
        assertThat(StringUtils.equalsIgnoreTrailingWhiteSpaces(" space at start\n\n", "space at start\n\n")).isFalse();
        assertThat(StringUtils.equalsIgnoreTrailingWhiteSpaces("return\nin line\n", "return in line\n")).isFalse();
    }

}