package dev.wsousa.controller;

import dev.wsousa.dto.Prompt;
import dev.wsousa.service.BedrockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/knowledges")
public class BedrockController {

    @Autowired
    private BedrockService bedrockService;

    @GetMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<Prompt> getData(@RequestBody Prompt prompt) {
        bedrockService.converse(prompt.getPromptString());
        return new ResponseEntity<>(prompt, HttpStatus.CREATED);
    }

}
