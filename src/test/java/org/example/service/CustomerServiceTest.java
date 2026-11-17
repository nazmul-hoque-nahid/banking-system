package org.example.service;

import org.example.model.Customer;
import org.example.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {
   @Mock
    private  CustomerRepository customerRepository;
   @InjectMocks
    private CustomerService customerService;
   @Test
    void testSave(){
       DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
       //String fullName, String email, String phoneNumber, LocalDate dob, String address
       //Customer customer=new Customer("test","test@gmail.com","019763784", LocalDate.parse("12-12-2002", formatter),"kishoregonj");
      Customer customer=mock(Customer.class);
       customerService.save(customer);
       verify(customerRepository).save(customer);
   }
   @Test
    void  testDelete(){
       customerService.delete("customer_id");
       verify(customerRepository).delete("customer_id");
   }
      @Test
    void testFindCustomerById(){
       String customerNumber="CS_87";
       Customer customer=mock(Customer.class);
          when(customerRepository.findByCustomerNumber(customerNumber))
                  .thenReturn(Optional.of(customer));
          Optional<Customer>result=customerService.findByCustomerNumber(customerNumber);
          assertTrue(result.isPresent());
          assertEquals(customer, result.get());
          verify(customerRepository).findByCustomerNumber(customerNumber);
      }
    @Test
    void shouldReturnEmptyWhenCustomerDoesNotExist() {
        String customerNumber = "CUS-999";
        when(customerRepository.findByCustomerNumber(customerNumber))
                .thenReturn(Optional.empty());
        Optional<Customer> result =customerService.findByCustomerNumber(customerNumber);
        assertTrue(result.isEmpty());
        verify(customerRepository).findByCustomerNumber(customerNumber);
    }
    @Test
    void shouldThrowExceptionWhenCustomerNumberIsNull() {
        assertThrows(IllegalArgumentException.class, () -> customerService.findByCustomerNumber(null));
        verifyNoInteractions(customerRepository);
    }
    @Test
    void testExistByCustomerNumberTrue(){
//        public boolean existByCustomerNumber(String customerNumber){
//            if(customerNumber==null){
//                throw new IllegalArgumentException("Customer number can't be null");
//            }
//            return repository.existByCustomerNumber(customerNumber);
//        }
        String customerNumber="OIMN_5_C";
        when(customerRepository.existByCustomerNumber(customerNumber)).thenReturn(true);
        assertTrue(customerService.existByCustomerNumber(customerNumber));
    }
    @Test
    void shouldReturnFalseWhenCustomerDoesNotExist() {
        String customerNumber = "CUS-999";
        when(customerRepository.existByCustomerNumber(customerNumber)).thenReturn(false);
        assertFalse(customerService.existByCustomerNumber(customerNumber));
        verify(customerRepository).existByCustomerNumber(customerNumber);
    }
    @Test
    void shouldReturnAllCustomers() {

        List<Customer> customers = List.of(
                mock(Customer.class),
                mock(Customer.class),
                mock(Customer.class)
        );
        when(customerRepository.findAll()).thenReturn(customers);
        List<Customer> result = customerService.findAll();
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(customers, result);
        verify(customerRepository).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoCustomersExist() {
        when(customerRepository.findAll()).thenReturn(Collections.emptyList());
        List<Customer> result = customerService.findAll();
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(customerRepository).findAll();
    }

    @Test
    void shouldPropagateExceptionWhenRepositoryFails() {
        when(customerRepository.findAll()).thenThrow(new RuntimeException("Database error"));
        assertThrows(RuntimeException.class, () -> customerService.findAll());
        verify(customerRepository).findAll();
    }
}