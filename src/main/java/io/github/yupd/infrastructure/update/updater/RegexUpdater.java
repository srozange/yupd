package io.github.yupd.infrastructure.update.updater;

import io.github.yupd.domain.model.ContentUpdateCriteria;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ApplicationScoped
public class RegexUpdater {

    public String update(String content, ContentUpdateCriteria contentUpdateCriteria) {
        StringBuilder result = new StringBuilder();
        Matcher matcher = Pattern.compile(contentUpdateCriteria.key()).matcher(content);
        while (matcher.find()) {
            String newValue;
            if (matcher.groupCount() > 0) {
                newValue = matcher.group(0).replace(matcher.group(1), contentUpdateCriteria.value());
            } else {
                newValue = contentUpdateCriteria.value();
            }
            matcher.appendReplacement(result, newValue);
        }
        matcher.appendTail(result);
        return result.toString();
    }
}