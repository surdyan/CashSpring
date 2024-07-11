package org.example.cashbackapp.controller;

import org.example.cashbackapp.model.Puncte;
import org.springframework.web.bind.annotation.*;
import com.google.firebase.cloud.FirestoreClient;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/puncte")
public class PuncteController {

    @PostMapping
    public String addPuncte(@RequestBody Puncte puncte) throws InterruptedException, ExecutionException {
        Firestore db = FirestoreClient.getFirestore();

        Map<String, Object> data = new HashMap<>();
        data.put("userId", puncte.getUserId());
        data.put("restaurantId", puncte.getRestaurantId());
        data.put("points", puncte.getPoints());

        db.collection("puncte").add(data).get();
        return "Puncte added successfully!";
    }

    @GetMapping("/user-points")
    public int getUserPointsForRestaurant(@RequestParam String userId, @RequestParam String restaurantId) throws InterruptedException, ExecutionException {
        Firestore db = FirestoreClient.getFirestore();

        QuerySnapshot querySnapshot = db.collection("puncte")
                .whereEqualTo("userId", userId)
                .whereEqualTo("restaurantId", restaurantId)
                .get()
                .get();

        int totalPoints = 0;
        for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
            if (document.contains("points")) {
                totalPoints += document.getLong("points").intValue();
            }
        }

        return totalPoints;
    }

    @GetMapping("/points/{userId}/{restaurantId}")
    public Map<String, Integer> getUserPointsForRestaurantAndUser(@PathVariable String userId, @PathVariable String restaurantId) throws InterruptedException, ExecutionException {
        Firestore db = FirestoreClient.getFirestore();

        QuerySnapshot querySnapshot = db.collection("puncte")
                .whereEqualTo("userId", userId)
                .whereEqualTo("restaurantId", restaurantId)
                .get()
                .get();

        int totalPoints = 0;
        for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
            if (document.contains("points")) {
                totalPoints += document.getLong("points").intValue();
            }
        }

        Map<String, Integer> response = new HashMap<>();
        response.put("points", totalPoints);

        return response;
    }
}
