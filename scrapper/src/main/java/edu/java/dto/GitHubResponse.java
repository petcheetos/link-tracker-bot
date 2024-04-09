package edu.java.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;

public record GitHubResponse(@NotNull String login,
                             @NotNull Long id,
                             @JsonProperty("created_at") OffsetDateTime createdAt,
                             @JsonProperty("pushed_at") OffsetDateTime pushedAt,
                             @JsonProperty("updated_at") OffsetDateTime updatedAt) {
}
