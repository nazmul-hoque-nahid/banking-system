package org.example.service;

import org.example.exception.InvalidAmountException;
import org.example.model.Customer;
import org.example.model.SavingAccount;
import org.example.repository.CustomerRepository;

import java.util.List;
import java.util.Optional;

public class CustomerService {
    private final CustomerRepository repository;

    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }
   public void save(Customer customer){
        if(customer==null)throw new IllegalArgumentException("Customer can't be null");
        repository.save(customer);
   }
   public void delete(String customerNumber){
        if(customerNumber==null){
            throw new IllegalArgumentException("Customer number can't be null");
        }
        repository.delete(customerNumber);
    }
    public Optional<Customer>findByEmail(String email){
        if(email==null){
            throw new InvalidAmountException("Email can't be null");
        }
        return repository.findByEmail(email);
    }
   public Optional<Customer> findByCustomerNumber(String customerNumber){
       if(customerNumber==null){
           throw new IllegalArgumentException("Customer number can't be null");
       }
       return repository.findByCustomerNumber(customerNumber);
    }
   public boolean existByCustomerNumber(String customerNumber){
       if(customerNumber==null){
           throw new IllegalArgumentException("Customer number can't be null");
       }
       return repository.existByCustomerNumber(customerNumber);
    }

  public   List<Customer> findAll(){
        return repository.findAll();
    }
}
