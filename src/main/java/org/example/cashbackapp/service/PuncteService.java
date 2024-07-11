package org.example.cashbackapp.service;

import org.example.cashbackapp.model.Puncte;
import org.springframework.stereotype.Service;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;

import java.util.concurrent.ExecutionException;

@Service
public class PuncteService {

    public void addOrUpdatePoints(String userId, String restaurantId, int points) throws InterruptedException, ExecutionException {
        Firestore db = FirestoreClient.getFirestore();

        QuerySnapshot querySnapshot = db.collection("puncte")
                .whereEqualTo("userId", userId)
                .whereEqualTo("restaurantId", restaurantId)
                .get()
                .get();

        if (!querySnapshot.isEmpty()) {
            for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
                int currentPoints = document.getLong("points").intValue();
                int updatedPoints = currentPoints + points;
                db.collection("puncte").document(document.getId()).update("points", updatedPoints).get();
            }
        } else {
            Puncte puncte = new Puncte();
            puncte.setUserId(userId);
            puncte.setRestaurantId(restaurantId);
            puncte.setPoints(points);

            db.collection("puncte").add(puncte).get();
        }
    }

    public int getUserPoints(String userId, String restaurantId) throws InterruptedException, ExecutionException {
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
}
