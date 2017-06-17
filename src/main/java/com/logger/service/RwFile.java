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
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@PropertySource("classpath:application.properties")
@ConditionalOnExpression("${rw.enabled:true}")
public class RwFile {

    int tact = 0;
    private List<File> files;

    @Value("${files.to.watch}")
    private String fileNames;

    @PostConstruct
    public void init() {
        String[] filesPath = fileNames.trim().split(",");
        files = Arrays.stream(filesPath).map(path -> new File(path)).collect(Collectors.toList());
    }

    @Async
    @Scheduled(initialDelay=2000, fixedDelay=50)
    public void writeFile() {
        files.forEach(file -> {
            try {
                tact++;
                String content = file.getName() + " rw_:" + tact + "\n";
                content = Constants.DATE_FORMAT_TIME.format(new Date()) + " " + this.getRandomType() + content;
                Files.write(file.toPath(), content.getBytes(), StandardOpenOption.APPEND);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private String getRandomType() {
        Random ran = new Random();
        int x = ran.nextInt(3);
        LogType type = LogType.INFO;
        switch (x) {
            case 2:
                type = LogType.WARNING;
                break;
            case 1:
                type = LogType.ERROR;
                break;
        }

        return type.name();
    }
}
