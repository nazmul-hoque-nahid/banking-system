package org.example.repository.impl;

import org.example.config.DatabaseConfig;
import org.example.model.Customer;
import org.example.repository.CustomerRepository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MySQLCustomerImpl implements CustomerRepository {
    @Override
    public void save(Customer customer) {
        String sql= """
                insert into customers(id,full_name,email,phone,dob,address,created_at) 
                values(?,?,?,?,?,?,?)
                """;
            try (
                Connection connection= DatabaseConfig.getConnection();
                PreparedStatement ps=connection.prepareStatement(sql);)
                {
                    ps.setString(1,customer.getId());
                    ps.setString(2, customer.getFullName());
                    ps.setString(3,customer.getEmail());
                    ps.setString(4,customer.getPhoneNumber());
                    ps.setDate(5, Date.valueOf(customer.getDob().toString()));
                    ps.setString(6, customer.getAddress());
                    ps.setTimestamp(7, Timestamp.valueOf(customer.getCreatedAt()));
                    ps.executeUpdate();
                }
             catch (SQLException e) {
                throw new RuntimeException(e);
            }
    }

    @Override
    public void delete(String customerNumber){
        String sql= """
                delete from customers where id=?
                """;
                  try (Connection connection= DatabaseConfig.getConnection();
                       PreparedStatement ps=connection.prepareStatement(sql)) {
                      ps.setString(1,customerNumber);
                      ps.executeUpdate();
                  }catch (SQLException e){
                      throw new RuntimeException("Failed to delete",e);
                  }
    }

    @Override
    public Optional<Customer> findByCustomerNumber(String customerNumber) {
        String sql= """
                select * from customers where id=?
                """;
        try (Connection connection= DatabaseConfig.getConnection();
             PreparedStatement ps=connection.prepareStatement(sql)){
            ps.setString(1,customerNumber);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                return Optional.of(new Customer(rs.getString("id"), rs.getString("full_name"), rs.getString("email"),
                        rs.getString("phone"), rs.getDate("dob").toLocalDate(), rs.getString("address"),
                        rs.getTimestamp("created_at").toLocalDateTime()
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find customer",e);
        }
        return Optional.empty();
    }

    @Override
    public boolean existByCustomerNumber(String customerNumber) {
        String sql= """
                select id from customers where id=?
                """;
        try (Connection connection= DatabaseConfig.getConnection();
             PreparedStatement ps=connection.prepareStatement(sql)){
            ps.setString(1,customerNumber);
            ResultSet rs=ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find customer",e);
        }

    }

    @Override
    public List<Customer> findAll() {
        String sql="select * from customers";
        try (Connection connection= DatabaseConfig.getConnection();
             PreparedStatement ps=connection.prepareStatement(sql)){
            ResultSet rs=ps.executeQuery();
            List<Customer>customers=new ArrayList<>();
            while (rs.next()){
                customers.add(
                        new Customer(rs.getString("id"), rs.getString("full_name"), rs.getString("email"),
                                rs.getString("phone"), rs.getDate("dob").toLocalDate(), rs.getString("address"),
                                rs.getTimestamp("created_at").toLocalDateTime()
                        )
                );
            }
            return customers;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find customers",e);
        }

    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        String sql= """
                select * from customers where email=?
                """;
        try (Connection connection= DatabaseConfig.getConnection();
             PreparedStatement ps=connection.prepareStatement(sql)
             ){
            ps.setString(1,email);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    Customer customer = new Customer(
                            rs.getString("id"),
                            rs.getString("full_name"),
                            rs.getString("email"),
                            rs.getString("phone"),
                            rs.getDate("dob").toLocalDate(),
                            rs.getString("address"),
                            rs.getTimestamp("created_at").toLocalDateTime()
                    );

                    return Optional.of(customer);
                }
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
            throw new RuntimeException("failed to find customer");
        }
        return Optional.empty();
    }
}
