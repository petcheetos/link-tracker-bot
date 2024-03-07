package edu.java.bot.controllers;

import edu.java.models.LinkUpdateRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/updates")
public class BotController {

    @PostMapping
    public String processUpdate(@RequestBody LinkUpdateRequest linkRequest) {
        //проверка на валидное обновление
        return "Update processed";
    }
}
