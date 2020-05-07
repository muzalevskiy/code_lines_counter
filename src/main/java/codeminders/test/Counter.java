package codeminders.test;

import codeminders.test.processor.FileProcessor;

import java.io.File;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Counter {

    private static final String JAVA_FILE_PATTERN = ".*\\.java";

    private final FileProcessor processor;

    public Counter(FileProcessor processor) {
        this.processor = processor;
    }

    public Map<String, Integer> countLines(String path) {
        File root = new File(path);
        if (root.isDirectory()) {
            try {
                return countLinesInADirectory(root).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                throw new RuntimeException("There was a problem during counting process");
            }
        } else {
            return countLinesInAFile(root);
        }
    }

    private Map<String, Integer> countLinesInAFile(File file) {
        if (file.getName().matches(JAVA_FILE_PATTERN)) {
            int result = processor.countCodeLines(file);
            return Collections.singletonMap(file.getName(), result);
        } else {
            throw new RuntimeException("Isn't java file");
        }
    }

    private CompletableFuture<Map<String, Integer>> countLinesInADirectory(File folder) {
        List<CompletableFuture<Map<String, Integer>>> futures = new Vector<>();
        HashSet<String> children = new HashSet<>();
        for(File file : folder.listFiles()) {
            children.add(file.getName());
            if(file.isDirectory()) {
                futures.add(countLinesInADirectory(file));
            } else {
                futures.add(
                        CompletableFuture.supplyAsync(() -> countLinesInAFile(file))
                );
            }
        }

        CompletableFuture<Void> future = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        return future.thenApply(res -> {
                    Map<String, Integer> result = new TreeMap<>();
                    AtomicInteger count = new AtomicInteger(0);
                    for(CompletableFuture<Map<String, Integer>> cf : futures) {
                        cf.join().forEach((key, value) -> {
                            if(children.contains(key)) {
                                count.addAndGet(value);
                            }
                            result.put(folder.getName() + "/" + key, value);
                        });
                    }
                    result.put(folder.getName(), count.intValue());
                    return result;
                }
        );
    }
}
