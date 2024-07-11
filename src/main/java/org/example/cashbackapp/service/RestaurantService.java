package org.example.cashbackapp.service;

import org.example.cashbackapp.model.Restaurant;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class RestaurantService {

    public List<Restaurant> getAllRestaurants() throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference restaurants = db.collection("restaurants");
        ApiFuture<QuerySnapshot> querySnapshot = restaurants.get();

        List<Restaurant> restaurantList = new ArrayList<>();
        for (QueryDocumentSnapshot document : querySnapshot.get().getDocuments()) {
            Restaurant restaurant = new Restaurant();
            restaurant.setId(document.getId());
            restaurant.setName(document.getString("name"));
            restaurant.setImageURL(document.getString("imageURL"));
            restaurantList.add(restaurant);
        }
        return restaurantList;
    }
}
