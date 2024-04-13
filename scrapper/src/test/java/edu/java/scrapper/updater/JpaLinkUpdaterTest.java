package edu.java.scrapper.updater;

import edu.java.clients.BotClient;
import edu.java.clients.GitHubClient;
import edu.java.clients.StackoverflowClient;
import edu.java.dto.GitHubResponse;
import edu.java.dto.StackoverflowResponse;
import edu.java.dto.entity.Link;
import edu.java.repository.jpa.JpaLinkRepository;
import edu.java.updater.JpaLinkUpdater;
import edu.java.utils.LinkProcessor;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class JpaLinkUpdaterTest {

    @Mock private JpaLinkRepository linkRepository;
    @Mock private BotClient botClient;
    @Mock private LinkProcessor linkProcessor;
    @Mock private GitHubClient gitHubClient;
    @Mock private StackoverflowClient stackoverflowClient;
    @InjectMocks private JpaLinkUpdater jpaLinkUpdater;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUpdateWithNoLinks() {
        when(linkRepository.getLinksToUpdate(any())).thenReturn(new ArrayList<>());
        jpaLinkUpdater.update();
        verify(linkRepository, never()).updateLastUpdatedAt(any(), any());
    }

    @Test
    public void testUpdateWithGitHubLink() {
        Link link = new Link(
            1L,
            "http://github.com/user/repo",
            OffsetDateTime.now().minusDays(1),
            OffsetDateTime.now().minusDays(1),
            new HashSet<>()
        );
        List<Link> links = List.of(link);
        when(linkRepository.getLinksToUpdate(any())).thenReturn(links);
        when(linkProcessor.isGithubUrl(any())).thenReturn(true);
        when(linkProcessor.getUserRepoName(any())).thenReturn(List.of("user", "repo"));
        GitHubResponse gitHubResponse = mock(GitHubResponse.class);
        when(gitHubClient.getRepositoryInfo("user", "repo")).thenReturn(gitHubResponse);
        when(gitHubResponse.updatedAt()).thenReturn(OffsetDateTime.now());
        when(gitHubResponse.pushedAt()).thenReturn(OffsetDateTime.now());
        jpaLinkUpdater.update();
        verify(linkRepository).updateCheckedAt(anyString(), any());
        verify(botClient).sendUpdate(any());
    }

    @Test
    public void testProcessStackoverflowUpdate() {
        Link link = new Link(
            1L,
            "http://stackoverflow.com/questions/123",
            OffsetDateTime.now().minusDays(1),
            OffsetDateTime.now().minusDays(1),
            new HashSet<>()
        );
        List<Link> links = List.of(link);
        when(linkRepository.getLinksToUpdate(any())).thenReturn(links);
        when(linkProcessor.isStackoverflowUrl(URI.create(link.getUrl()))).thenReturn(true);
        when(linkProcessor.getQuestionId(URI.create(link.getUrl()))).thenReturn("123");
        StackoverflowResponse response = mock(StackoverflowResponse.class);
        StackoverflowResponse.ItemResponse itemResponse =
            new StackoverflowResponse.ItemResponse(1L, OffsetDateTime.now().plusHours(1), 1);
        when(response.items()).thenReturn(List.of(itemResponse));
        when(stackoverflowClient.getUpdate("123")).thenReturn(response);
        jpaLinkUpdater.update();
        verify(linkRepository).updateCheckedAt(eq(link.getUrl()), any(OffsetDateTime.class));
        verify(botClient).sendUpdate(any());
    }
}
