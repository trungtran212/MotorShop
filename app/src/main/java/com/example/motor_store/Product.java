package com.example.motor_store;

public class Product {
    private int id;
    private String name;
    private String brand;
    private double price;
    private String description;
    private String imageUrl;
    public Product(int id, String name, String brand, double price, String description, String imageUrl) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getBrand() { return brand; }
    public double getPrice() { return price; }

    public String getDescription() { return description; }
    public String getImageUri() { return imageUrl; }
}
