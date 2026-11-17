package org.example.repository.impl;

import org.example.config.DatabaseConfig;
import org.example.enums.AccountStatus;
import org.example.enums.AccountType;
import org.example.exception.AccountNotFoundException;
import org.example.model.Account;
import org.example.model.CurrentAccount;
import org.example.model.Customer;
import org.example.model.SavingAccount;
import org.example.repository.AccountRepository;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MySQLAccountImpl implements AccountRepository {

    private Account mapRowToAccount(ResultSet rs) throws SQLException {

        Customer owner = new Customer(
                rs.getString("customer_id"),
                rs.getString("full_name"),
                rs.getString("email"),
                rs.getString("phone"),
                rs.getDate("dob").toLocalDate(),
                rs.getString("address"),
                rs.getTimestamp("customer_created_at").toLocalDateTime()
        );

        AccountType type = AccountType.valueOf(
                rs.getString("account_type")
        );

        String accountNumber = rs.getString("account_number");
        BigDecimal balance = rs.getBigDecimal("balance");

        AccountStatus status = AccountStatus.valueOf(
                rs.getString("status")
        );

        LocalDateTime createdAt =
                rs.getTimestamp("created_at").toLocalDateTime();

        LocalDateTime updatedAt =
                rs.getTimestamp("updated_at").toLocalDateTime();

        return switch (type) {

            case SAVINGS -> new SavingAccount(
                    accountNumber, owner, balance,
                    status, createdAt, updatedAt
            );

            case CURRENT -> new CurrentAccount(
                    accountNumber, owner, balance,
                    status, createdAt, updatedAt
            );
        };
    }
    @Override
    public void save(Account account) {
           try {
               Connection connection= DatabaseConfig.getConnection();
               String sql = """
    INSERT INTO accounts (
        account_number,
        customer_id,
        account_type,
        balance,
        status,
        created_at,
        updated_at
    )VALUES (?, ?, ?, ?, ?, ?, ?)""";
               PreparedStatement ps=connection.prepareStatement(sql);
               ps.setString(1,account.getAccountNumber());
               ps.setString(2,account.getOwner().getId());
               ps.setString(3,account.getAccountType().name());
               ps.setBigDecimal(4,account.getBalance());
               ps.setString(5,account.getAccountStatus().name());
               ps.setTimestamp(6, Timestamp.valueOf(account.getCreatedAt()));
               ps.setTimestamp(7, Timestamp.valueOf(account.getUpdatedAt()));
               ps.executeUpdate();
               ps.close();
               connection.close();
           } catch (SQLException e) {
               System.out.println(e.getMessage());
               throw new RuntimeException("Failed to save",e);
           }
    }

    @Override
    public Optional<Account> findByAccountNumber(String accountNumber) {
        try {
            Connection connection=DatabaseConfig.getConnection();
            String sql = """
    SELECT
        a.account_number,
        a.customer_id,
        a.account_type,
        a.balance,
        a.status,
        a.created_at,
        a.updated_at,

        c.full_name,
        c.email,
        c.phone,
        c.dob,
        c.address,
        c.created_at AS customer_created_at

    FROM accounts a
    JOIN customers c
        ON a.customer_id = c.id
    WHERE a.account_number = ?
    """;
            PreparedStatement ps=connection.prepareStatement(sql);
            ps.setString(1,accountNumber);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                Account account = mapRowToAccount(rs);
                return Optional.of(account);
            }
            ps.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Failed to find account",e);
        }
        return Optional.empty();
    }

    @Override
    public List<Account> findAll() {
        try {
            Connection connection=DatabaseConfig.getConnection();
            String sql = """
    SELECT
        a.account_number,
        a.customer_id,
        a.account_type,
        a.balance,
        a.status,
        a.created_at,
        a.updated_at,

        c.full_name,
        c.email,
        c.phone,
        c.dob,
        c.address,
        c.created_at AS customer_created_at

    FROM accounts a
    JOIN customers c
        ON a.customer_id = c.id
    """;
            PreparedStatement ps=connection.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
            List<Account>accounts=new ArrayList<>();
            while (rs.next()){
                Account account=mapRowToAccount(rs);
                accounts.add(account);
            }
            ps.close();
            connection.close();
            return  accounts;
        }catch (SQLException e){
            throw new RuntimeException("failed to retrieve data");
        }
    }

    @Override
    public Optional<Account> findByCustomerNumber(String customerNumber) {
        try {
            Connection connection=DatabaseConfig.getConnection();
            String sql= """
                    select   a.account_number,
                            a.customer_id,
                            a.account_type,
                            a.balance,
                            a.status,
                            a.created_at,
                            a.updated_at,
                    
                            c.full_name,
                            c.email,
                            c.phone,
                            c.dob,
                            c.address,
                            c.created_at AS customer_created_at
                    from account a
                    join customer c on a.customer_id=c.id
                    where a.customer_id=?
                    """;
            PreparedStatement ps= connection.prepareStatement(sql);
            ps.setString(1,customerNumber);
            ResultSet rs=ps.executeQuery();
            while (rs.next()){
                Account account= mapRowToAccount(rs);
                return Optional.of(account);
            }
            ps.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find account",e);
        }
        return Optional.empty();
    }

    @Override
    public boolean existsByAccountNumber(String accountNumber) {
        try {
            Connection connection=DatabaseConfig.getConnection();
            String sql = """
    SELECT
        a.account_number,
        a.customer_id,
        a.account_type,
        a.balance,
        a.status,
        a.created_at,
        a.updated_at,

        c.full_name,
        c.email,
        c.phone,
        c.dob,
        c.address,
        c.created_at AS customer_created_at

    FROM accounts a
    JOIN customers c
        ON a.customer_id = c.id
    WHERE a.account_number = ?
    """;
            PreparedStatement ps= connection.prepareStatement(sql);
            ps.setString(1,accountNumber);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getBoolean(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find account",e);
        }

    }

    @Override
    public void delete(String accountNumber) {
        try {
            Connection connection=DatabaseConfig.getConnection();
            String sql= """
                    delete from accounts where account_number=?
                    """;
            PreparedStatement ps=connection.prepareStatement(sql);
            ps.setString(1,accountNumber);
            ps.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete",e);
        }
    }
    @Override
    public void update(Account account) {

        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null");
        }

        String sql = """
            UPDATE accounts
            SET balance = ?,
                status = ?,
                updated_at = ?
            WHERE account_number = ?
            """;

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setBigDecimal(1, account.getBalance());
            ps.setString(2, account.getAccountStatus().name());
            ps.setTimestamp(3, Timestamp.valueOf(account.getUpdatedAt()));
            ps.setString(4, account.getAccountNumber());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected == 0) {
                throw new AccountNotFoundException("Account not found: " + account.getAccountNumber()
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to update account", e);
        }
    }

    @Override
    public void updateStatus(String accountNumber ,AccountStatus status) {
        String sql= """
                update accounts set status=?
                where account_number=?
                and status="PENDING"
                """;
        try  (Connection connection = DatabaseConfig.getConnection();
              PreparedStatement ps = connection.prepareStatement(sql)){
           ps.setString(1,status.name());
           ps.setString(2,accountNumber);
          int rowsAffected= ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new AccountNotFoundException("Account not found: "
                );
            }
        }catch (SQLException e){
            throw new RuntimeException("Failed to update");
        }
    }
}
