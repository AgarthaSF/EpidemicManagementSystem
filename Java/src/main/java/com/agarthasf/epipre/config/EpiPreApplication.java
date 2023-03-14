package com.agarthasf.epipre.config;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

@ComponentScan("com.agarthasf")
@SpringBootApplication
@MapperScan("com.agarthasf.epipre.mapper")
public class EpiPreApplication {

    private static final Logger LOG = LoggerFactory.getLogger(
            EpiPreApplication.class);

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(EpiPreApplication.class);
        Environment env = app.run(args).getEnvironment();
        LOG.info("Launch succeed");
        LOG.info("Address: \thttp://127.0.0.1:{}", env.getProperty("server.port"));
    }

}
