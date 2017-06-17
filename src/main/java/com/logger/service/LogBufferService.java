package com.logger.service;

import com.logger.model.Log;
import com.logger.model.LogData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@PropertySource("classpath:application.properties")
public class LogBufferService {

    @Value("${bufferSize}")
    private int bufferSize;

    private ConcurrentHashMap<Integer, LogData> watchData = new ConcurrentHashMap();

    public synchronized List<Log> getLastPeriod(Integer requestedInterval, Integer dataID) {
        List<Log> result = new ArrayList<>();
        LogData logData = watchData.get(dataID);
        if (logData == null) {
            throw new RuntimeException("Log id request does not match data");
        }

        int size = logData.buffer.size() - 1;
        long diff = System.currentTimeMillis() - (requestedInterval == null
                ? bufferSize : requestedInterval.intValue() * 1000);
        boolean end = false;

        while (size >= 0 && !end) {
            Log log = logData.buffer.get(size);
            if (log.timestamp > diff) {
                result.add(log);
            } else {
                end = true;
            }
            size--;
        }

        return result;
    }

    public synchronized Log put(Log entry, Integer dataID) {
        LogData logData = watchData.get(dataID);
        if (logData == null) {
            throw new RuntimeException("Log id request does not match data");
        }

        Log log = new Log(entry.getLogType(), entry.getMsg());
        log.timestamp = System.currentTimeMillis();
        log.setId(dataID);
        logData.buffer.add(log);
        if (logData.buffer.size() > bufferSize) {
            logData.buffer = new Vector<>(logData.buffer.subList(0, bufferSize / 2));
        }
        return log;
    }

    void setLogsData(Enumeration<Integer> ids) {
        while (ids.hasMoreElements()) {
            Integer id = ids.nextElement();
            LogData data = new LogData();
            data.id = id;
            data.buffer = new Vector(bufferSize, bufferSize);
            watchData.put(id, data);
        }
    }
}
