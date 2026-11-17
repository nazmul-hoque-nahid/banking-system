package org.example.model;

import org.example.enums.AccountStatus;
import org.example.enums.AccountType;
import org.example.exception.InvalidAccountException;
import org.example.exception.InvalidAmountException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public abstract class Account {
    private final String accountNumber;
    private final Customer owner;
    private final AccountType accountType;
    private BigDecimal balance;
    private AccountStatus accountStatus;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    Account(Customer owner,AccountType accountType){
        if (owner == null) {
            throw new IllegalArgumentException("Account owner cannot be null");
        }

        if (accountType == null) {
            throw new IllegalArgumentException("Account type cannot be null");
        }
        accountNumber= UUID.randomUUID().toString();
        this.owner=owner;
        this.accountType=accountType;
        balance=BigDecimal.ZERO;
        accountStatus=AccountStatus.ACTIVE;
        createdAt=LocalDateTime.now();
        updatedAt=LocalDateTime.now();
    }
     Account(
            String accountNumber,
            Customer owner,
            AccountType accountType,
            BigDecimal balance,
            AccountStatus status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.accountNumber = accountNumber;
        this.owner = Objects.requireNonNull(owner);
        this.accountType = Objects.requireNonNull(accountType);
        this.balance = Objects.requireNonNull(balance);
        this.accountStatus = Objects.requireNonNull(status);
        this.createdAt = Objects.requireNonNull(createdAt);
        this.updatedAt = Objects.requireNonNull(updatedAt);
    }
    public  abstract void withdraw(BigDecimal amount);
    public synchronized void deposit(BigDecimal amount){
        if(amount==null || amount.compareTo(BigDecimal.ZERO)<=0){
            throw new IllegalArgumentException("Invalid amount.");
        }
        if(accountStatus.equals(AccountStatus.INACTIVE)){
            throw new InvalidAccountException("Account is not active.");
        }
        balance=balance.add(amount);
        updatedAt=LocalDateTime.now();
    }

    public synchronized void setAccountStatus(AccountStatus accountStatus) {
        if(accountStatus==null){
            throw new IllegalArgumentException("Can't be null.");
        }
        this.accountStatus = accountStatus;
    }
    protected void setBalance(BigDecimal balance) {
        if(balance==null){
            throw new InvalidAmountException("invalid balance");
        }
        this.balance = balance;
    }

    public String getAccountNumber(){
        return accountNumber;
    }
    protected void validateWithdrawal(BigDecimal amount) {
        if (amount == null ||
                amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Invalid amount.");
        }
        if (getAccountStatus() != AccountStatus.ACTIVE) {
            throw new InvalidAccountException("Account is inactive.");
        }
    }
    public Customer getOwner() {
        return owner;
    }

    public  synchronized  BigDecimal getBalance() {
        return balance;
    }

    public  AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
