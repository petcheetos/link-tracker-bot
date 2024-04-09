package edu.java.services.jdbc;

import edu.java.exception.RequestException;
import edu.java.models.LinkResponse;
import edu.java.repository.LinkRepository;
import edu.java.services.LinkService;
import edu.java.utils.LinkProcessor;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class JdbcLinkService implements LinkService {
    private final LinkRepository linkRepository;
    private final LinkProcessor linkValidator;

    @Override
    @Transactional
    public List<LinkResponse> getLinks(long chatId) {
        return linkRepository.findAllByChat(chatId);
    }

    @Override
    @Transactional
    public LinkResponse addLink(long chatId, URI link) {
        checkUrlIsValid(link);
        return linkRepository.add(chatId, link);
    }

    @Override
    public LinkResponse deleteLink(long chatId, URI link) {
        return linkRepository.remove(chatId, link);
    }

    private void checkUrlIsValid(URI link) {
        if (!linkValidator.isValid(link)) {
            throw new RequestException("Incorrect link");
        }
    }
}
