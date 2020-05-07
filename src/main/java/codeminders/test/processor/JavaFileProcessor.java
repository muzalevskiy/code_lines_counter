package codeminders.test.processor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class JavaFileProcessor implements FileProcessor {

    public static final String COMMENT_STRING = "//";
    public static final String MULTILINE_COMMENT_START_STRING = "/*";
    public static final String MULTILINE_COMMENT_END_STRING = "*/";

    @Override
    public int countCodeLines(File file) {

        int result = 0;
        AtomicBoolean openCommentPresent = new AtomicBoolean(false);
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String currentLine;
            while ((currentLine = br.readLine()) != null)
            {
                if (isCodeLine(currentLine, openCommentPresent)) {
                    result++;
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private boolean isCodeLine(String line, AtomicBoolean openCommentPresent) {
        line = line.trim();
        if(line.isBlank() || line.startsWith(COMMENT_STRING)) {
            return false;
        } else {
            if(openCommentPresent.get()) {
                int indexOfCloseComment = line.indexOf(MULTILINE_COMMENT_END_STRING);
                if(indexOfCloseComment == -1) {
                    return false;
                } else {
                    openCommentPresent.set(false);
                    return isCodeLine(line.substring(indexOfCloseComment + 2), openCommentPresent);
                }
            } else {
                int indexOfOpenComment = line.indexOf(MULTILINE_COMMENT_START_STRING);
                int indexOfCloseComment = line.indexOf(MULTILINE_COMMENT_END_STRING);
                if(indexOfOpenComment != -1 && indexOfCloseComment != -1) {
                    return isCodeLine(line.substring(0, indexOfOpenComment).concat(line.substring(indexOfCloseComment + 2)), openCommentPresent);
                } else if(indexOfOpenComment != -1) {
                    boolean result = isCodeLine(line.substring(0, indexOfOpenComment), openCommentPresent);
                    openCommentPresent.set(true);
                    return result;
                } else {
                    return true;
                }
            }
        }
    }
}
