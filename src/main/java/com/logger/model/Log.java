package com.logger.model;

import com.logger.domain.Constants;
import com.logger.domain.LogType;
import java.text.ParseException;
import java.util.Date;

public class Log {
    private LogType logType;
    private String msg;
    public Long timestamp;

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
            timestamp = Constants.DATE_FORMAT_TIME.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
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

    public static Log parseLog(String data) {
        String[] detected;

        if ((detected = data.split(LogType.INFO.name())).length > 1) {
            return new Log(detected[0].trim(), LogType.INFO, detected[1].trim());
        } else if ((detected = data.split(LogType.WARNING.name())).length > 1) {
            return new Log(detected[0].trim(), LogType.WARNING, detected[1].trim());
        } else if ((detected = data.split(LogType.ERROR.name())).length > 1) {
            return new Log(detected[0].trim(), LogType.ERROR, detected[1].trim());
        }

        return new Log(LogType.BROKEN, data);
    }
}
