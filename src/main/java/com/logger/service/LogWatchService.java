package com.logger.service;

import com.logger.model.LogConnector;
import com.logger.model.Log;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@PropertySource("classpath:application.properties")
public class LogWatchService {
    private static final Logger LOGGER = Logger.getLogger(LogWatchService.class);

    private AtomicInteger ids = new AtomicInteger(0);
    private final ConcurrentHashMap<Integer, LogConnector> watchData = new ConcurrentHashMap<>();

    @Value("${files.to.watch}")
    private String filesToWatch;

    @Value("${invoke.stored.logs}")
    private Boolean invokeLogs;

    private LogService logService;

    @Autowired
    public LogWatchService(LogService logService) {
        this.logService = logService;
    }

    @PostConstruct
    public void init() {
        String[] filesPath = filesToWatch.trim().split(",");
        for (String aFilesPath : filesPath) {
            try {
                LogConnector connector = new LogConnector();
                connector.id = ids.incrementAndGet();
                connector.raf = new RandomAccessFile(new File(aFilesPath), "r");

                if (invokeLogs) {
                    connector.lastSeek = 0L;
                } else {
                    connector.lastSeek = connector.raf.length();
                }

                connector.fileName = aFilesPath;
                connector.shortName = aFilesPath.substring(
                        aFilesPath.lastIndexOf(
                                System.getProperty("file.separator")
                        ),
                        aFilesPath.length()
                );
                watchData.put(connector.id, connector);
            } catch (IOException e) {
                LOGGER.warn("Can not read file by path: " + filesToWatch, e);
                throw new RuntimeException(e);
            }
        }
        logService.setWatchData(watchData);
    }

    @Async
    @Scheduled(initialDelay=1000, fixedDelay=25)
    public void check() throws IOException {
        Enumeration<Integer> ids = watchData.keys();
        while (ids.hasMoreElements()) {

            LogConnector connector = watchData.get(ids.nextElement());

            if ( connector.lastSeek == connector.raf.length() ) {
                return;
            }

            byte[] buffer = new byte[(int)(connector.raf.length() - connector.lastSeek)];
            connector.raf.seek(connector.lastSeek);
            connector.lastSeek = connector.raf.length();
            connector.raf.readFully(buffer);

            List<Log> logs = Log.parseLogs(new String(buffer, Charset.forName("UTF-8")));
            logService.storeLog(logs, connector.id);
        }
    }
}
