package edu.java.scheduler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class LinkUpdaterScheduler {
    private final static Logger LOGGER = LogManager.getLogger();

    @Scheduled(fixedDelayString = "#{@scheduler.interval()}")
    private void update() {
        LOGGER.info("update");
    }
}
