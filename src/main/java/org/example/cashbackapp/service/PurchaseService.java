package org.example.cashbackapp.service;

import org.example.cashbackapp.model.Purchase;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class PurchaseService {

    public void addPurchase(Purchase purchase) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference purchases = db.collection("purchases");
        ApiFuture<DocumentReference> addedDocRef = purchases.add(purchase);
    }
}
