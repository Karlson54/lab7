package com.example;

import java.util.Map;

import lombok.Data;

@Data
class User {
    private Integer id;
    private String username;
    private Map<Product, Integer> cart;

    public User(Integer id, String username, Map<Product, Integer> cart) {
        this.id = id;
        this.username = username;
        this.cart = cart;
    }

    public void addToCart(Product product, int quantity) {
        cart.put(product, cart.getOrDefault(product, 0) + quantity);
    }

    public void removeFromCart(Product product, int quantity) {
        cart.put(product, cart.getOrDefault(product, 0) - quantity);
    }

    @Override
    public String toString() {
        return String.format("User: id = %d, username = %s", id, username);
    }
}