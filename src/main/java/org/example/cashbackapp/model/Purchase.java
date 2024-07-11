package org.example.cashbackapp.model;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class Purchase {
    private String userId;
    private String userName;
    private String restaurantId;
    private String restaurantName;
    private List<Map<String, Object>> items;
    private double totalAmount;
    private double points; // Updated to double
    private String date; // Field for purchase date
}
