package com.example.motor_store;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditProductActivity extends AppCompatActivity {

    EditText edtName, edtBrand, edtPrice, edtDescription, edtImageUri;
    Button btnSave, btnCancel;
    ImageView ivPreview;

    int productId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        edtName = findViewById(R.id.edtName);
        edtBrand = findViewById(R.id.edtBrand);
        edtPrice = findViewById(R.id.edtPrice);
        edtDescription = findViewById(R.id.edtDescription);
        edtImageUri = findViewById(R.id.edtImageUri);
        ivPreview = findViewById(R.id.ivPreview);

        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        Intent intent = getIntent();
        productId = intent.getIntExtra("id", -1);
        edtName.setText(intent.getStringExtra("name"));
        edtBrand.setText(intent.getStringExtra("brand"));
        edtPrice.setText(intent.getStringExtra("price"));
        edtDescription.setText(intent.getStringExtra("description"));
        String imageUri = intent.getStringExtra("imageUri");
        edtImageUri.setText(imageUri);
        ivPreview.setImageURI(Uri.parse(imageUri));

        edtImageUri.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String newUri = edtImageUri.getText().toString();
                ivPreview.setImageURI(Uri.parse(newUri));
            }
        });

        btnSave.setOnClickListener(v -> {
            String newName = edtName.getText().toString();
            String newBrand = edtBrand.getText().toString();
            String newPrice = edtPrice.getText().toString();
            String newDesc = edtDescription.getText().toString();
            String newImageUri = edtImageUri.getText().toString();

            double price = 0;
            try {
                price = Double.parseDouble(newPrice);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Giá phải là số hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            Product updatedProduct = new Product(productId, newName, newBrand, price, newDesc, newImageUri);

            DatabaseHelper dbHelper = new DatabaseHelper(this);
            boolean success = dbHelper.updateProduct(updatedProduct);

            if (success) {
                Toast.makeText(this, "Sản phẩm đã được cập nhật", Toast.LENGTH_SHORT).show();
                Intent resultIntent = new Intent();
                resultIntent.putExtra("id", productId);
                resultIntent.putExtra("name", newName);
                resultIntent.putExtra("brand", newBrand);
                resultIntent.putExtra("price", String.valueOf(price));
                resultIntent.putExtra("description", newDesc);
                resultIntent.putExtra("imageUri", newImageUri);
                setResult(RESULT_OK, resultIntent);

                finish();
            } else {
                Toast.makeText(this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancel.setOnClickListener(v -> finish());
    }
}
