package edu.java.dto;

import jakarta.validation.constraints.NotNull;
import java.util.Set;

public record ChatLinkDTO(@NotNull long linkId, @NotNull Set<Long> chatIds) {
}
