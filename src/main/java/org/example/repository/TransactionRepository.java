package org.example.repository;

import org.example.enums.TransactionStatus;
import org.example.model.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository {
    void save(Transaction transaction);
    Optional<Transaction>findByTransactionId(String transactionId);
    void delete(String transactionId);
    List<Transaction>findAll();
    boolean existsBTransactionId(String transactionId);
    void updateStatus(TransactionStatus status,String transactionId);
}
