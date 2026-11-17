package org.example.model;

import org.example.enums.AccountStatus;
import org.example.enums.AccountType;
import org.example.enums.TransactionStatus;
import org.example.enums.TransactionType;
import org.example.exception.InvalidAmountException;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.example.enums.AccountType.CURRENT;

public class Transaction {
    private   String transactionId;
    private final Account fromAccount;
    private final Account toAccount;
    private final BigDecimal amount;
    private final TransactionType type;
    private  LocalDateTime createdAt;
    private TransactionStatus status;
    private Transaction(Builder builder){
        if (builder.amount == null || builder.amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Invalid transaction amount.");
        }

        if (builder.type == null) {
            throw new IllegalArgumentException("Transaction type cannot be null.");
        }

        if (builder.fromAccount == null && builder.toAccount == null) {
            throw new IllegalArgumentException("At least one account is required.");
        }
        this.transactionId = builder.transactionId != null
                ? builder.transactionId
                : UUID.randomUUID().toString();
        this.status = builder.status != null
                ? builder.status
                : TransactionStatus.PENDING;
          this.createdAt=LocalDateTime.now();
          this.type=builder.type;
          this.fromAccount=builder.fromAccount;
          this.toAccount= builder.toAccount;
          this.amount=builder.amount;
        this.createdAt = builder.createdAt != null
                ? builder.createdAt
                : LocalDateTime.now();
    }
    public static class Builder{
        private String transactionId;
        private  Account fromAccount;
        private  Account toAccount;
        private  BigDecimal amount;
        private TransactionType type;
        private TransactionStatus status;
        private LocalDateTime createdAt;

        public Builder fromAccount(Account fromAccount) {
            this.fromAccount = fromAccount;
            return this;
        }
        public Builder transactionId(String transactionId) {
            this.transactionId = transactionId;
            return this;
        }

        public Builder timestamp(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        public Builder status(TransactionStatus status) {
            this.status = status;
            return this;
        }
        public Builder toAccount(Account toAccount) {
            this.toAccount = toAccount;
            return this;
        }

        public Builder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public Builder type(TransactionType type) {
            this.type = type;
            return this;
        }

        public Transaction build() {
            return new Transaction(this);
        }

    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Account getFromAccount() {
        return fromAccount;
    }

    public Account getToAccount() {
        return toAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public TransactionType getType() {
        return type;
    }
    public void markSuccessful() {
        if (status != TransactionStatus.PENDING) {
            throw new IllegalStateException("Transaction already processed.");
        }
        status = TransactionStatus.SUCCESSFUL;
    }

    public void markFailed() {
        if (status != TransactionStatus.PENDING) {
            throw new IllegalStateException("Transaction already processed.");
        }
        status = TransactionStatus.FAILED;
    }
}
