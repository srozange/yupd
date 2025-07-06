package io.github.yupd.infrastructure.diff;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DiffServiceTest {

    private final DiffService diffService = new DiffService();

    @Test
    void shouldReturnNoDifferencesWhenContentsAreEqual() {
        String content = "line1" + System.lineSeparator() + "line2";
        
        String result = diffService.generateDiff(content, content);
        
        assertThat(result).isEqualTo("No differences found");
    }

    @Test
    void shouldReturnDiffWhenContentsAreDifferent() {
        String oldContent = "old line";
        String newContent = "new line";
        
        String result = diffService.generateDiff(oldContent, newContent);
        
        assertThat(result).isNotEmpty();
        assertThat(result).isEqualTo("- old line" + System.lineSeparator() + "+ new line");
    }
}