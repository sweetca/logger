package com.logger.service;

import com.logger.model.Log;
import com.logger.model.LogsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LogService {

    private EventSendService eventSendService;
    private LogBufferService logBufferService;

    @Autowired
    public LogService(EventSendService eventSendService, LogBufferService logBufferService) {
        this.eventSendService = eventSendService;
        this.logBufferService = logBufferService;
    }

    public synchronized void notifyLastLogsPeriod(LogsRequest request) {
        List<Log> logs = logBufferService.getLastPeriod(request.getPeriod());
        eventSendService.lastLogsNotification(logs, request.getUser());
    }

    public synchronized void storeLog(Log log) {
        Log storedLog = logBufferService.put(log);
        eventSendService.logNotification(storedLog);
    }
}
