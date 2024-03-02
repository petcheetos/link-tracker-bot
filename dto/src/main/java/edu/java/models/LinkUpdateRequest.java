package edu.java.models;

import jakarta.validation.constraints.NotEmpty;
import java.net.URI;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public record LinkUpdateRequest(long id,
                                @NotNull URI url,
                                @NotEmpty String description,
                                @NotEmpty List<Long> tgChatIds) {
}
