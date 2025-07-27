package io.github.yupd.infrastructure.update.updater;

import io.github.yupd.domain.model.ContentUpdateCriteria;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

class RegexUpdatorTest {

    @Test
    void noMatch() {
        ContentUpdateCriteria criteria = ContentUpdateCriteria.from("regex:(original contentt)", "titi");

        String result = new RegexUpdater().update("original content", criteria);

        assertThat(result).isEqualTo("original content");
    }

    @Test
    void matchNoGroup() {
        RegexUpdater updator = new RegexUpdater();
        
        String result = updator.update("name: oldname", ContentUpdateCriteria.from("regex:oldname", "newname"));
        result = updator.update(result, ContentUpdateCriteria.from("regex:name:", "newlabel:"));

        assertThat(result).isEqualTo("newlabel: newname");
    }

    @Test
    void match() {
        ContentUpdateCriteria criteria = ContentUpdateCriteria.from("regex:good name: ([a-z]+)", "newname");

        String result = new RegexUpdater().update(
                "1. should be updated -> good name: oldname\n" +
                        "2. should not be updated -> good name: 1oldname\n" +
                        "3. should not be updated -> bad name: oldname\n" +
                        "4. should be updated -> good name: oldoldname\n"
                , criteria);

        assertThat(result).isEqualTo("1. should be updated -> good name: newname\n" +
                "2. should not be updated -> good name: 1oldname\n" +
                "3. should not be updated -> bad name: oldname\n" +
                "4. should be updated -> good name: newname\n");
    }

}