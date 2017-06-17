package com.logger.service;

import com.logger.model.LogConnector;
import com.logger.model.Log;
import com.logger.model.LogsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class LogService {

    private EventSendService eventSendService;
    private LogBufferService logBufferService;
    private ConcurrentHashMap<Integer, LogConnector> watchData;

    @Autowired
    public LogService(EventSendService eventSendService, LogBufferService logBufferService) {
        this.eventSendService = eventSendService;
        this.logBufferService = logBufferService;
    }

    public synchronized void notifyLastLogsPeriod(LogsRequest request) {
        List<Log> logs = logBufferService.getLastPeriod(request.getPeriod(), request.getId());
        eventSendService.lastLogsNotification(logs, request.getUser());
    }

    public synchronized void storeLog(Log log, Integer dataId) {
        Log storedLog = logBufferService.put(log, dataId);
        eventSendService.logNotification(storedLog);
    }

    public void setWatchData(ConcurrentHashMap<Integer, LogConnector> watchData) {
        this.watchData = watchData;
        logBufferService.setLogsData(watchData.keys());
    }

    public List<LogConnector> getWatchData() {
        return watchData
                .values()
                .stream()
                .map(LogConnector::clone)
                .collect(Collectors.toList());
    }
}
