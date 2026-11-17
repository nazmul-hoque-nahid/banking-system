package org.example;

import org.example.config.DatabaseInitializer;
import org.example.model.Account;
import org.example.model.Customer;
import org.example.model.SavingAccount;
import org.example.repository.AccountRepository;
import org.example.repository.TransactionRepository;
import org.example.repository.impl.MySQLAccountImpl;
import org.example.repository.impl.MySQLCustomerImpl;
import org.example.repository.impl.MySQLTransactionImpl;
import org.example.service.AccountService;
import org.example.service.CustomerService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        DatabaseInitializer.initialize();
        MySQLCustomerImpl mySQLCustomer=new MySQLCustomerImpl();
        CustomerService customerService=new CustomerService(mySQLCustomer);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        MySQLAccountImpl mySQLAccount=new MySQLAccountImpl();
        MySQLTransactionImpl mySQLTransaction=new MySQLTransactionImpl(mySQLAccount);
        AccountService accountService=new AccountService(mySQLAccount,mySQLTransaction);
        Customer customer = new Customer(
                "nazmul hoque",
                "nahid@gmail.com",
                "0176588",
                LocalDate.parse("12-12-2002", formatter),
                "kishorenj"
        );
//service.save(customer);
//        Customer customer1 = customerService.findByEmail("nahid@gmail.com")
//                .orElseThrow(() -> new RuntimeException("Customer not found"));
//        Account account = new SavingAccount(customer1);
//        accountService.createAccount(account);
//        System.out.println("Successfully created account");
        Optional<Account>res=accountService.findAccountByNumber("3484001c-9a55-4ce3-824c-47c23c0209a9");
        Account account=res.orElseThrow(()->new RuntimeException("not found"));
       accountService.withdraw("3484001c-9a55-4ce3-824c-47c23c0209a9",new BigDecimal("200.00"));
       // System.out.println(account.getBalance());

    }
}