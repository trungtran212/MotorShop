package com.example.motor_store;

public class Order {
    private int id;
    private String username;
    private String customerName;
    private String phone;
    private String address;
    private String productName;
    private long timestamp;
    private int status;

    public Order(int id, String username, String customerName, String phone, String address, String productName, long timestamp, int status) {
        this.id = id;
        this.username = username;
        this.customerName = customerName;
        this.phone = phone;
        this.address = address;
        this.productName = productName;
        this.timestamp = timestamp;
        this.status = status;
    }

    public Order(String username, String customerName, String phone, String address, String productName, long timestamp, int status) {
        this(-1, username, customerName, phone, address, productName, timestamp, status);
    }

    public int getId() { return id; }
    public String getUsername() {return username;}
    public String getCustomerName() { return customerName; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public String getProductName() { return productName; }
    public long getTimestamp() { return timestamp; }
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }



}
