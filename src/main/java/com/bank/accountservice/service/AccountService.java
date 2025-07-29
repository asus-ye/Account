package com.bank.accountservice.service;

import com.bank.accountservice.model.Account;
import com.bank.accountservice.model.Customer;
import com.bank.accountservice.model.AccountType;
import com.bank.accountservice.repository.AccountRepository;
import com.bank.accountservice.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID; // لتوليد رقم حساب فريد

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Optional<Account> getAccountById(Long id) {
        return accountRepository.findById(id);
    }

    public List<Account> getAccountsByCustomerId(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id " + customerId));
        return customer.getAccounts();
    }

    public Account createAccount(Long customerId, AccountType accountType, BigDecimal initialBalance) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id " + customerId));

        Account account = new Account();
        account.setCustomer(customer);
        account.setAccountType(accountType);
        account.setBalance(initialBalance != null ? initialBalance : BigDecimal.ZERO);
        account.setAccountNumber(generateUniqueAccountNumber()); // توليد رقم حساب فريد

        return accountRepository.save(account);
    }

    public Account updateAccount(Long id, Account accountDetails) {
        return accountRepository.findById(id).map(account -> {
            account.setAccountType(accountDetails.getAccountType());
            account.setBalance(accountDetails.getBalance());
            // لا يجب تحديث customer_id أو account_number بهذه الطريقة عادة
            return accountRepository.save(account);
        }).orElseThrow(() -> new RuntimeException("Account not found with id " + id));
    }

    public void deleteAccount(Long id) {
        accountRepository.deleteById(id);
    }

    // طريقة لتوليد رقم حساب فريد (مثال بسيط، يمكن تحسينها)
    private String generateUniqueAccountNumber() {
        return "ACC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}