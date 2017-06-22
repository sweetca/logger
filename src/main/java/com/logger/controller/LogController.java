package com.logger.controller;

import com.logger.model.Log;
import com.logger.model.LogConnector;
import com.logger.model.LogsRequest;
import com.logger.service.ILogService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import java.util.List;

@Controller
public class LogController {

    private ILogService logService;

    public LogController(ILogService logService) {
        this.logService = logService;
    }

    @MessageMapping("/last-logs")
    public void lastLogs(LogsRequest request) throws Exception {
        logService.notifyLastLogsPeriod(request);
    }

    @MessageMapping("/available-logs")
    @SendTo("/topic/available-logs")
    public List<LogConnector> logMap() {
        return logService.getWatchData();
    }

    @MessageMapping("/new-log")
    public void newLog(Log log) throws Exception {
        logService.storeLog(log, log.getId());
    }

}
