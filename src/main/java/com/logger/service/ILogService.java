package com.logger.service;

import com.logger.model.Log;
import com.logger.model.LogConnector;
import com.logger.model.LogsRequest;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public interface ILogService {

    void notifyLastLogsPeriod(LogsRequest request);
    void storeLog(List<Log> logs,final Integer dataId);
    void storeLog(Log log,final Integer dataId) ;
    void setWatchData(ConcurrentHashMap<Integer, LogConnector> watchData);
    List<LogConnector> getWatchData();
}
