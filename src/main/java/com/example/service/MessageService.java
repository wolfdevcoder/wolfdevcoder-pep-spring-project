package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    private final AccountRepository accountRepository;
    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(AccountRepository accountRepository, MessageRepository messageRepository) {
        this.accountRepository = accountRepository;
        this.messageRepository= messageRepository;
    }

    public Message createMessage(Message message) { 
        // Validating posting message
        if (message.getMessageText() == null || message.getMessageText().isBlank() || message.getMessageText().length() > 255) {
            throw new IllegalArgumentException("Message text must be non-blank and under 255 characters.");
        }

        if (!accountRepository.existsById(message.getPostedBy())) {
            throw new IllegalArgumentException("Invalid postedBy user ID.");
        }

        return messageRepository.save(message);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Optional<Message> getMessageById(Integer id) {
        return messageRepository.findById(id);
    }

    public boolean deleteMessage(Integer id) {
        if (messageRepository.existsById(id)) {
            messageRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean updateMessageText(Integer id, String newText) {
        Optional<Message> optionalMessage = messageRepository.findById(id);

        if (optionalMessage.isEmpty()) return false;

        Message message = optionalMessage.get();

        if (newText == null || newText.isBlank() || newText.length() > 255) {
            throw new IllegalArgumentException("New message text must be valid.");
        }

        message.setMessageText(newText);
        messageRepository.save(message);
        return true;
    }

    public List<Message> getMessagesByAccountId(Integer accountId) {
        return messageRepository.findByPostedBy(accountId);
    }
}
