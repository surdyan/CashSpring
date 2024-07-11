package org.example.cashbackapp.model;

import lombok.Data;

@Data
public class Puncte {
    private String id;
    private String userId;
    private String restaurantId;
    private int points;
}
