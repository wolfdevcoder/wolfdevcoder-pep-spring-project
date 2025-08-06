package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

@RestController
@RequestMapping("/")
public class SocialMediaController {
    private final AccountService accountService;
    private final MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    // account registeration end-point
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Account account) {
        try {
            Account saved = accountService.register(account);
            return ResponseEntity.ok(saved);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // account login end-point
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Account account) {
        try {
            Account loggedIn = accountService.login(account);
            return ResponseEntity.ok(loggedIn);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password.");
        }
    }

    //post message end-point
    @PostMapping("/messages")
    public ResponseEntity<?> createMessage(@RequestBody Message message) {
        try {
            Message saved = messageService.createMessage(message);
            return ResponseEntity.ok(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // get all messages end-point
    @GetMapping("/messages")
    public List<Message> getAllMessages() {
        return messageService.getAllMessages();
    }

    // get message data from id
    @GetMapping("/messages/{id}")
    public ResponseEntity<?> getMessageById(@PathVariable Integer id) {
        Optional<Message> message = messageService.getMessageById(id);
        return message.<ResponseEntity<?>>map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.ok().build());
    }

    // delete message by id
    @DeleteMapping("/messages/{id}")
    public ResponseEntity<?> deleteMessage(@PathVariable Integer id) {
        boolean deleted = messageService.deleteMessage(id);
        if (deleted) {
            return ResponseEntity.ok(1);
        } else {
            return ResponseEntity.ok().build();
        }
    }

    // update message
    @PatchMapping("/messages/{id}")
    public ResponseEntity<?> updateMessageText(@PathVariable Integer id, @RequestBody Map<String, String> updates) {
        String newText = updates.get("messageText");
        if (newText == null) {
            return ResponseEntity.badRequest().body("Missing messageText field.");
        }
        try {
            boolean updated = messageService.updateMessageText(id, newText);
            if (updated) {
                return ResponseEntity.ok(1);
            } else {
                return ResponseEntity.badRequest().body("Message with id " + id + " not found.");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //returns all messages for a user
    @GetMapping("/accounts/{accountId}/messages")
    public List<Message> getMessagesByAccount(@PathVariable Integer accountId) {
        return messageService.getMessagesByAccountId(accountId);
    }
}
