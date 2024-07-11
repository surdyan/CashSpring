package org.example.cashbackapp.model;

import lombok.Data;

@Data
public class User {
    private String id;
    private String fullname;
    private String email;
    private int points;
}
