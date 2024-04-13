package service;

import edu.java.bot.clients.ScrapperClient;
import edu.java.bot.services.ClientService;
import edu.java.models.AddLinkRequest;
import edu.java.models.LinkResponse;
import edu.java.models.ListLinkResponse;
import edu.java.models.RemoveLinkRequest;
import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ClientServiceTest {

    @Mock
    private ScrapperClient scrapperClient;

    @InjectMocks
    private ClientService clientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterChat() {
        long chatId = 1L;
        clientService.registerChat(chatId);
        verify(scrapperClient).registerChat(chatId);
    }

    @Test
    void testGetLinks() {
        long chatId = 1L;
        ListLinkResponse expectedResponse =
            new ListLinkResponse(List.of(new LinkResponse(1L, URI.create("http://github.com"))), 1);
        when(scrapperClient.getLinks(chatId)).thenReturn(expectedResponse);
        var actualResponse = clientService.getLinks(chatId);
        assertSame(expectedResponse, actualResponse);
        verify(scrapperClient).getLinks(chatId);
    }

    @Test
    void testAddLink() {
        long chatId = 1L;
        AddLinkRequest request = new AddLinkRequest(URI.create("http://github.com"));
        LinkResponse expectedResponse = new LinkResponse(1L, URI.create("http://github.com"));
        when(scrapperClient.addLink(chatId, request)).thenReturn(expectedResponse);
        var actualResponse = clientService.addLink(chatId, request);
        assertSame(expectedResponse, actualResponse);
        verify(scrapperClient).addLink(chatId, request);
    }

    @Test
    void testRemoveLink() {
        long chatId = 1L;
        RemoveLinkRequest request = new RemoveLinkRequest(URI.create("http://github.com"));
        LinkResponse expectedResponse = new LinkResponse(1L, URI.create("http://github.com"));
        when(scrapperClient.deleteLink(chatId, request)).thenReturn(expectedResponse);
        var actualResponse = clientService.removeLink(chatId, request);
        assertSame(expectedResponse, actualResponse);
        verify(scrapperClient).deleteLink(chatId, request);
    }
}
