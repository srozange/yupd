package io.github.yupd.infrastructure.utils;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DiffUtilsTest {

    @Test
    void shouldReturnNoDifferencesWhenContentsAreEqual() {
        String content = "line1" + System.lineSeparator() + "line2";
        
        String result = DiffUtils.generateDiff(content, content);
        
        assertThat(result).isEqualTo("No differences found");
    }

    @Test
    void shouldReturnDiffWhenContentsAreDifferent() {
        String oldContent = "old line";
        String newContent = "new line";
        
        String result = DiffUtils.generateDiff(oldContent, newContent);
        
        assertThat(result).isNotEmpty();
        assertThat(result).isEqualTo("- old line" + System.lineSeparator() + "+ new line");
    }
}