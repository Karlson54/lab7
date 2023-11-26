package com.example;

import java.util.HashMap;
import java.util.List;

public class ECommerceDemo {
    public static void main(String[] args) {
        ECommercePlatform platform = new ECommercePlatform();
    
        User user1 = new User(1, "Andrii", new HashMap<Product, Integer>());
        User user2 = new User(2, "Dima", new HashMap<Product, Integer>());
    
        Product laptop = new Product(1, "Laptop", 999.99, 10);
        Product smartphone = new Product(2, "Smartphone", 499.99, 20);
        Product mouse = new Product(3, "Mouse", 199.99, 50);
    
        platform.addUser(user1);
        platform.addUser(user2);
    
        platform.addProduct(laptop);
        platform.addProduct(smartphone);
        platform.addProduct(mouse);
    
        user1.addToCart(laptop, 2);
        user1.addToCart(smartphone, 1);
        user1.addToCart(mouse, 3);
        user1.removeFromCart(mouse, 1);
    
        platform.createOrder(user1);
    
        user2.addToCart(smartphone, 3);
        platform.createOrder(user2);
    
        List<Product> recommendationsUser1 = platform.recommendProducts(user1);
    
        System.out.println("Final State:");
        System.out.println("Users:");
        for (User user : platform.getUsers()) {
            System.out.println(user);
        }
    
        System.out.println("Products:");
        for (Product product : platform.getAvailableProducts()) {
            System.out.println(product);
        }
    
        System.out.println("Orders:");
        for (Order order : platform.getOrders()) {
            System.out.println(order);
        }
    
        System.out.println("Recommendations for User 1:");
        for (Product recommendedProduct : recommendationsUser1) {
            System.out.println(recommendedProduct);
        }
    }
}
