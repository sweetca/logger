package com.logger.service;

import com.logger.model.Log;
import java.util.Enumeration;
import java.util.List;

public interface IBufferService {

    List<Log> getLastPeriod(Integer requestedInterval, Integer dataID);
    Log put(Log entry, Integer dataID);
    void setLogsData(Enumeration<Integer> ids);
}
