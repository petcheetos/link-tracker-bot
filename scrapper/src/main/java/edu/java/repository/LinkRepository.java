package edu.java.repository;

import edu.java.dto.LinkDTO;
import edu.java.models.LinkResponse;
import java.net.URI;
import java.util.List;

public interface LinkRepository {

    void add(long chatId, URI link);

    void remove(long chatId, URI link);

    List<LinkResponse> findAll(long chatId);

}
