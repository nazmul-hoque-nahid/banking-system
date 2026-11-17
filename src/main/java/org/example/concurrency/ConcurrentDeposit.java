package org.example.concurrency;

import org.example.model.Account;
import org.example.model.Customer;
import org.example.model.CurrentAccount;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Demonstrates multiple worker threads updating the SAME account object.
 * Account.deposit() is synchronized, so each balance update is atomic
 * with respect to other calls on this account instance.
 */
public final class ConcurrentDeposit {
    private ConcurrentDeposit() { }

    public static void main(String[] args) throws Exception {
        int threadCount = 10;
        int depositsPerThread = 100;
        BigDecimal depositAmount = new BigDecimal("10.00");

        Customer customer = new Customer(
                "Concurrency Demo", "demo@example.com", "01700000000",
                LocalDate.of(1995, 1, 1), "Sylhet"
        );
        Account account = new CurrentAccount(customer);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch ready = new CountDownLatch(threadCount);
        CountDownLatch start = new CountDownLatch(1);
        List<Future<?>> tasks = new ArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            tasks.add(executor.submit(() -> {
                ready.countDown();
                start.await();
                for (int j = 0; j < depositsPerThread; j++) {
                    account.deposit(depositAmount);
                }
                return null;
            }));
        }

        ready.await();
        start.countDown();
        for (Future<?> task : tasks) {
            task.get();
        }
        executor.shutdown();

        BigDecimal expected = depositAmount.multiply(BigDecimal.valueOf(threadCount * depositsPerThread));
        BigDecimal actual = account.getBalance();
        System.out.println("Threads: " + threadCount);
        System.out.println("Deposits per thread: " + depositsPerThread);
        System.out.println("Expected balance: " + expected);
        System.out.println("Actual balance:   " + actual);
        System.out.println("Result: " + (expected.compareTo(actual) == 0
                ? "PASS - no lost updates in this shared-object test"
                : "FAIL - balance mismatch"));
    }
}