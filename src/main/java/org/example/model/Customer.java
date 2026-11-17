package org.example.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class Customer {
    private final String id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private final LocalDate dob;
    private String address;
    private final LocalDateTime createdAt;
    public Customer(String fullName, String email, String phoneNumber, LocalDate dob, String address){
        if(fullName==null || email==null || phoneNumber==null || dob==null || address==null){
            throw new IllegalArgumentException("Nothing can't be Null");
        }
        this.id= UUID.randomUUID().toString();
        this.fullName=fullName;
        this.email=email;
        this.phoneNumber=phoneNumber;
        this.dob=dob;
        this.address=address;
        createdAt=LocalDateTime.now();
    }
    public Customer(
            String id,
            String fullName,
            String email,
            String phoneNumber,
            LocalDate dob,
            String address,
            LocalDateTime createdAt
    ) {
        if (id == null || fullName == null || email == null
                || phoneNumber == null || dob == null
                || address == null || createdAt == null) {
            throw new IllegalArgumentException("Fields cannot be null");
        }
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.dob = dob;
        this.address = address;
        this.createdAt = createdAt;
    }

    public void updateFullName(String fullName) {
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.fullName = fullName;
    }

    public void updateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }

        this.email = email;
    }

    public void updatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new IllegalArgumentException("Phone number cannot be empty");
        }

        this.phoneNumber = phoneNumber;
    }
   public String getEmail(){return email;}
    public void updateAddress(String address) {
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("Address cannot be empty");
        }

        this.address = address;
    }
    public String getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public LocalDate getDob() {
        return dob;
    }

    public String getAddress() {
        return address;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

}
