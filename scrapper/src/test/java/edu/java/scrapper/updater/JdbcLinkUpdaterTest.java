package edu.java.scrapper.updater;

import edu.java.clients.BotClient;
import edu.java.clients.GitHubClient;
import edu.java.clients.StackoverflowClient;
import edu.java.dto.GitHubResponse;
import edu.java.dto.LinkDTO;
import edu.java.dto.StackoverflowResponse;
import edu.java.repository.LinkRepository;
import edu.java.updater.JdbcLinkUpdater;
import edu.java.utils.LinkProcessor;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class JdbcLinkUpdaterTest {

    @Mock private LinkRepository linkRepository;
    @Mock private BotClient botClient;
    @Mock private LinkProcessor linkProcessor;
    @Mock private GitHubClient gitHubClient;
    @Mock private StackoverflowClient stackoverflowClient;
    @InjectMocks private JdbcLinkUpdater jdbcLinkUpdater;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUpdateWithGitHubLink() {
        LinkDTO linkDTO = new LinkDTO(1L, URI.create("http://github.com/user/repo"), OffsetDateTime.now().minusDays(1));
        when(linkRepository.getLinksToUpdate()).thenReturn(List.of(linkDTO));
        when(linkProcessor.isGithubUrl(linkDTO.url())).thenReturn(true);
        when(linkProcessor.getUserRepoName(linkDTO.url())).thenReturn(Arrays.asList("user", "repo"));
        GitHubResponse githubResponse = mock(GitHubResponse.class);
        when(githubResponse.updatedAt()).thenReturn(OffsetDateTime.now());
        when(githubResponse.pushedAt()).thenReturn(OffsetDateTime.now());
        when(gitHubClient.getRepositoryInfo("user", "repo")).thenReturn(githubResponse);

        jdbcLinkUpdater.update();

        verify(linkRepository).updateLink(any(LinkDTO.class));
        verify(botClient).sendUpdate(any());
    }

    @Test
    public void testUpdateWithStackoverflowLink() {
        LinkDTO linkDTO =
            new LinkDTO(1L, URI.create("http://stackoverflow.com/questions/123"), OffsetDateTime.now().minusDays(1));
        when(linkRepository.getLinksToUpdate()).thenReturn(List.of(linkDTO));
        when(linkProcessor.isStackoverflowUrl(linkDTO.url())).thenReturn(true);
        when(linkProcessor.getQuestionId(linkDTO.url())).thenReturn("123");
        StackoverflowResponse response = mock(StackoverflowResponse.class);
        StackoverflowResponse.ItemResponse itemResponse =
            new StackoverflowResponse.ItemResponse(1L, OffsetDateTime.now(), 1);
        when(response.items()).thenReturn(List.of(itemResponse));
        when(stackoverflowClient.getUpdate("123")).thenReturn(response);

        jdbcLinkUpdater.update();

        verify(linkRepository).updateLink(any(LinkDTO.class));
        verify(botClient).sendUpdate(any());
    }
}
