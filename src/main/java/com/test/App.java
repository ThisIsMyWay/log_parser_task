package com.test;

import ch.qos.logback.classic.Logger;
import com.test.db.DBManager;
import com.test.exception.CriticalAppException;
import com.test.logreader.LogFileReader;
import com.test.recordparser.LogItem;
import com.test.worker.DataProcessor;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

public class App {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        logger.info("============== Checking  ==================");
        String pathToFile = getPathFromArgs(args);
        if (pathToFile == null) {
            logger.error("Path wasn't passed, please pass it with -args flag");
            return;
        }

        DBManager dbManager = new DBManager();
        try {
            logger.info("============== Initialize db ==================");
            dbManager.initializeDB();

            logger.info("============== Initialize db ==================");
            ConcurrentHashMap<String, LogItem> storedItems = new ConcurrentHashMap<String, LogItem>(1000);

            logger.info("============== Initialize File Reader ==================");
            LogFileReader fileReader = new LogFileReader(pathToFile);


            logger.info("============== Start Processing ==================");
            DataProcessor.procesingData(storedItems, fileReader);
        } catch (CriticalAppException e) {
            logger.error("Processing file failed" + e.toString());
            return;
        } finally {
            dbManager.stopDB();
        }
        logger.info("============== Processing Finished ==================");

    }

    private static String getPathFromArgs(String[] args) {
        String pathToFile;
        if (args.length == 0){
            pathToFile = null;
        } else {
            pathToFile = args[0];
        }
        return pathToFile;
    }

}
