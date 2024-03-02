package edu.java.models;

import jakarta.validation.constraints.NotNull;
import java.net.URI;

public record LinkResponse(@NotNull long id, @NotNull URI url) {
}
