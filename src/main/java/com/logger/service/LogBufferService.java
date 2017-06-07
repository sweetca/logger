package com.logger.service;

import com.logger.model.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.util.*;

@Service
@PropertySource("classpath:application.properties")
public class LogBufferService {

    @Value("${interval}")
    private int interval;

    private int dataSize;
    private Vector<Log> buffer;

    @PostConstruct
    public void init() {
        dataSize = interval;
        buffer = new Vector(dataSize, interval);
    }

    public synchronized List<Log> getLastPeriod(Integer requestedInterval) {
        List<Log> result = new ArrayList<>();

        int size = buffer.size() - 1;
        long diff = System.currentTimeMillis() - (requestedInterval == null
                ? interval : requestedInterval.intValue() * 1000);
        boolean end = false;

        while (size >= 0 && !end) {
            Log log = buffer.get(size);
            if (log.timestamp > diff) {
                result.add(log);
            } else {
                end = true;
            }
            size--;
        }

        return result;
    }

    public synchronized Log put(Log entry) {
        Log log = new Log(entry.getLogType(), entry.getMsg());
        log.timestamp = System.currentTimeMillis();
        buffer.add(log);
        if (buffer.size() > dataSize) {
            buffer = new Vector<>(buffer.subList(0, dataSize / 2));
        }
        return log;
    }
}
