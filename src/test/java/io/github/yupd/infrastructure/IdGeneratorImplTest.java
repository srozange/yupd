package io.github.yupd.infrastructure;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class IdGeneratorImplTest {

    @Test
    void should_generate_valid_uuid() {
        assertThat(new IdGeneratorImpl().generate())
            .isNotNull()
            .isNotEmpty();
    }

    @Test
    void should_generate_unique_ids() {
        assertThat(new IdGeneratorImpl().generate()).isNotEqualTo(new IdGeneratorImpl().generate());
    }
}