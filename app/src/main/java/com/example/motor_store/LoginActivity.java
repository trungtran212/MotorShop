package com.example.motor_store;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    EditText etUsername, etPassword;
    Button btnLogin;

    TextView btnGoToRegister;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoToRegister = findViewById(R.id.tvGoToRegister);

        db = new DatabaseHelper(this);

        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please full fill information", Toast.LENGTH_SHORT).show();
            } else if (db.checkUserLogin(username, password)) {
                String role = db.getUserRole(username);

                SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("username", username);
                editor.putString("role", role);
                editor.apply();

                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();

                Intent intent;
                if ("admin".equals(role)) {
                    intent = new Intent(LoginActivity.this, AdminActivity.class);
                } else {
                    intent = new Intent(LoginActivity.this, UserActivity.class);
                    intent.putExtra("USERNAME", username);
                }

                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Username or password not valid", Toast.LENGTH_SHORT).show();
            }
        });

        btnGoToRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}
