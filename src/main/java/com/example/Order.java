package com.example;

import java.util.Locale;
import java.util.Map;

import lombok.Data;

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