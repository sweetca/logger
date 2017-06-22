package com.logger.model;

import com.logger.domain.Constants;
import com.logger.domain.LogType;
import com.logger.domain.DateUtil;
import com.logger.domain.LogUtil;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

public class Log {
    private Integer id;
    private LogType logType;
    private String msg;
    private Long timestamp;

    public Log() {
    }
    public Log(LogType logType, String message) {
        this.logType = logType;
        this.msg = message;
    }
    public Log(String logType, String message) {
        this.logType = LogType.valueOf(logType);
        this.msg = message;
    }
    public Log(String time, LogType logType, String message) {
        this.logType = logType;
        this.msg = message;

        try {
            this.timestamp = DateUtil.detectDate(time);
        } catch (ParseException e) {
            this.logType = LogType.UNDEFINED;
            this.timestamp = new Date().getTime();
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogType() {
        return this.logType == null ? null : this.logType.name();
    }

    public String  getDate() {
        return this.timestamp == null ? null : Constants.DATE_FORMAT_TIME.format(new Date(this.timestamp));
    }

    public String getMsg() {
        return msg;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public static List<Log> parseLogs(String data) {
        List<String> separateLogs = new ArrayList<>();
        LogUtil.separateLogs(separateLogs, data, 0);

        return separateLogs
                .stream()
                .map(LogUtil::parseLog)
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "id: " + id + " type: " + logType.name() + " msg: " + msg + " t: " + timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Log) {
            Log log = (Log)o;
            return new EqualsBuilder()
                    .append(this.getId(), log.getId())
                    .append(this.getMsg(), log.getMsg())
                    .append(this.getTimestamp(), log.getTimestamp())
                    .append(this.getLogType(), log.getLogType())
                    .isEquals();
        }

        return false;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(this.getId())
                .append(this.getMsg())
                .append(this.getTimestamp())
                .append(this.getLogType())
                .toHashCode();
    }
}
