package com.example.motor_store;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AddProductActivity extends AppCompatActivity {

    EditText etName, etBrand, etPrice, etDescription;
    Button btnAdd, btnChooseImage;
    ImageView ivProductImage;
    String imageUri = "";

    DatabaseHelper db;

    private final ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    if (uri != null) {
                        // 🔐 Lưu quyền truy cập
                        getContentResolver().takePersistableUriPermission(
                                uri, Intent.FLAG_GRANT_READ_URI_PERMISSION
                        );
                        imageUri = uri.toString();
                        ivProductImage.setImageURI(uri);
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        db = new DatabaseHelper(this);

        etName = findViewById(R.id.etProductName);
        etBrand = findViewById(R.id.etBrand);
        etPrice = findViewById(R.id.etPrice);
        etDescription = findViewById(R.id.etDescription);
        btnAdd = findViewById(R.id.btnAddProduct);
        btnChooseImage = findViewById(R.id.btnChooseImage);
        ivProductImage = findViewById(R.id.ivProductImage);
        Button btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> {
            finish(); // đóng activity này, quay lại trước đó
        });

        btnChooseImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });

        btnAdd.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String brand = etBrand.getText().toString().trim();
            String priceStr = etPrice.getText().toString().trim();
            String description = etDescription.getText().toString().trim();

            if (name.isEmpty() || brand.isEmpty() || priceStr.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            double price = Double.parseDouble(priceStr);

            boolean inserted = db.addProduct(name, brand, price, description, imageUri);

            if (inserted) {
                Toast.makeText(this, "Product added successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to add product", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
