package com.example.agrimall;

public class CartItem {
    private String productName;
    private double productPrice;
    private int quantity;
    private String url;

    public CartItem(Product  product) {
        this.productName = product.getName();;
        this.productPrice = product.getPrice();
        this.quantity = product.getQuantity();
        this.url = product.getImageUrl();
    }

    public String getProductName() {
        return productName;
    }


    public String getProductImage() {  // ✅ Add this method
        return url;
    }

    public double getProductPrice() {
        return productPrice;
    }



    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return productPrice * quantity;
    }
}