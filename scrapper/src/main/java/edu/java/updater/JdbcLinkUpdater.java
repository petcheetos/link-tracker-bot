package edu.java.updater;

import edu.java.clients.BotClient;
import edu.java.clients.GitHubClient;
import edu.java.clients.StackoverflowClient;
import edu.java.dto.GitHubResponse;
import edu.java.dto.LinkDTO;
import edu.java.dto.StackoverflowResponse;
import edu.java.models.LinkUpdateRequest;
import edu.java.repository.LinkRepository;
import edu.java.utils.LinkProcessor;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class JdbcLinkUpdater implements LinkUpdater {
    private static final Logger LOGGER = LogManager.getLogger();

    private final LinkRepository linkRepository;
    private final BotClient botClient;
    private final LinkProcessor linkProcessor;
    private final GitHubClient gitHubClient;
    private final StackoverflowClient stackoverflowClient;

    @Override
    @Transactional
    public void update() {
        LOGGER.info("updated");
        List<LinkDTO> list = linkRepository.getLinksToUpdate();
        list.forEach(linkDTO -> {
            if (linkProcessor.isGithubUrl(linkDTO.url())) {
                processGithubUpdate(linkDTO);
            } else if (linkProcessor.isStackoverflowUrl(linkDTO.url())) {
                processStackoverflowUpdate(linkDTO);
            }
        });
    }

    private void processGithubUpdate(LinkDTO linkDTO) {
        List<String> info = linkProcessor.getUserRepoName(linkDTO.url());
        GitHubResponse response = gitHubClient.getRepositoryInfo(info.getFirst(), info.getLast());
        if (response.updatedAt().isAfter(linkDTO.lastUpdated())
            || response.pushedAt().isAfter(linkDTO.lastUpdated())) {
            pushUpdate(linkDTO);
        } else {
            linkRepository.checkLink(linkDTO);
        }
    }

    private void processStackoverflowUpdate(LinkDTO linkDTO) {
        String idStr = linkProcessor.getQuestionId(linkDTO.url());
        StackoverflowResponse response = stackoverflowClient.getUpdate(idStr);
        List<StackoverflowResponse.ItemResponse> itemResponses = response.items();
        for (var item : itemResponses) {
            OffsetDateTime time = item.lastActivityDate();
            if (time.isAfter(linkDTO.lastUpdated())) {
                pushUpdate(linkDTO);
            } else {
                linkRepository.checkLink(linkDTO);
            }
        }
    }

    private void pushUpdate(LinkDTO linkDTO) {
        linkRepository.updateLink(linkDTO);
        botClient.sendUpdate(new LinkUpdateRequest(linkDTO.id(), linkDTO.url(),
            "\uD83C\uDF3A New update by link: ", linkRepository.getChatIdsForLink(linkDTO.id())
        ));
    }
}
