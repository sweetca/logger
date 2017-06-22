package com.logger.domain;

import com.logger.model.Log;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class LogUtil {
    private static final String pattern = "\\d{2,4}[/|\\-|.]\\d{2}[/|\\-|.]\\d{2,4} \\d{2}:\\d{2}:\\d{2}[.|,]\\d{3} *[A-Z]{3,9}";
    private static final Pattern compiledPattern = Pattern.compile(pattern);
    private static final int pattern_size = 9;

    private static int regExIndex(String text, Integer fromIndex){
        Matcher matcher = compiledPattern.matcher(text);
        if ( ( fromIndex != null && matcher.find(fromIndex) ) || matcher.find()) {
            return matcher.start();
        }
        return -1;
    }

    public static void separateLogs(List<String> result, String data, int startPosition) {
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

    public static Log parseLog(String data) {
        LogType type = detectType(data);
        String[] detected;

        if (type != LogType.UNDEFINED && (detected = data.split(type.name())).length > 1) {
            return new Log(detected[0].trim(), type, detected[1].trim());
        }

        return new Log(LogType.UNDEFINED, data);
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
                .map(Map.Entry::getKey)
                .orElse(LogType.UNDEFINED);
    }
}
