package com.logger.model;

import com.logger.domain.Constants;
import com.logger.domain.LogType;
import com.logger.domain.DateUtil;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
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

    public static List<Log> parseLogs(String data) {
        List<String> separateLogs = new ArrayList<>();
        separateLogs(separateLogs, data, 0);

        return separateLogs
                .stream()
                .map(Log::parseLog)
                .collect(Collectors.toList());
    }

    private static final String pattern = "\\d{2,4}[/|\\-|.]\\d{2}[/|\\-|.]\\d{2,4} \\d{2}:\\d{2}:\\d{2}[.|,]\\d* *[A-Z]{0,10}";
    private static final int pattern_size = 9;

    private static int regExIndex(String text, Integer fromIndex){
        Matcher matcher = Pattern.compile(pattern).matcher(text);
        if ( ( fromIndex != null && matcher.find(fromIndex) ) || matcher.find()) {
            return matcher.start();
        }
        return -1;
    }
    private static void separateLogs(List<String> result, String data, int startPosition) {
        int logStart = regExIndex(data, startPosition);
        int logStop = regExIndex(data, startPosition + pattern_size);

        if (logStop < 1) {
            logStop = data.length();
        }

        result.add(data.substring(logStart, logStop));

        if (logStop < data.length()) {
            separateLogs(result, data, logStop);
        }
    }

    private static Log parseLog(String data) {
        LogType type = Log.detectType(data);
        String[] detected;

        if (type != LogType.UNDEFINED && (detected = data.split(type.name())).length > 1) {
            return new Log(detected[0].trim(), type, detected[1].trim());
        }

        return new Log(LogType.UNDEFINED, data);
    }
    private static LogType detectType(String data) {
        Map<LogType, Integer> positionMap = new HashMap<>();
        Stream.of(LogType.values()).forEach(lt -> positionMap.put(lt, data.indexOf(lt.name())));
        return positionMap
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .filter(l -> l.getValue() > 0)
                .findFirst()
                .map(Map.Entry::getKey)
                .orElse(LogType.UNDEFINED);
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
