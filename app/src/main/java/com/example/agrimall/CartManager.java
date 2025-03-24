package com.example.agrimall;

import com.example.agrimall.Product;

import java.util.ArrayList;
import java.util.List;

public class CartManager {

    // List to store cart items
    private static final List<Product> cartItems = new ArrayList<>();

    // Add product to cart
    public static void addToCart(Product product) {
        cartItems.add(product);
    }

    // Get all cart items
    public static List<Product> getCartItems() {
        return new ArrayList<>(cartItems); // Return a copy to avoid modifying original list
    }

    // Remove item from cart
    public static void removeItem(Product product) {
        cartItems.remove(product);
    }

    // Clear the entire cart
    public static void clearCart() {
        cartItems.clear();
    }
}