package com.example.motor_store;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ProductDetailActivity extends AppCompatActivity {

    ImageView ivImage;
    TextView tvName, tvBrand, tvPrice, tvDescription;
    Button btnBack, btnEdit, btnDelete;

    private int productId = -1;
    private String name, brand, price, description, imageUri;

    private ActivityResultLauncher<Intent> editProductLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        ivImage = findViewById(R.id.ivDetailImage);
        tvName = findViewById(R.id.tvDetailName);
        tvBrand = findViewById(R.id.tvDetailBrand);
        tvPrice = findViewById(R.id.tvDetailPrice);
        tvDescription = findViewById(R.id.tvDetailDescription);
        btnBack = findViewById(R.id.btnBack);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);
        Button btnBuy = findViewById(R.id.btnBuy);

        Intent intent = getIntent();
        productId = intent.getIntExtra("id", -1);
        name = intent.getStringExtra("name");
        brand = intent.getStringExtra("brand");
        price = intent.getStringExtra("price");
        description = intent.getStringExtra("description");
        imageUri = intent.getStringExtra("imageUri");

        updateUI();

        btnBack.setOnClickListener(v -> finish());
        btnBuy.setOnClickListener(v -> showOrderDialog());

        SharedPreferences sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String role = sharedPref.getString("role", "null");
        if ("admin".equals(role)) {
            btnEdit.setVisibility(View.VISIBLE);
            btnDelete.setVisibility(View.VISIBLE);
        } else {
            btnEdit.setVisibility(View.GONE);
            btnDelete.setVisibility(View.GONE);
        }

        editProductLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        productId = data.getIntExtra("id", productId);
                        name = data.getStringExtra("name");
                        brand = data.getStringExtra("brand");
                        price = data.getStringExtra("price");
                        description = data.getStringExtra("description");
                        imageUri = data.getStringExtra("imageUri");

                        updateUI();
                    }
                }
        );

        btnEdit.setOnClickListener(v -> {
            Intent editIntent = new Intent(ProductDetailActivity.this, EditProductActivity.class);
            editIntent.putExtra("id", productId);
            editIntent.putExtra("name", name);
            editIntent.putExtra("brand", brand);
            editIntent.putExtra("price", price);
            editIntent.putExtra("description", description);
            editIntent.putExtra("imageUri", imageUri);
            editProductLauncher.launch(editIntent);
        });

        btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(ProductDetailActivity.this)
                    .setTitle("Confirm")
                    .setMessage("Are you sure?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        DatabaseHelper dbHelper = new DatabaseHelper(ProductDetailActivity.this);
                        if (productId != -1 && dbHelper.deleteProduct(productId)) {
                            Toast.makeText(ProductDetailActivity.this, "Delete Successful", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(ProductDetailActivity.this, "Delete Failed", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    private void updateUI() {
        tvName.setText(name);
        tvBrand.setText(brand);
        tvPrice.setText(price);
        tvDescription.setText(description);
        ivImage.setImageURI(Uri.parse(imageUri));
    }

    private void showOrderDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_order_form, null);
        EditText edtName = dialogView.findViewById(R.id.edtName);
        EditText edtPhone = dialogView.findViewById(R.id.edtPhone);
        EditText edtAddress = dialogView.findViewById(R.id.edtAddress);

        new AlertDialog.Builder(this)
                .setTitle("Information")
                .setView(dialogView)
                .setPositiveButton("Send", (dialog, which) -> {
                    String name = edtName.getText().toString();
                    String phone = edtPhone.getText().toString();
                    String address = edtAddress.getText().toString();

                    // Lưu đơn hàng
                    saveOrderToDatabase(name, phone, address);

                    Toast.makeText(this, "Successful Order", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void saveOrderToDatabase(String customerName, String phone, String address) {
        SharedPreferences sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String username = sharedPref.getString("username", "unknown_user");

        Order order = new Order(
                0,
                username,
                customerName,
                phone,
                address,
                tvName.getText().toString(),
                System.currentTimeMillis(),
                0
        );

        OrderDatabaseHelper dbHelper = new OrderDatabaseHelper(this);
        dbHelper.insertOrder(order);
    }

}
