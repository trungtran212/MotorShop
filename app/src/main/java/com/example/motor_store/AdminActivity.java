package com.example.motor_store;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class AdminActivity extends AppCompatActivity {

    Button btnAddProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        btnAddProduct = findViewById(R.id.btnAddProduct);
        Button btnViewProducts = findViewById(R.id.btnViewProducts);

        btnAddProduct.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, AddProductActivity.class);
            startActivity(intent);
        });
        btnViewProducts.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, ProductListActivity.class);
            startActivity(intent);
        });

        Button btnViewOrders = findViewById(R.id.btnViewOrders);
        btnViewOrders.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, OrderListActivity.class);
            startActivity(intent);
        });

        Button btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });


    }
}
