package com.example.service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account register(Account newAccount) {
        // Validation checks for account creation
        if (newAccount.getUsername() == null || newAccount.getUsername().isBlank()) {
            throw new IllegalArgumentException("Username cannot be blank.");
        }

        if (newAccount.getPassword() == null || newAccount.getPassword().length() < 4) {
            throw new IllegalArgumentException("Password must be at least 4 characters long.");
        }

        if (accountRepository.findByUsername(newAccount.getUsername()).isPresent()) {
            throw new IllegalStateException("Username already exists.");
        }

        return accountRepository.save(newAccount);
    }

    public Account login(Account accountLogin) {
        Optional<Account> found = accountRepository.findByUsernameAndPassword(
                accountLogin.getUsername(),
                accountLogin.getPassword()
        );

        // Security exception if id-pass is invalid
        if (found.isEmpty()) {
            throw new SecurityException("Invalid username or password.");
        }

        return found.get();
    }
}
