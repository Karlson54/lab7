package com.example;

import java.util.Locale;

import lombok.Data;

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