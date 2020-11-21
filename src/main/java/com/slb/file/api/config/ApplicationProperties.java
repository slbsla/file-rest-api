package com.slb.file.api.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
@Component
@PropertySource("classpath:application.yml")
@Getter
public class ApplicationProperties {
    @Value("${file.root}")
    private String root;
    @Value("${file.work}")
    private String work;
    @Value("${file.sftp}")
    private String sftp;
    @Value("${file.archive}")
    private String archive;
    @Value("${integration.input}")
    private String input;
    @Value("${integration.pattern}")
    private String pattern;

}
