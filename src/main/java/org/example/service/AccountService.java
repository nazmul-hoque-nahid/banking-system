package org.example.service;
import org.example.enums.AccountStatus;
import org.example.enums.TransactionStatus;
import org.example.enums.TransactionType;
import org.example.exception.InvalidAccountException;
import org.example.exception.InvalidAmountException;
import org.example.model.Account;
import org.example.model.Transaction;
import org.example.repository.AccountRepository;
import org.example.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
public class AccountService{
    private final AccountRepository repository;
    private final TransactionRepository transactionRepository;
    public AccountService(AccountRepository repository, TransactionRepository transactionRepository){
        this.repository=repository;
        this.transactionRepository = transactionRepository;
    }
    public void createAccount(Account account){
        if(account==null){
            throw new InvalidAccountException("account cant be null.");
        }
         repository.save(account);
    }
    public Optional<Account>findAccountByNumber(String accountNumber){
        if(accountNumber==null){
            throw new IllegalArgumentException("Account number can't be null");
        }
        return repository.findByAccountNumber(accountNumber);
    }
    boolean existsByAccountNumber(String accountNumber){
        if(accountNumber==null){
            throw new IllegalArgumentException("Account number can't be null");
        }
        return repository.existsByAccountNumber(accountNumber);
    }
    public Optional<Account>findByCustomerNumber(String customerNumber){
        if(customerNumber==null){
            throw new InvalidAmountException("Customer number can't be null");
        }
        return repository.findByCustomerNumber(customerNumber);
    }
    public void deposit(String accountNumber, BigDecimal amount) {
        validateAccountNumber(accountNumber);
        validateAmount(amount);
        Account account = repository
                .findByAccountNumber(accountNumber)
                .orElseThrow(() -> new InvalidAccountException("Account not found"));
        account.deposit(amount);
        Transaction transaction=new Transaction.Builder()

                .toAccount(account)
                .amount(amount)
                .status(TransactionStatus.SUCCESSFUL)
                .timestamp(LocalDateTime.now())
                .type(TransactionType.DEPOSIT)
                .build();
        transactionRepository.save(transaction);
        repository.update(account);
    }
    public void withdraw(String accountNumber, BigDecimal amount){
        validateAccountNumber(accountNumber);
        validateAmount(amount);
        Account account = repository
                .findByAccountNumber(accountNumber)
                .orElseThrow(() -> new InvalidAccountException("Account not found"));
        account.withdraw(amount);
        Transaction transaction=new Transaction.Builder()
                .fromAccount(account)
                .amount(amount)
                .status(TransactionStatus.SUCCESSFUL)
                .timestamp(LocalDateTime.now())
                .type(TransactionType.WITHDRAWAL)
                .build();
        transactionRepository.save(transaction);
        repository.update(account);
    }
    public void transfer(String fromAccount,String toAccount,BigDecimal amount){
        validateAccountNumber(fromAccount);
        validateAccountNumber(toAccount);
        validateAmount(amount);
        Account from = repository
                .findByAccountNumber(fromAccount)
                .orElseThrow(() -> new InvalidAccountException("Account not found"));
        Account to = repository
                .findByAccountNumber(toAccount)
                .orElseThrow(() -> new InvalidAccountException("Account not found"));
        from.withdraw(amount);
        to.deposit(amount);
        Transaction transaction=new Transaction.Builder()
                .fromAccount(from)
                .toAccount(to)
                .type(TransactionType.TRANSFER)
                .status(TransactionStatus.SUCCESSFUL)
                .amount(amount)
                .timestamp(LocalDateTime.now())
                .build();
        transactionRepository.save(transaction);
        repository.update(from);
        repository.update(to);
    }
    public void updateStatus(String accountNumber, AccountStatus status){
        repository.updateStatus(accountNumber,status);
    }
    public List<Account>findAll(){
        return repository.findAll();
    }
    public void delete(String accountNumber){
        if(accountNumber==null){
            throw new IllegalArgumentException("Account number can't be null");
        }
        repository.delete(accountNumber);
    }
    private void validateAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.isBlank()) {
            throw new IllegalArgumentException("Account number cannot be empty");
        }
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Amount must be greater than zero");
        }
    }
}
