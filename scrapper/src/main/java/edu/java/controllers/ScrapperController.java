package edu.java.controllers;

import edu.java.models.AddLinkRequest;
import edu.java.models.LinkResponse;
import edu.java.models.ListLinkResponse;
import edu.java.models.RemoveLinkRequest;
import edu.java.services.ChatService;
import edu.java.services.LinkService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ScrapperController {
    private final ChatService chatService;
    private final LinkService linkService;

    @PostMapping("/tg-chat/{id}")
    public String registerChat(@PathVariable("id") long id) {
        chatService.registerChat(id);
        return "Chat registered";
    }

    @DeleteMapping("/tg-chat/{id}")
    public String deleteChat(@PathVariable("id") long id) {
        chatService.deleteChat(id);
        return "Chat deleted";
    }

    @GetMapping("/links")
    public ListLinkResponse getLinks(@RequestHeader("Tg-Chat-Id") long chatId) {
        List<LinkResponse> links = linkService.getLinks(chatId);
        return new ListLinkResponse(links, links.size());
    }

    @PostMapping("/links")
    public LinkResponse addLink(@RequestHeader("Tg-Chat-Id") long chatId, @RequestBody AddLinkRequest request) {
        return linkService.addLink(chatId, request.link());
    }

    @DeleteMapping("/links")
    public LinkResponse deleteLink(@RequestHeader("Tg-Chat-Id") long chatId, @RequestBody RemoveLinkRequest request) {
        return linkService.deleteLink(chatId, request.link());
    }
}
