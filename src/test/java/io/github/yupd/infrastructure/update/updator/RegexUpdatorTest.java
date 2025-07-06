package io.github.yupd.infrastructure.update.updator;

import io.github.yupd.infrastructure.update.model.ContentUpdateCriteria;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

class RegexUpdatorTest {

    @Test
    void noMatch() {
        ContentUpdateCriteria criteria = new ContentUpdateCriteria("regex:(original contentt)", "titi");

        String result = new RegexUpdator().update("original content", criteria);

        assertThat(result).isEqualTo("original content");
    }

    @Test
    void matchNoGroup() {
        RegexUpdator updator = new RegexUpdator();
        
        String result = updator.update("name: oldname", new ContentUpdateCriteria("regex:oldname", "newname"));
        result = updator.update(result, new ContentUpdateCriteria("regex:name:", "newlabel:"));

        assertThat(result).isEqualTo("newlabel: newname");
    }

    @Test
    void match() {
        ContentUpdateCriteria criteria = new ContentUpdateCriteria("regex:good name: ([a-z]+)", "newname");

        String result = new RegexUpdator().update(
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