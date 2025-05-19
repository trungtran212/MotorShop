package com.example.motor_store;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    EditText etNewUsername, etNewPassword, etConfirmPassword;
    Button btnRegister;

    TextView goToLogin;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etNewUsername = findViewById(R.id.etNewUserName);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        goToLogin = findViewById(R.id.tvgoToLogin);

        db = new DatabaseHelper(this);

        btnRegister.setOnClickListener(v -> {
            String username = etNewUsername.getText().toString().trim();
            String password = etNewPassword.getText().toString().trim();
            String confirm = etConfirmPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
                Toast.makeText(this, "Please full fill information", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(confirm)) {
                Toast.makeText(this, "Confirm password does not match", Toast.LENGTH_SHORT).show();
            } else if (db.checkUserExists(username)) {
                Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show();
            } else {
                boolean inserted = db.insertUser(username, password);
                if (inserted) {
                    Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
                    finish(); // Quay lại Login
                } else {
                    Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show();
                }
                }
        });
        goToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });
        }
}
