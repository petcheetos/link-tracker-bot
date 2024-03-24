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

@RequiredArgsConstructor
public class JdbcLinkUpdater implements LinkUpdater {
    private final LinkRepository linkRepository;
    private final BotClient botClient;
    private final LinkProcessor linkProcessor;
    private final GitHubClient gitHubClient;
    private final StackoverflowClient stackoverflowClient;

    @Override
    public void update() {
        List<LinkDTO> list = linkRepository.getLinksToUpdate();
        list.forEach(linkDTO -> {
            if (linkProcessor.isGithubUrl(linkDTO.url())) {
                List<String> info = linkProcessor.getUserRepoName(linkDTO.url());
                GitHubResponse response = gitHubClient.getRepositoryInfo(info.getFirst(), info.getLast());
                if (response.updatedAt().isAfter(linkDTO.lastUpdated())
                    || response.pushedAt().isAfter(linkDTO.lastUpdated())) {
                    linkRepository.updateLink(linkDTO);
                    botClient.sendUpdate(new LinkUpdateRequest(linkDTO.id(), linkDTO.url(),
                        "Github link update", linkRepository.getChatIdsForLink(linkDTO.id())
                    ));
                } else {
                    linkRepository.checkLink(linkDTO);
                }
            } else if (linkProcessor.isStackoverflowUrl(linkDTO.url())) {
                String idStr = linkProcessor.getQuestionId(linkDTO.url());
                StackoverflowResponse response = stackoverflowClient.getUpdate(idStr);
                List<StackoverflowResponse.ItemResponse> itemResponses = response.items();
                for (var item : itemResponses) {
                    OffsetDateTime time = item.lastActivityDate();
                    if (time.isAfter(linkDTO.lastUpdated())) {
                        linkRepository.updateLink(linkDTO);
                        botClient.sendUpdate(new LinkUpdateRequest(linkDTO.id(), linkDTO.url(),
                            "Stackoverflow link update", linkRepository.getChatIdsForLink(linkDTO.id())
                        ));
                    } else {
                        linkRepository.checkLink(linkDTO);
                    }
                }
            }
        });
    }
}
