package com.example;

import lombok.*;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Comparator;
import java.util.Collections;
import java.util.stream.Collectors;

@Data
class Product implements Comparable<Product> {
    private Integer id;
    private String name;
    private double price;
    private int stock;

    public Product(Integer id, String name, double price, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "Product: id = %d, name = %s, price = %.2f, stock = %d", id, name, price,
                stock);
    }

    @Override
    public int compareTo(Product other) {
        return Double.compare(this.price, other.price);
    }
}

class ProductNameComparator implements Comparator<Product> {
    @Override
    public int compare(Product p1, Product p2) {
        return p1.getName().compareTo(p2.getName());
    }
}

class ProductStockComparator implements Comparator<Product> {
    @Override
    public int compare(Product p1, Product p2) {
        return Integer.compare(p1.getStock(), p2.getStock());
    }
}

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

@Data
class Order {
    private Integer id;
    private Integer userId;
    private Map<Product, Integer> orderDetails;
    private double totalPrice;

    public Order(Integer id, Integer userId, Map<Product, Integer> orderDetails) {
        this.id = id;
        this.userId = userId;
        this.orderDetails = orderDetails;
        this.totalPrice = calculateTotalPrice();
    }

    private double calculateTotalPrice() {
        double total = 0.0;
        for (Map.Entry<Product, Integer> entry : orderDetails.entrySet()) {
            Product product = entry.getKey();
            int quantityInOrder = entry.getValue();
            total += product.getPrice() * quantityInOrder;
        }
        return total;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%d order for user %d. Details: ", id, userId));

        for (Map.Entry<Product, Integer> entry : orderDetails.entrySet()) {
            Product product = entry.getKey();
            sb.append("\n\t")
                    .append(String.format(Locale.US, "%s (Quantity: %d, Cost: %.2f, Stock: %d)",
                            product.getName(), entry.getValue(),
                            product.getPrice() * entry.getValue(), product.getStock()))
                    .append(",");
        }

        if (!orderDetails.isEmpty()) {
            sb.setLength(sb.length() - 1);
        }

        sb.append(String.format(Locale.US, "\nTotal cost: %.2f", totalPrice));

        return sb.toString();
    }
}

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
