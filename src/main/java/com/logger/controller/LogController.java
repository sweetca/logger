package com.logger.controller;

import com.logger.model.Log;
import com.logger.model.LogsRequest;
import com.logger.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class LogController {

    private LogService logService;

    @Autowired
    public LogController(LogService logService) {
        this.logService = logService;
    }

    @MessageMapping("/last-logs")
    public void lastLogs(LogsRequest request) throws Exception {
        logService.notifyLastLogsPeriod(request);
    }

    @MessageMapping("/new-log")
    public void lastLogs(Log log) throws Exception {
        logService.storeLog(log);
    }

}
