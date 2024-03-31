package edu.java.retry_model;

import java.util.List;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class RetryPolicyData {
    private RetryPolicy retryPolicy;
    private int attempts;
    private List<HttpStatus> httpStatuses;
}
