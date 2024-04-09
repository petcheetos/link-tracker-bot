package edu.java.dto;

import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.time.OffsetDateTime;

public record LinkDTO(@NotNull long id, @NotNull URI url, OffsetDateTime lastUpdated) {
}
