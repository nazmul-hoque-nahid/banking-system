package org.example.model;

import org.example.enums.AccountStatus;
import org.example.enums.AccountType;
import org.example.exception.InvalidAmountException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CurrentAccount extends Account{
    private static final BigDecimal OVERDRAFT_LIMIT =new BigDecimal("1000.00");
    public CurrentAccount(Customer owner) {
        super(owner, AccountType.CURRENT);
    }
    public CurrentAccount(
            String accountNumber,
            Customer owner,
            BigDecimal balance,
            AccountStatus status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        super(accountNumber, owner, AccountType.CURRENT, balance, status, createdAt, updatedAt);
    }
    @Override
    public synchronized void withdraw(BigDecimal amount) {
        validateWithdrawal(amount);
        BigDecimal remaining = getBalance().subtract(amount);
        BigDecimal lowestAllowedBalance = OVERDRAFT_LIMIT.negate();

        if (remaining.compareTo(lowestAllowedBalance) < 0) {
            throw new InvalidAmountException("Overdraft limit exceeded.");
        }
        setBalance(remaining);
    }}
