package com.logger.service;

import com.logger.domain.Constants;
import com.logger.domain.LogType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@PropertySource("classpath:application.properties")
@ConditionalOnExpression("${rw.enabled:true}")
public class RwFile {

    private final AtomicInteger step = new AtomicInteger(0);
    private List<File> files;

    @Value("${files.to.watch}")
    private String fileNames;
    @Value("${logging.file}")
    private String appLog;

    @PostConstruct
    public void init() {
        files = Stream.of(fileNames.split(","))
                .filter(name -> !name.endsWith(appLog))
                .map(File::new)
                .collect(Collectors.toList());
    }

    @Async
    @Scheduled(initialDelay=2000, fixedDelay=50)
    public void writeFile() {
        files.forEach(file -> {
            try {
                String content = " " + file.getName() + " rw_: " + step.incrementAndGet() + System.getProperty("line.separator");
                content = Constants.DATE_FORMAT_TIME.format(new Date()) + " " + this.getRandomType() + content;
                Files.write(file.toPath(), content.getBytes(), StandardOpenOption.APPEND);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private String getRandomType() {
        Random ran = new Random();
        int x = ran.nextInt(LogType.values().length - 1);
        LogType type = LogType.values()[x];
        return type.name();
    }
}
