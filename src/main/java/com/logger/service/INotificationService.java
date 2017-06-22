package com.logger.service;

import com.logger.model.Log;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.broker.BrokerAvailabilityEvent;
import java.util.List;

public interface INotificationService extends ApplicationListener<BrokerAvailabilityEvent> {

    void logNotification(Log log);
    void lastLogsNotification(List<Log> logs, String user);
}
