package io.github.yupd.infrastructure.update.updator;

import io.github.yupd.infrastructure.update.model.ContentUpdateCriteria;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ApplicationScoped
public class RegexUpdator {

    public String update(String content, List<ContentUpdateCriteria> updates) {
        return updates.stream()
                .reduce(content,
                        (currentContent, update) -> update(currentContent, update),
                        (previous, last) -> last
                );
    }

    private String update(String content, ContentUpdateCriteria update) {
        StringBuffer result = new StringBuffer();
        Matcher matcher = Pattern.compile(update.key()).matcher(content);
        while (matcher.find()) {
            String newValue;
            if (matcher.groupCount() > 0) {
                newValue = matcher.group(0).replace(matcher.group(1), update.value());
            } else {
                newValue = update.value();
            }
            matcher.appendReplacement(result, newValue);
        }
        matcher.appendTail(result);
        return result.toString();
    }
}