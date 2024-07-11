package org.example.cashbackapp.controller;

import org.example.cashbackapp.model.Purchase;
import org.springframework.web.bind.annotation.*;
import com.google.firebase.cloud.FirestoreClient;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/purchases")
public class PurchaseController {

    @PostMapping
    public String addPurchase(@RequestBody Purchase purchase) throws InterruptedException, ExecutionException {
        Firestore db = FirestoreClient.getFirestore();

        // Calculate the total amount from the items if not provided
        double totalAmount = purchase.getTotalAmount();
        if (totalAmount == 0) {
            totalAmount = purchase.getItems().stream()
                    .mapToDouble(item -> (double) item.get("amount"))
                    .sum();
            purchase.setTotalAmount(totalAmount);
        }

        // Get current date
        String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        Map<String, Object> data = new HashMap<>();
        data.put("userId", purchase.getUserId());
        data.put("userName", purchase.getUserName());
        data.put("restaurantId", purchase.getRestaurantId());
        data.put("restaurantName", purchase.getRestaurantName());
        data.put("items", purchase.getItems());
        data.put("totalAmount", totalAmount);
        data.put("date", currentDate); // Add purchase date

        double points = calculatePoints(totalAmount);
        data.put("points", points);

        db.collection("purchases").add(data).get();
        updatePuncte(purchase.getUserId(), purchase.getRestaurantId(), points);
        return "Purchase added successfully!";
    }

    @GetMapping("/points/{userId}/{restaurantId}")
    public Map<String, Double> getUserPointsForRestaurantAndUser(@PathVariable String userId, @PathVariable String restaurantId) throws InterruptedException, ExecutionException {
        Firestore db = FirestoreClient.getFirestore();

        QuerySnapshot querySnapshot = db.collection("puncte")
                .whereEqualTo("userId", userId)
                .whereEqualTo("restaurantId", restaurantId)
                .get()
                .get();

        double totalPoints = 0;
        for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
            if (document.contains("points")) {
                totalPoints += document.getDouble("points");
            }
        }

        Map<String, Double> response = new HashMap<>();
        response.put("points", totalPoints);

        return response;
    }

    private double calculatePoints(double amount) {
        return amount * 0.03; // 3% cashback in points
    }

    private void updatePuncte(String userId, String restaurantId, double points) throws InterruptedException, ExecutionException {
        Firestore db = FirestoreClient.getFirestore();

        QuerySnapshot querySnapshot = db.collection("puncte")
                .whereEqualTo("userId", userId)
                .whereEqualTo("restaurantId", restaurantId)
                .get()
                .get();

        if (querySnapshot.isEmpty()) {
            // Create new puncte document
            Map<String, Object> puncte = new HashMap<>();
            puncte.put("userId", userId);
            puncte.put("restaurantId", restaurantId);
            puncte.put("points", points);

            db.collection("puncte").add(puncte).get();
        } else {
            // Update existing puncte document
            for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
                double existingPoints = document.getDouble("points");
                double newPoints = existingPoints + points;

                Map<String, Object> updateData = new HashMap<>();
                updateData.put("points", newPoints);

                db.collection("puncte").document(document.getId()).update(updateData).get();
            }
        }
    }
}
