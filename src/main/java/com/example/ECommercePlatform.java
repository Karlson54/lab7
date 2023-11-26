package com.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class ECommercePlatform {
    private Map<Integer, User> users;
    private Map<Integer, Product> products;
    private Map<Integer, Order> orders;

    public ECommercePlatform() {
        this.users = new HashMap<>();
        this.products = new HashMap<>();
        this.orders = new HashMap<>();
    }

    public void addUser(User user) {
        users.put(user.getId(), user);
    }

    public void addProduct(Product product) {
        products.put(product.getId(), product);
    }

    public void createOrder(User user) {
        Map<Product, Integer> orderDetails = new HashMap<>(user.getCart());

        for (Map.Entry<Product, Integer> entry : orderDetails.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();

            int newStock = product.getStock() - quantity;
            product.setStock(newStock);
        }

        Order order = new Order(orders.size() + 1, user.getId(), orderDetails);
        orders.put(order.getId(), order);

        user.getCart().clear();
    }

    public List<Product> getAvailableProducts() {
        return new ArrayList<>(products.values());
    }

    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    public List<Order> getOrders() {
        return new ArrayList<>(orders.values());
    }

    public List<Product> getProductsSortedByPrice() {
        List<Product> sortedProducts = new ArrayList<>(products.values());
        Collections.sort(sortedProducts);
        return sortedProducts;
    }

    public List<Product> getProductsSortedByName() {
        List<Product> sortedProducts = new ArrayList<>(products.values());
        sortedProducts.sort(new ProductNameComparator());
        return sortedProducts;
    }

    public List<Product> getProductsSortedByStock() {
        List<Product> sortedProducts = new ArrayList<>(products.values());
        sortedProducts.sort(new ProductStockComparator());
        return sortedProducts;
    }

    public List<Product> filterProductsByStock(int minStock) {
        return products.values().stream()
                .filter(product -> product.getStock() >= minStock)
                .collect(Collectors.toList());
    }

    public List<Product> recommendProducts(User user) {
        List<Product> recommendedProducts = new ArrayList<>();

        Map<Product, Integer> userCart = user.getCart();
        for (Product product : userCart.keySet()) {
            recommendedProducts.addAll(findSimilarProducts(product));
        }

        List<Order> userOrders = getOrdersByUser(user);
        for (Order order : userOrders) {
            for (Product product : order.getOrderDetails().keySet()) {
                recommendedProducts.addAll(findSimilarProducts(product));
            }
        }

        recommendedProducts = recommendedProducts.stream().distinct().collect(Collectors.toList());

        return recommendedProducts;
    }

    private List<Product> findSimilarProducts(Product targetProduct) {
        List<Product> similarProducts = new ArrayList<>();

        List<Product> availableProducts = getAvailableProducts();
        for (Product product : availableProducts) {
            if (!product.equals(targetProduct)) {
                similarProducts.add(product);
            }

            if (similarProducts.size() >= 2) {
                break;
            }
        }

        return similarProducts;
    }

    private List<Order> getOrdersByUser(User user) {
        return getOrders().stream()
                .filter(order -> order.getUserId().equals(user.getId()))
                .collect(Collectors.toList());
    }
}