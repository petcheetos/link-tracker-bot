package edu.java.scrapper.services;

import edu.java.exception.LinkNotFoundException;
import edu.java.exception.RequestException;
import edu.java.services.ChatService;
import java.net.URI;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ChatServiceTest {

    @Test
    void testRegisterChatTwiceThrowsException() {
        ChatService chatService = new ChatService();
        chatService.registerChat(1L);
        assertThrows(RequestException.class, () -> chatService.registerChat(1L));
    }

    @Test
    void testDeleteChat() {
        ChatService chatService = new ChatService();
        chatService.registerChat(1L);
        assertDoesNotThrow(() -> chatService.deleteChat(1L));
    }

    @Test
    void testDeleteChatWithNoChat() {
        ChatService chatService = new ChatService();
        assertThrows(RequestException.class, () -> chatService.deleteChat(1L));
    }

    @Test
    public void testGetLinks() {
        ChatService chatService = new ChatService();
        chatService.registerChat(1L);
        chatService.addLink(1L, URI.create("1"));
        assertDoesNotThrow(() -> chatService.getLinks(1L));
    }

    @Test
    public void testGetLinksWithNoChat() {
        ChatService chatService = new ChatService();
        assertThrows(RequestException.class, () -> chatService.getLinks(1L));
    }

    @Test
    void testAddLink() {
        ChatService chatService = new ChatService();
        chatService.registerChat(1L);
        assertDoesNotThrow(() -> chatService.addLink(1L, URI.create("1")));
    }

    @Test
    public void testAddLinkWithNoChat() {
        ChatService chatService = new ChatService();
        assertThrows(RequestException.class, () -> chatService.addLink(1L, URI.create("1")));
    }

    @Test
    public void testDeleteLink() {
        ChatService chatService = new ChatService();
        chatService.registerChat(1L);
        chatService.addLink(1L, URI.create("1"));
        assertDoesNotThrow(() -> chatService.deleteLink(1L, URI.create("1")));
    }

    @Test
    public void testDeleteLinkWithNoChat() {
        ChatService chatService = new ChatService();
        assertThrows(RequestException.class, () -> chatService.deleteLink(1L, URI.create("1")));
    }

    @Test
    public void testDeleteLinkWithNoLink() {
        ChatService chatService = new ChatService();
        chatService.registerChat(1L);
        chatService.addLink(1L, URI.create("1"));
        assertThrows(LinkNotFoundException.class, () -> chatService.deleteLink(1L, URI.create("2")));
    }
}
