package com.test.worker;

import com.test.exception.CriticalAppException;
import com.test.logreader.LogFileReader;
import com.test.recordparser.LogItem;
import com.test.worker.Worker;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

public class DataProcessor {

    public static void procesingData(ConcurrentHashMap<String, LogItem> storedItems, LogFileReader fileReader) throws CriticalAppException {
        List<Future<?>> startedTasks = new ArrayList<Future<?>>();
        ExecutorService exec = Executors.newFixedThreadPool(5);
        Optional<String> chunkToProcess;
        while ((chunkToProcess = fileReader.getChunkToProcess()).isPresent()) {
            String stripedChunkToProcess = chunkToProcess.get();
            Worker worker = new Worker(stripedChunkToProcess, storedItems);
            Future<?> f = exec.submit(worker);
            startedTasks.add(f);
        }
        awaitForRunningTasks(startedTasks);
        exec.shutdown();
    }

    private static void awaitForRunningTasks(List<Future<?>> startedTasks) throws CriticalAppException {
        for (Future<?> future : startedTasks) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new CriticalAppException("Problem with retrieving result from thread" + e.toString());
            }
        }
    }
}