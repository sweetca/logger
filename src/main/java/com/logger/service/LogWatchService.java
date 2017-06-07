package com.logger.service;

import com.logger.model.Log;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;

@Service
@PropertySource("classpath:application.properties")
public class LogWatchService {
    private static final Logger LOGGER = Logger.getLogger(LogWatchService.class);

    private RandomAccessFile raf;
    private long lastSeek;

    @Value("${files.to.watch}")
    private String filesToWatch;

    private LogService logService;

    @Autowired
    public LogWatchService(LogService logService) {
        this.logService = logService;
    }

    @PostConstruct
    public void init() {
        String[] filesPath = filesToWatch.trim().split(",");
        try {
            raf = new RandomAccessFile(filesPath[0], "r");
            lastSeek = raf.length();
        } catch (IOException e) {
            LOGGER.warn("Can not read file by path: " + filesToWatch, e);
        }
    }

    @Async
    @Scheduled(initialDelay=1000, fixedDelay=25)
    public void check() throws IOException {
        if ( lastSeek == raf.length() ) {
            return;
        }

        byte[] buffer = new byte[(int)(raf.length() - lastSeek)];
        raf.seek(lastSeek);
        lastSeek = raf.length();
        raf.readFully(buffer);

        Log log = Log.parseLog(new String(buffer, Charset.forName("UTF-8")));
        logService.storeLog(log);
    }
}
