package com.logger.service;

import com.logger.model.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.broker.BrokerAvailabilityEvent;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class EventSendService implements ApplicationListener<BrokerAvailabilityEvent> {

    private SimpMessagingTemplate messagingTemplate;
    private AtomicBoolean brokerAvailable;

    @Autowired
    public EventSendService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
        brokerAvailable = new AtomicBoolean();
    }

    @Override
    public void onApplicationEvent(BrokerAvailabilityEvent event) {
        this.brokerAvailable.set(event.isBrokerAvailable());
    }

    public void logNotification(Log log){
        this.messagingTemplate.convertAndSend("/topic/log", log);
    }

    public void lastLogsNotification(List<Log> logs, String user){
        this.messagingTemplate.convertAndSend("/topic/last-logs-" + user, logs);
    }
}
