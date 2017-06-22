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
public class LogService implements ILogService {

    private final INotificationService notificationService;
    private final IBufferService logBufferService;
    private ConcurrentHashMap<Integer, LogConnector> watchData;

    @Autowired
    public LogService(INotificationService notificationService, IBufferService logBufferService) {
        this.notificationService = notificationService;
        this.logBufferService = logBufferService;
    }

    @Override
    public synchronized void notifyLastLogsPeriod(LogsRequest request) {
        List<Log> logs = logBufferService.getLastPeriod(request.getPeriod(), request.getId());
        notificationService.lastLogsNotification(logs, request.getUser());
    }

    @Override
    public synchronized void storeLog(List<Log> logs,final Integer dataId) {
        logs.forEach(l -> this.storeLog(l, dataId));
    }

    @Override
    public synchronized void storeLog(Log log,final Integer dataId) {
        Log storedLog = logBufferService.put(log, dataId);
        notificationService.logNotification(storedLog);
    }

    @Override
    public void setWatchData(ConcurrentHashMap<Integer, LogConnector> watchData) {
        this.watchData = watchData;
        logBufferService.setLogsData(watchData.keys());
    }

    @Override
    public List<LogConnector> getWatchData() {
        return watchData
                .values()
                .stream()
                .map(LogConnector::clone)
                .collect(Collectors.toList());
    }
}
