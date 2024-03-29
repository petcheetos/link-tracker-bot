package edu.java.updater;

import edu.java.clients.BotClient;
import edu.java.clients.GitHubClient;
import edu.java.clients.StackoverflowClient;
import edu.java.dto.GitHubResponse;
import edu.java.dto.StackoverflowResponse;
import edu.java.dto.entity.Link;
import edu.java.models.LinkUpdateRequest;
import edu.java.repository.jpa.JpaLinkRepository;
import edu.java.utils.LinkProcessor;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JpaLinkUpdater implements LinkUpdater {
    private final int minutes = 10;
    private final JpaLinkRepository linkRepository;
    private final BotClient botClient;
    private final LinkProcessor linkProcessor;
    private final GitHubClient gitHubClient;
    private final StackoverflowClient stackoverflowClient;

    @Override
    public void update() {
        List<Link> list = linkRepository.getLinksToUpdate(OffsetDateTime.now().minusMinutes(minutes));
        list.forEach(link -> {
            if (linkProcessor.isGithubUrl(URI.create(link.getUrl()))) {
                List<String> info = linkProcessor.getUserRepoName(URI.create(link.getUrl()));
                GitHubResponse response = gitHubClient.getRepositoryInfo(info.getFirst(), info.getLast());
                if (response.updatedAt().isAfter(link.getLastUpdated())
                    || response.pushedAt().isAfter(link.getLastUpdated())) {
                    linkRepository.updateLastUpdatedAt(link.getUrl(), OffsetDateTime.now());
                    botClient.sendUpdate(new LinkUpdateRequest(link.getId(), URI.create(link.getUrl()),
                            "\uD83C\uDF3A New update by link: ", linkRepository.findChatIdsByLink(link)
                        )
                    );
                }
                linkRepository.updateCheckedAt(link.getUrl(), OffsetDateTime.now());
            } else if (linkProcessor.isStackoverflowUrl(URI.create(link.getUrl()))) {
                String idStr = linkProcessor.getQuestionId(URI.create(link.getUrl()));
                StackoverflowResponse response = stackoverflowClient.getUpdate(idStr);
                List<StackoverflowResponse.ItemResponse> itemResponses = response.items();
                for (var item : itemResponses) {
                    OffsetDateTime time = item.lastActivityDate();
                    if (time.isAfter(link.getLastUpdated())) {
                        linkRepository.updateLastUpdatedAt(link.getUrl(), OffsetDateTime.now());
                        botClient.sendUpdate(new LinkUpdateRequest(link.getId(), URI.create(link.getUrl()),
                            "\uD83C\uDF80 New update by link: ", linkRepository.findChatIdsByLink(link)
                        ));
                    }
                    linkRepository.updateCheckedAt(link.getUrl(), OffsetDateTime.now());
                }
            }
        });
    }
}
