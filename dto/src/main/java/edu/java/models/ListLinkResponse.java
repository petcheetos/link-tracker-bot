package edu.java.models;

import java.util.List;

public record ListLinkResponse(List<LinkResponse> links,
                               int size) {
}
