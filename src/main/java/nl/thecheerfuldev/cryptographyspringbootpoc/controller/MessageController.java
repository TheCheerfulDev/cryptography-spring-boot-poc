package nl.thecheerfuldev.cryptographyspringbootpoc.controller;

import nl.thecheerfuldev.cryptographyspringbootpoc.repository.MessageRepository;
import nl.thecheerfuldev.cryptographyspringbootpoc.service.CryptographyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final CryptographyService cryptographyService;
    private final MessageRepository messageRepository;

    public MessageController(CryptographyService cryptographyService, MessageRepository messageRepository) {
        this.cryptographyService = cryptographyService;
        this.messageRepository = messageRepository;
    }

    @GetMapping("/{userId}")
    public List<String> getAllMessagesForUserId(@PathVariable("userId") final int userId) {
        final List<String> result = new ArrayList<>();

        List<String> messages = this.messageRepository.getMessagesForUserId(userId);

        for (String message : messages) {
            result.add(this.cryptographyService.decryptTextForUserId(message, userId));
        }
        return List.copyOf(result);
    }

    @PostMapping("/{userId}")
    public ResponseEntity<Void> postMessageForUserId(@PathVariable("userId") final int userId, @RequestBody final String message) {
        this.messageRepository.putMessageForUserId(userId, this.cryptographyService.encryptTextForUserId(message, userId));
        return ResponseEntity.accepted().build();
    }

}
