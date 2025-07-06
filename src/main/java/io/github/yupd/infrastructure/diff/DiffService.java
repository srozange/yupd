package io.github.yupd.infrastructure.diff;

import com.github.difflib.DiffUtils;
import com.github.difflib.patch.Patch;
import com.github.difflib.patch.AbstractDelta;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Arrays;
import java.util.List;

@ApplicationScoped
public class DiffService {

    public String generateDiff(String oldContent, String newContent) {
        List<String> oldLines = Arrays.asList(oldContent.split(System.lineSeparator()));
        List<String> newLines = Arrays.asList(newContent.split(System.lineSeparator()));
        
        Patch<String> patch = DiffUtils.diff(oldLines, newLines);
        
        if (patch.getDeltas().isEmpty()) {
            return "No differences found";
        }
        
        StringBuilder diff = new StringBuilder();
        
        for (AbstractDelta<String> delta : patch.getDeltas()) {
            delta.getSource().getLines().forEach(line -> diff.append("- ").append(line).append(System.lineSeparator()));
            delta.getTarget().getLines().forEach(line -> diff.append("+ ").append(line).append(System.lineSeparator()));
        }
        
        return diff.toString().trim();
    }
}