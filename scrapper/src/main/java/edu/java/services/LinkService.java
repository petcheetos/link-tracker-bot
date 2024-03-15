package edu.java.services;

import edu.java.models.LinkResponse;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.util.List;

@Service
public interface LinkService {

    List<LinkResponse> getLinks(long chatId);

    LinkResponse addLink(long chatId, URI link);

    LinkResponse deleteLink(long chatId, URI link);

}

