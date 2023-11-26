package com.example;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class ECommerceTests {

    private Product product;
    private User user;
    private Order order;
    private ECommercePlatform platform;

    @Before
    public void setUp() {
        platform = new ECommercePlatform();
        product = new Product(1, "TestProduct", 100, 50);
        user = new User(1, "TestUser", new HashMap<>());
        Map<Product, Integer> orderDetails = new HashMap<>();
        orderDetails.put(product, 3);
        order = new Order(1, 1, orderDetails);
    }

    @Test
    public void testProductToString() {
        String expected = "Product: id = 1, name = TestProduct, price = 100.00, stock = 50";
        assertEquals(expected, product.toString());
    }

    @Test
    public void testProductComparison() {
        Product product2 = new Product(2, "Product2", 75.0, 15);
        assertTrue(product2.compareTo(product) < 0);
    }

    @Test
    public void testUserToString() {
        String expected = "User: id = 1, username = TestUser";
        assertEquals(expected, user.toString());
    }

    @Test
    public void testAddToCart() {
        user.addToCart(product, 2);
        assertEquals(2, user.getCart().get(product).intValue());
    }

    @Test
    public void testRemoveFromCart() {
        user.addToCart(product, 3);
        user.removeFromCart(product, 2);
        assertEquals(1, user.getCart().get(product).intValue());
    }

    @Test
    public void testOrderToString() {
        String expected = "1 order for user 1. Details: \n\tTestProduct (Quantity: 3, Cost: 300.00, Stock: 50)\nTotal cost: 300.00";
        assertEquals(expected, order.toString());
    }

    @Test
    public void testAddUser() {
        platform.addUser(user);
        assertTrue(platform.getUsers().contains(user));
    }

    @Test
    public void testAddProduct() {
        platform.addProduct(product);
        assertTrue(platform.getAvailableProducts().contains(product));
    }
}
