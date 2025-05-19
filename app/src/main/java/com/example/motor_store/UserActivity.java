package com.example.motor_store;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        TextView tvAdminGreeting = findViewById(R.id.tvUserGreeting);

        String username = getIntent().getStringExtra("USERNAME");
        if (username != null && !username.isEmpty()) {
            tvAdminGreeting.setText("Hello " + username + "!");
        } else {
            tvAdminGreeting.setText("Hello User!");
        }

        Button btnViewProducts = findViewById(R.id.btnViewProducts);
        btnViewProducts.setOnClickListener(v -> {
            Intent intent = new Intent(UserActivity.this, ProductListActivity.class);
            startActivity(intent);
        });

        Button btnViewOrders = findViewById(R.id.btnViewOrders);
        btnViewOrders.setOnClickListener(v -> {
            Intent intent = new Intent(UserActivity.this, OrderListActivity.class);
            startActivity(intent);
        });

        Button btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(UserActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

}

