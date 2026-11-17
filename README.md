# 🏦 Banking System

A Java-based Banking System built using **OOP, JDBC, MySQL, Multithreading,Junit and Synchronization**.

The application supports customer management, savings and current accounts, deposits, withdrawals, money transfers, and transaction history.

## ✨ Key Features

- Customer and account management
- Savings and current account functionality
- Deposit, withdrawal, and money transfer
- Transaction history management
- Custom exception handling
- JDBC and MySQL database integration
- Repository and service layer architecture

## 🧠 Concepts Demonstrated

- **OOP:** Encapsulation, Inheritance, Abstraction, and Polymorphism
- **JDBC:** Database connectivity and CRUD operations
- **Multithreading:** Concurrent task execution using `ExecutorService`
- **Synchronization:** Thread-safe account balance updates using `synchronized`
- **Concurrency:** Multiple threads accessing a shared bank account
- **Design Pattern:** Builder Pattern, Repository Pattern, DIP

## 🧪 Testing

The project includes JUnit 5 and Mockito-based service tests.

A concurrent deposit demonstration uses:
- 10 worker threads
- 100 deposits per thread
- ৳10 per deposit
- Expected final balance: ৳10,000

The test verifies synchronized updates to a shared account object.

## 🛠️ Technologies

- Java
- MySQL
- JDBC
- JUnit 5
- Mockito
- ExecutorService
- CountDownLatch
- BigDecimal

## 👨‍💻 Author

**NAZMUL HOQUE**

