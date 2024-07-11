package org.example.cashbackapp.controller;

import org.example.cashbackapp.model.Restaurant;
import org.example.cashbackapp.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @GetMapping("/restaurants")
    public List<Restaurant> getAllRestaurants() throws ExecutionException, InterruptedException {
        return restaurantService.getAllRestaurants();
    }
}
