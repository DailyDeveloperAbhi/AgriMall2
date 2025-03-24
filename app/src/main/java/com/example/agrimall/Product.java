package com.example.agrimall;

public class Product {
    private String name;
    private double price;
    private int quantity;
    private String description;
    private int imageResource;

    // Constructor
    public Product(String name, double price, String description, int imageResource,int quantity) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageResource = imageResource;
    }

    // Getters
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getDescription() { return description; }
    public int getImageResource() { return imageResource; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public void decreaseQuantity() {
    }

    public void increaseQuantity() {
    }
}