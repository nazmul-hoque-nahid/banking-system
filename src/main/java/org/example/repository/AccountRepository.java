package org.example.repository;
import org.example.enums.AccountStatus;
import org.example.model.Account;
import java.util.List;
import java.util.Optional;

public interface AccountRepository {
    void save(Account account);
    Optional<Account>findByAccountNumber(String accountNumber);
    List<Account>findAll();
    Optional<Account>findByCustomerNumber(String customerNumber);
    boolean existsByAccountNumber(String accountNumber);
    void  delete(String accountNumber);
    void update(Account account);
    void updateStatus(String accountNumber,AccountStatus status);
}
