package codeminders.test;

import codeminders.test.processor.JavaFileProcessor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class CounterTest {

    private Counter counter = new Counter(new JavaFileProcessor());

    @Test
    public void fileTest() {
        Map<String, Integer> counters = counter.countLines(getClass().getClassLoader().getResource("emptyFile.java").getFile());
        Assertions.assertEquals(1, counters.size());
        Assertions.assertEquals(0, counters.get("emptyFile.java"));
    }

    @Test
    public void directoryTest() {
        Map<String, Integer> counters = counter.countLines(getClass().getClassLoader().getResource("non_empty").getFile());
        Assertions.assertEquals(7, counters.size());
        Assertions.assertEquals(18, counters.get("non_empty"));
        Assertions.assertEquals(0, counters.get("non_empty/fileWithCommentOnly.java"));
        Assertions.assertEquals(0, counters.get("non_empty/fileWithJavaDocOnly.java"));
        Assertions.assertEquals(18, counters.get("non_empty/with_code"));
        Assertions.assertEquals(6, counters.get("non_empty/with_code/fileWithSimpleCode.java"));
        Assertions.assertEquals(6, counters.get("non_empty/with_code/fileWithCodeAndComments.java"));
        Assertions.assertEquals(6, counters.get("non_empty/with_code/fileWithCodeAndComplicatedComments.java"));
    }
}
