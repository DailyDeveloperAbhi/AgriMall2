package com.example.agrimall;

public class Product {
    private String name;
    private int price;
    private String description;
    private String imageUrl;

    public Product(String productName, int i, String productDescription, int productImage, int productQuantity) {
        // Empty constructor needed for Firestore
    }

    public Product(String name, int price, String description, String imageUrl) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public String getName() { return name; }
    public int getPrice() { return price; }
    public String getDescription() { return description; }
    public String getImageUrl() { return imageUrl; }

    public int getQuantity() {
        return getQuantity();
    }

    public void decreaseQuantity() {
    }
}