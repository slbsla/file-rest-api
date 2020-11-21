package com.slb.file.api.config;

import com.slb.file.api.listner.MyFileChangeListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.devtools.filewatch.FileSystemWatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.config.EnableIntegration;

import javax.annotation.PreDestroy;
import java.io.File;
import java.time.Duration;

@Configuration
@EnableIntegration
public class FileIntegrationConfig {

    @Autowired
    ApplicationProperties applicationProperties ;

    @Bean
    public FileSystemWatcher watcherService() {
        FileSystemWatcher fileSystemWatcher = new FileSystemWatcher(true, Duration.ofMillis(5000L), Duration.ofMillis(3000L));
        fileSystemWatcher.addSourceFolder(new File(applicationProperties.getInput()));
        fileSystemWatcher.addListener(new MyFileChangeListener());
        fileSystemWatcher.start();
        System.out.println("started fileSystemWatcher");
        return fileSystemWatcher;
    }
    @PreDestroy
    public void onDestroy() throws Exception {
        watcherService().stop();
    }
}
