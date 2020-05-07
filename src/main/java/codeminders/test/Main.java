package codeminders.test;

import codeminders.test.processor.JavaFileProcessor;

import java.util.Map;

public class Main {

    public static void main(String[] args) {
        Counter counter = new Counter(new JavaFileProcessor());
        Map<String, Integer> counters = counter.countLines(args[0]);
        printCounters(counters);
    }

    private static void printCounters(Map<String, Integer> counters) {
        counters.forEach((key, value) -> {
            String[] path = key.split("/");
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < path.length - 1; i++) {
                sb.append("\t");
            }
            sb.append(path[path.length - 1]);
            System.out.println(sb.toString() + " : " + value);
        });
    }
}
