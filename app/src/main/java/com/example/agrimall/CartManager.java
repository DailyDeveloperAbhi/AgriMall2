package com.example.agrimall;

import java.util.ArrayList;
import java.util.List;
import android.util.Log;

public class CartManager {
    private static CartManager instance;
    private List<CartItem> cartItems;

    // ✅ Singleton Constructor
    private CartManager() {
        cartItems = new ArrayList<>();
    }

    public static CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    // ✅ Get all cart items
    public List<CartItem> getCartItems() {
        return cartItems;
    }

    // ✅ Add item to cart
    public void addToCart(Product product) {
        for (CartItem cartItem : cartItems) {
            if (cartItem.getProductName().equals(product.getName())) {
                cartItem.setQuantity(cartItem.getQuantity() + 1);
                Log.d("CartManager", "Increased quantity: " + cartItem.getProductName());
                return;
            }
        }
        CartItem newItem = new CartItem(product);
        cartItems.add(newItem);
        Log.d("CartManager", "Added to cart: " + newItem.getProductName());
    }

    // ✅ Remove item from cart
    public void removeItem(CartItem item) {
        cartItems.remove(item);
        Log.d("CartManager", "Removed from cart: " + item.getProductName());
    }

    // ✅ Increase quantity
    public void increaseQuantity(CartItem item) {
        for (CartItem cartItem : cartItems) {
            if (cartItem.getProductName().equals(item.getProductName())) {
                cartItem.setQuantity(cartItem.getQuantity() + 1);
                Log.d("CartManager", "Increased quantity: " + cartItem.getProductName());
                return;
            }
        }
    }

    // ✅ Decrease quantity or remove if 1
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

    // ✅ Clear entire cart after order
    public void clearCart() {
        cartItems.clear();
        Log.d("CartManager", "Cart cleared successfully");
    }
}
