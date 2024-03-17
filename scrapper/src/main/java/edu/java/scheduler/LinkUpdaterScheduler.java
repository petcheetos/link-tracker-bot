package edu.java.scheduler;

import edu.java.clients.BotClient;
import edu.java.clients.GitHubClient;
import edu.java.clients.StackoverflowClient;
import edu.java.dto.ChatLinkDTO;
import edu.java.dto.GitHubResponse;
import edu.java.dto.LinkDTO;
import edu.java.dto.LinkProcessor;
import edu.java.dto.StackoverflowResponse;
import edu.java.models.LinkUpdateRequest;
import edu.java.repository.ChatLinkRepository;
import edu.java.repository.LinkRepository;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class LinkUpdaterScheduler {
    private static final Logger LOGGER = LogManager.getLogger();

    @Value("${spring.database.check-minutes}")
    private int minutesCheck;
    private final ChatLinkRepository chatLinkRepository;
    private final LinkRepository linkRepository;
    private final BotClient botClient;
    private final LinkProcessor linkProcessor;
    private final GitHubClient gitHubClient;
    private final StackoverflowClient stackoverflowClient;

    @Scheduled(fixedDelayString = "${app.scheduler.interval}")
    @Transactional
    @SuppressWarnings("MultipleStringLiterals")
    public void update() {
        LOGGER.info("updated");
        OffsetDateTime time = OffsetDateTime.now();
        time = time.minusMinutes(minutesCheck);
        List<ChatLinkDTO> list = chatLinkRepository.findAllByTime(time);
        for (ChatLinkDTO chatLinkDTO : list) {
            long linkId = chatLinkDTO.linkId();
            LinkDTO linkDTO = linkRepository.getLinkDTO(linkId);
            LinkUpdateRequest linkUpdateRequest = new LinkUpdateRequest(
                linkId,
                linkDTO.url(),
                "updated",
                chatLinkDTO.chatIds().stream().toList()
            );
            if (linkProcessor.isGithubUrl(linkDTO.url())) {
                List<String> arr = linkProcessor.getUserRepoName(linkDTO.url());
                GitHubResponse response = gitHubClient.getRepositoryInfo(arr.getFirst(), arr.getLast());
                if (response.updatedAt().isAfter(linkDTO.lastUpdated())
                    || response.pushedAt().isAfter(linkDTO.lastUpdated())) {
                    //учесть pushedAt
                    linkRepository.updateLink(new LinkDTO(linkId, linkDTO.url(), response.updatedAt()));
                    botClient.sendUpdate(linkUpdateRequest);
                }
            } else if (linkProcessor.isStackoverflowUrl(linkDTO.url())) {
                StackoverflowResponse response =
                    stackoverflowClient.getUpdate(linkProcessor.getQuestionId(linkDTO.url()));
                List<StackoverflowResponse.ItemResponse> itemResponses = response.items();
                for (StackoverflowResponse.ItemResponse itemResponse : itemResponses) {
                    OffsetDateTime dateTime = itemResponse.lastActivityDate();
                    if (dateTime.isAfter(linkDTO.lastUpdated())) {
                        linkRepository.updateLink(new LinkDTO(linkId, linkDTO.url(), dateTime));
                    }
                }
            }
        }
    }
}
