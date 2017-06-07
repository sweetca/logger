package com.logger.model;

import com.logger.domain.LogType;

/**
 * @author askorokhod@guidewire.com
 */
public class LogsRequest {
    LogType logType;
    Integer period;
    String user;

    public LogsRequest() {}

    public LogType getLogType() {
        return logType;
    }

    public void setLogType(LogType logType) {
        this.logType = logType;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
