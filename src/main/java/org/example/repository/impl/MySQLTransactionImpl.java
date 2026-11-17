
package org.example.repository.impl;

import org.example.config.DatabaseConfig;
import org.example.enums.TransactionStatus;
import org.example.enums.TransactionType;
import org.example.model.Account;
import org.example.model.Transaction;
import org.example.repository.AccountRepository;
import org.example.repository.TransactionRepository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MySQLTransactionImpl implements TransactionRepository {
       private final AccountRepository accountRepository;

    public MySQLTransactionImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void save(Transaction transaction) {
        String sql = """
                INSERT INTO transactions
                (transaction_id, from_account, to_account,
                 amount, type, status, created_at)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setString(1, transaction.getTransactionId());
            if (transaction.getFromAccount() == null) {
                ps.setNull(2, Types.VARCHAR);
            } else {
                ps.setString(2, transaction.getFromAccount().getAccountNumber());
            }

            if (transaction.getToAccount() == null) {
                ps.setNull(3, Types.VARCHAR);
            } else {
                ps.setString(3, transaction.getToAccount().getAccountNumber());
            }
            ps.setBigDecimal(4, transaction.getAmount());
            ps.setString(5, transaction.getType().name());
            ps.setString(6, transaction.getStatus().name());
            ps.setTimestamp(7, Timestamp.valueOf(transaction.getCreatedAt()));
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save data",e);
        }
    }

    @Override
    public Optional<Transaction> findByTransactionId(String transactionId) {
        String sql = """
                SELECT *
                FROM transactions
                WHERE transaction_id = ?
                """;
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, transactionId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSet(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("failed to find transaction",e);
        }

    }

    @Override
    public void delete(String transactionId) {
        String sql = """
                DELETE FROM transactions
                WHERE transaction_id = ?
                """;

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, transactionId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete transaction",e);
        }
    }

    @Override
    public List<Transaction> findAll() {

        String sql = """
                SELECT *
                FROM transactions
                ORDER BY created_at DESC
                """;

        List<Transaction> transactions = new ArrayList<>();
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                transactions.add(mapResultSet(rs));
            }
            return transactions;
        } catch (SQLException e) {
            throw new RuntimeException("failed to find transactions",e);
        }
    }

    @Override
    public boolean existsBTransactionId(String transactionId) {

        String sql = """
                SELECT EXISTS(
                    SELECT 1
                    FROM transactions
                    WHERE transaction_id = ?
                )
                """;

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, transactionId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean(1);
                }
                return false;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed ",e);
        }

    }
    @Override
    public void updateStatus(TransactionStatus status, String transactionId) {

        if (status == null || transactionId == null || transactionId.isBlank()) {
            throw new IllegalArgumentException("Invalid transaction details");
        }

        if (status == TransactionStatus.PENDING) {
            throw new IllegalArgumentException("Cannot change transaction status back to PENDING");
        }

        String sql = """
            UPDATE transactions
            SET status = ?
            WHERE transaction_id = ?
            AND status = 'PENDING'
            """;

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, status.name());
            ps.setString(2, transactionId);

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected == 0) {
                throw new IllegalStateException(
                        "Transaction not found or already processed."
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to update transaction status", e);
        }
    }
    private Transaction mapResultSet(ResultSet rs) throws SQLException {
        Account fromAccount = null;
        Account toAccount = null;

        String fromAccountNumber = rs.getString("from_account");
        String toAccountNumber = rs.getString("to_account");

        if (fromAccountNumber != null) {
            fromAccount = accountRepository
                    .findByAccountNumber(fromAccountNumber)
                    .orElseThrow();
        }

        if (toAccountNumber != null) {
            toAccount = accountRepository
                    .findByAccountNumber(toAccountNumber)
                    .orElseThrow();
        }
        return new Transaction.Builder()
                .transactionId(rs.getString("transaction_id"))
                .fromAccount(fromAccount)
                .toAccount(toAccount)
                .amount(rs.getBigDecimal("amount"))
                .type(TransactionType.valueOf(rs.getString("type")))
                .status(TransactionStatus.valueOf(rs.getString("status")))
                .timestamp(rs.getTimestamp("created_at").toLocalDateTime())
                .build();
    }
}
