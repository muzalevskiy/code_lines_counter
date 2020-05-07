package codeminders.test.processor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

public class JavaFileProcessorTest {

    FileProcessor processor = new JavaFileProcessor();

    @Test
    public void emptyFile() {
        int result = processor.countCodeLines(
                new File(getClass().getClassLoader().getResource("emptyFile.java").getFile())
        );
        Assertions.assertEquals(0, result);
    }

    @Test
    public void fileWithJavaDocOnly() {
        int result = processor.countCodeLines(
                new File(getClass().getClassLoader().getResource("non_empty/fileWithJavaDocOnly.java").getFile())
        );
        Assertions.assertEquals(0, result);
    }

    @Test
    public void fileWithCommentOnly() {
        int result = processor.countCodeLines(
                new File(getClass().getClassLoader().getResource("non_empty/fileWithCommentOnly.java").getFile())
        );
        Assertions.assertEquals(0, result);
    }

    @Test
    public void fileWithSimpleCode() {
        int result = processor.countCodeLines(
                new File(getClass().getClassLoader().getResource("non_empty/with_code/fileWithSimpleCode.java").getFile())
        );
        Assertions.assertEquals(6, result);
    }

    @Test
    public void fileWithCodeAndComments() {
        int result = processor.countCodeLines(
                new File(getClass().getClassLoader().getResource("non_empty/with_code/fileWithCodeAndComments.java").getFile())
        );
        Assertions.assertEquals(6, result);
    }

    @Test
    public void fileWithCodeAndComplicatedComments() {
        int result = processor.countCodeLines(
                new File(getClass().getClassLoader().getResource("non_empty/with_code/fileWithCodeAndComplicatedComments.java").getFile())
        );
        Assertions.assertEquals(6, result);
    }
}
