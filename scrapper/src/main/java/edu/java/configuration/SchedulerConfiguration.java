package edu.java.configuration;

import edu.java.scheduler.LinkUpdaterScheduler;
import edu.java.updater.LinkUpdater;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SchedulerConfiguration {

    private final LinkUpdater linkUpdater;

    public SchedulerConfiguration(LinkUpdater linkUpdater) {
        this.linkUpdater = linkUpdater;
    }

    @Bean
    public LinkUpdaterScheduler linkUpdaterScheduler() {
        return new LinkUpdaterScheduler(linkUpdater);
    }
}
