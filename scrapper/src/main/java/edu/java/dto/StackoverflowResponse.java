package edu.java.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.List;

public record StackoverflowResponse(List<ItemResponse> items) {
    public record ItemResponse(@JsonProperty("question_id") Long questionId,
                               @JsonProperty("last_activity_date") OffsetDateTime lastActivityDate,
                               @JsonProperty("answer_count") int answerCount) {
    }
}
