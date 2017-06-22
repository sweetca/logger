package com.logger.service;

import com.logger.model.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.broker.BrokerAvailabilityEvent;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class NotificationService implements INotificationService {

    private final SimpMessagingTemplate messagingTemplate;
    private final AtomicBoolean brokerAvailable;

    @Autowired
    public NotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
        brokerAvailable = new AtomicBoolean();
    }

    @Override
    public void onApplicationEvent(BrokerAvailabilityEvent event) {
        this.brokerAvailable.set(event.isBrokerAvailable());
    }

    @Override
    public void logNotification(Log log){
        this.messagingTemplate.convertAndSend("/topic/log", log);
    }

    @Override
    public void lastLogsNotification(List<Log> logs, String user){
        this.messagingTemplate.convertAndSend("/topic/last-logs-" + user, logs);
    }
}
