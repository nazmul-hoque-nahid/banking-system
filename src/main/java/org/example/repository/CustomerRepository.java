package org.example.repository;

import org.example.model.Customer;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
public interface CustomerRepository{
    void save(Customer customer);
    void delete(String customerNumber);
    Optional<Customer>findByCustomerNumber(String customerNumber);
    boolean existByCustomerNumber(String customerNumber);
    List<Customer>findAll();
    Optional<Customer>findByEmail(String email);
}
