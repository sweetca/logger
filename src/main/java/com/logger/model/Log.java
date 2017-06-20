package com.logger.model;

import com.logger.domain.Constants;
import com.logger.domain.LogType;
import com.logger.domain.DateUtil;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Stream;

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

    public static LogType detectType(String data) {
        Map<LogType, Integer> positionMap = new HashMap<>();
        Stream.of(LogType.values()).forEach(lt -> positionMap.put(lt, data.indexOf(lt.name())));
        return positionMap
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .filter(l -> l.getValue() > 0)
                .findFirst()
                .get()
                .getKey();
    }

    public static Log parseLog(String data) {
        LogType type = Log.detectType(data);
        String[] detected;

        if ((detected = data.split(type.name())).length > 1) {
            return new Log(detected[0].trim(), type, detected[1].trim());
        }

        return new Log(LogType.UNDEFINED, data);
    }

    public static List<Log> parseLogs(String data) {
        List<Log> result = new ArrayList<>();
        Stream.of(data.split("\\n")).forEach(str -> result.add(Log.parseLog(str)));
        return result;
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
