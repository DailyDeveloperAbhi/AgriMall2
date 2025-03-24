package com.example.agrimall;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static List<Product> cartList = new ArrayList<>();

    public static void addToCart(Product product) {
        cartList.add(product);
    }

    public static List<Product> getCartList() {
        return cartList;
    }

    public static double getTotalPrice() {
        double total = 0;
        for (Product product : cartList) {
            total += product.getPrice();
        }
        return total;
    }

    public static void clearCart() {
        cartList.clear();
    }
}