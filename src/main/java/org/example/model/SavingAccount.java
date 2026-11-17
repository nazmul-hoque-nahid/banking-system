package org.example.model;

import org.example.enums.AccountStatus;
import org.example.enums.AccountType;
import org.example.exception.InvalidAmountException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class SavingAccount extends Account{
    private static final BigDecimal MINIMUM_BALANCE=new BigDecimal("500.00");
    public SavingAccount(Customer owner) {
        super(owner,AccountType.SAVINGS);
    }
    public SavingAccount(
            String accountNumber,
            Customer owner,
            BigDecimal balance,
            AccountStatus status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        super(accountNumber, owner, AccountType.SAVINGS, balance, status, createdAt, updatedAt);
    }
    @Override
    public synchronized void withdraw(BigDecimal amount) {
        validateWithdrawal(amount);
        BigDecimal remaining = getBalance().subtract(amount);
        if (remaining.compareTo(MINIMUM_BALANCE) < 0) {
            throw new InvalidAmountException("Minimum balance requirement violated.");
        }
        setBalance(remaining);
    }
}
