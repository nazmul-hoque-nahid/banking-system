package org.example.service;

import org.example.enums.TransactionStatus;
import org.example.model.Transaction;
import org.example.repository.TransactionRepository;

import java.util.List;
import java.util.Optional;

public class TransactionService {
    private final TransactionRepository repository;
    public TransactionService(TransactionRepository repository) {
        this.repository = repository;
    }
    public void save(Transaction transaction){
     repository.save(transaction);
    }
   public Optional<Transaction> findByTransactionId(String transactionId){
        return repository.findByTransactionId(transactionId);
   }
   public void delete(String transactionId){
        repository.delete(transactionId);
   }
   public List<Transaction> findAll(){
       return repository.findAll();
   }
   public boolean existsBTransactionId(String transactionId){
        return repository.existsBTransactionId(transactionId);
   }
   public void updateStatus(TransactionStatus status, String transactionId){
        repository.updateStatus(status,transactionId);
   }
}
