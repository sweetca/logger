package com.logger.model;

import java.io.RandomAccessFile;

public class LogConnector {
    public Integer id;
    public String fileName;
    public String shortName;
    public long lastSeek;
    public RandomAccessFile raf;

    @Override
    public LogConnector clone() {
        LogConnector connector = new LogConnector();
        connector.id = new Integer(this.id);
        connector.fileName = new String(this.fileName);
        connector.shortName = new String(this.shortName);
        return connector;
    }
}
