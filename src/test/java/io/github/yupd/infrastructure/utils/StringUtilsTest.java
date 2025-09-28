package io.github.yupd.infrastructure.utils;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StringUtilsTest {

    @Test
    void testIsNullOrEmpty() {
        assertThat(StringUtils.isNullOrEmpty(null)).isTrue();
        assertThat(StringUtils.isNullOrEmpty("")).isTrue();
        assertThat(StringUtils.isNullOrEmpty(" ")).isFalse();
        assertThat(StringUtils.isNullOrEmpty("test")).isFalse();
    }

    @Test
    void testIsNotEmpty() {
        assertThat(StringUtils.isNotEmpty(null)).isFalse();
        assertThat(StringUtils.isNotEmpty("")).isFalse();
        assertThat(StringUtils.isNotEmpty(" ")).isTrue();
        assertThat(StringUtils.isNotEmpty("test")).isTrue();
    }

    @Test
    void testNullToEmpty() {
        assertThat(StringUtils.nullToEmpty(null)).isEqualTo("");
        assertThat(StringUtils.nullToEmpty("")).isEqualTo("");
        assertThat(StringUtils.nullToEmpty(" ")).isEqualTo(" ");
        assertThat(StringUtils.nullToEmpty("test")).isEqualTo("test");
    }

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