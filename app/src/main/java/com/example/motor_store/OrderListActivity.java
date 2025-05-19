package com.example.motor_store;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import android.content.SharedPreferences;
import java.util.ArrayList;

public class OrderListActivity extends AppCompatActivity {

    RecyclerView recyclerOrders;
    Button btnBack;
    OrderDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        recyclerOrders = findViewById(R.id.recyclerOrders);
        recyclerOrders.setLayoutManager(new LinearLayoutManager(this));

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        dbHelper = new OrderDatabaseHelper(this);

        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String username = prefs.getString("username", null);
        String role = prefs.getString("role", null);

        List<Order> orderList;
        if ("admin".equals(role)) {
            orderList = dbHelper.getAllOrders();
        } else if (username != null) {
            orderList = dbHelper.getOrdersByUsername(username);
        } else {
            orderList = new ArrayList<>();
        }

        OrderAdapter adapter = new OrderAdapter(orderList, (order, newStatus) -> {
            dbHelper.updateOrderStatus(order.getId(), newStatus);
        });

        recyclerOrders.setAdapter(adapter);
    }
}

