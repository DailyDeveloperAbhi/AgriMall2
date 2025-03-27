package com.example.agrimall;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static List<Product> cartItems = new ArrayList<>();

    // Add product to cart
    public static void addToCart(Product product) {
        cartItems.add(product);
    }

    // Remove product from cart
    public static void removeFromCart(Product product) {
        cartItems.remove(product);
    }

    // Get all cart items
    public static List<Product> getCartItems() {
        return new ArrayList<>(cartItems); // Return a copy to prevent modification
    }

    // Get total price of cart
    public static double getTotalPrice() {
        double total = 0.0;
        for (Product product : cartItems) {
            total += product.getPrice();
        }
        return total;
    }

    public static void removeItem(Product product) {
        cartItems.remove(product);
    }
}