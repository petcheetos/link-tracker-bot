package edu.java.bot.retry_model;

import java.util.List;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ScrapperRetryPolicyData {
    private ScrapperRetryPolicy retryPolicy;
    private int attempts;
    private List<HttpStatus> httpStatuses;
}
