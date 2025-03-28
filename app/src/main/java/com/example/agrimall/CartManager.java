package com.example.agrimall;

import java.util.ArrayList;
import java.util.List;
import android.util.Log;

public class CartManager {
    private static CartManager instance;
    private List<CartItem> cartItems;

    private CartManager() {  // ✅ Constructor should be private (Singleton)
        cartItems = new ArrayList<>();
    }

    public static CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void addToCart(Product product) {
        for (CartItem cartItem : cartItems) {
            if (cartItem.getProductName().equals(product.getName())) {
                cartItem.setQuantity(cartItem.getQuantity() + 1);  // ✅ Increase quantity if exists
                Log.d("CartManager", "Increased quantity: " + cartItem.getProductName());
                return;
            }
        }
        CartItem newItem = new CartItem(product);
        cartItems.add(newItem);
        Log.d("CartManager", "Added to cart: " + newItem.getProductName());
    }

    public void removeItem(CartItem item) {
        cartItems.remove(item);
        Log.d("CartManager", "Removed from cart: " + item.getProductName());
    }

    public void increaseQuantity(CartItem item) {
        for (CartItem cartItem : cartItems) {
            if (cartItem.getProductName().equals(item.getProductName())) {
                cartItem.setQuantity(cartItem.getQuantity() + 1);
                Log.d("CartManager", "Increased quantity: " + cartItem.getProductName());
                return;
            }
        }
    }

    public void decreaseQuantity(CartItem item) {
        for (CartItem cartItem : cartItems) {
            if (cartItem.getProductName().equals(item.getProductName())) {
                if (cartItem.getQuantity() > 1) {
                    cartItem.setQuantity(cartItem.getQuantity() - 1);
                    Log.d("CartManager", "Decreased quantity: " + cartItem.getProductName());
                } else {
                    removeItem(cartItem);
                }
                return;
            }
        }
    }
}