package com.test.worker;

import ch.qos.logback.classic.Logger;
import com.test.App;
import com.test.db.DBWriter;
import com.test.db.EventItem;
import com.test.exception.CriticalAppException;
import com.test.recordparser.LogItem;
import com.test.recordparser.LogItemParser;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Worker implements Runnable {

    private List<LogItem> logItemsToProcess;
    private ConcurrentHashMap<String, LogItem> mapWithItems;
    private static final Logger logger = (Logger) LoggerFactory.getLogger(Worker.class);


    public Worker(String dataToProcess, ConcurrentHashMap<String, LogItem> mapWithItems  ) {
        this.logItemsToProcess = LogItemParser.convert(dataToProcess);
        this.mapWithItems = mapWithItems;
    }

    @Override
    public void run() {
        for (int i = 0; i < logItemsToProcess.size(); i++) {
            LogItem logItem = logItemsToProcess.get(i);
            LogItem retrievedFromMap = mapWithItems.get(logItem.getId());
            if (retrievedFromMap == null){
                mapWithItems.put(logItem.getId(), logItem);
            } else {
                mapWithItems.remove(logItem.getId());
                long duration = Math.abs(logItem.getTimestamp() - retrievedFromMap.getTimestamp());
                try {
                    DBWriter.saveEvent(new EventItem(logItem.getId(), duration, logItem.getType(), logItem.getHost(), duration > 4));
                } catch (CriticalAppException e) {
                    logger.error("Problem with saving event " + e.toString());
                }
            }

        }
    }
}
