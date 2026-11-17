package org.example.service;

import org.example.enums.TransactionStatus;
import org.example.enums.TransactionType;
import org.example.exception.InvalidAccountException;
import org.example.exception.InvalidAmountException;
import org.example.model.Account;
import org.example.model.Transaction;
import org.example.repository.AccountRepository;
import org.example.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private TransactionRepository transactionRepository;
    @InjectMocks
    private AccountService accountService;
    @Test
    void createAccountTest(){
        Account account=mock(Account.class);
        accountService.createAccount(account);
        verify(accountRepository).save(account);
    }
    @Test
    void accountCreationShouldThrowException(){
        assertThrows(InvalidAccountException.class,() -> accountService.createAccount(null));
        verifyNoInteractions(accountRepository);
    }
    @Test
    void shouldPropagateExceptionWhenRepositoryFails() {
        Account account = mock(Account.class);
        doThrow(new RuntimeException("Database error")).when(accountRepository).save(account);
        assertThrows(RuntimeException.class, () -> accountService.createAccount(account));
        verify(accountRepository).save(account);
    }
    @Test
    void shouldTestOptionalFindAccountByNumber(){
        String accountNumber="98-af";
        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.empty());
        assertEquals(Optional.empty(),accountService.findAccountByNumber(accountNumber));
    }
    @Test
    void shouldReturnCustomerFindByAccountNumber(){
        String accountNumber="98-af";
        Account account=mock(Account.class);
        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(account));
        Optional<Account> result=accountService.findAccountByNumber(accountNumber);
        assertTrue(result.isPresent());
        assertSame(account,result.get());
        verify(accountRepository, times(1)).findByAccountNumber(accountNumber);
    }
  @Test
    void  shouldThrowException(){
      String accountNumber="98-af";
      assertThrows(IllegalArgumentException.class,()->accountService.findAccountByNumber(null));
      verifyNoInteractions(accountRepository);
  }

    @Test
    void shouldReturnTrueExistByAccountNumber(){
        String accountNumber="98-af";
        when(accountRepository.existsByAccountNumber(accountNumber)).thenReturn(true);
        assertTrue(accountService.existsByAccountNumber(accountNumber));
    }
    @Test
    void shouldReturnFalseExistByAccountNumber(){
        String accountNumber="98-af";
        when(accountRepository.existsByAccountNumber(accountNumber)).thenReturn(false);
        assertFalse(accountService.existsByAccountNumber(accountNumber));
    }
    @Test
    void shouldThrowExceptionWhenNull(){
        String accountNumber="98-af";
        assertThrows(IllegalArgumentException.class,()->accountService.existsByAccountNumber(null));
        verifyNoInteractions(accountRepository);
    }
//    public void deposit(String accountNumber, BigDecimal amount) {
//        validateAccountNumber(accountNumber);
//        validateAmount(amount);
//        Account account = repository
//                .findByAccountNumber(accountNumber)
//                .orElseThrow(() -> new InvalidAccountException("Account not found"));
//        account.deposit(amount);
//        Transaction transaction=new Transaction.Builder()
//                .toAccount(account)
//                .amount(amount)
//                .status(TransactionStatus.SUCCESSFUL)
//                .timestamp(LocalDateTime.now())
//                .type(TransactionType.DEPOSIT)
//                .build();
//        transactionRepository.save(transaction);
//        repository.update(account);
//    }
@Test
void shouldDepositSuccessfully() {

    String accountNumber = "98-af";
    BigDecimal amount = new BigDecimal("1000.00");

    Account account = mock(Account.class);

    when(accountRepository.findByAccountNumber(accountNumber))
            .thenReturn(Optional.of(account));

    accountService.deposit(accountNumber, amount);

    ArgumentCaptor<Transaction> captor =
            ArgumentCaptor.forClass(Transaction.class);

    verify(transactionRepository).save(captor.capture());

    Transaction transaction = captor.getValue();

    assertEquals(TransactionType.DEPOSIT, transaction.getType());
    assertEquals(TransactionStatus.SUCCESSFUL, transaction.getStatus());
    assertEquals(0, amount.compareTo(transaction.getAmount()));
    assertSame(account, transaction.getToAccount());

    verify(account).deposit(amount);
    verify(accountRepository).update(account);
}
    @Test
    void shouldThrowExceptionWhenAccountDoesNotExist() {

        String accountNumber = "98-af";
        BigDecimal amount = new BigDecimal("1000.00");

        when(accountRepository.findByAccountNumber(accountNumber))
                .thenReturn(Optional.empty());

        assertThrows(
                InvalidAccountException.class,
                () -> accountService.deposit(accountNumber, amount)
        );

        verify(accountRepository).findByAccountNumber(accountNumber);

        verifyNoInteractions(transactionRepository);
    }
    @Test
    void shouldThrowExceptionWhenAmountIsInvalid() {

        String accountNumber = "98-af";
        BigDecimal amount = BigDecimal.ZERO;

        assertThrows(
                InvalidAmountException.class,
                () -> accountService.deposit(accountNumber, amount)
        );

        verifyNoInteractions(accountRepository, transactionRepository);
    }
    @Test
    void shouldThrowExceptionWhenAccountNumberIsNull() {

        BigDecimal amount = new BigDecimal("1000.00");

        assertThrows(
                IllegalArgumentException.class,
                () -> accountService.deposit(null, amount)
        );

        verifyNoInteractions(accountRepository, transactionRepository);
    }
    @Test
    void shouldNotSaveTransactionWhenDepositFails() {

        String accountNumber = "98-af";
        BigDecimal amount = new BigDecimal("1000.00");

        Account account = mock(Account.class);

        when(accountRepository.findByAccountNumber(accountNumber))
                .thenReturn(Optional.of(account));

        doThrow(new InvalidAccountException("Account is inactive"))
                .when(account).deposit(amount);

        assertThrows(
                InvalidAccountException.class,
                () -> accountService.deposit(accountNumber, amount)
        );

        verifyNoInteractions(transactionRepository);
        verify(accountRepository, never()).update(any());
    }
}