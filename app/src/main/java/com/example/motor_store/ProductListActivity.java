package com.example.motor_store;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseHelper dbHelper;
    ProductAdapter productAdapter;
    Spinner spinnerBrand, spinnerName;
    List<Product> fullProductList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        // Khởi tạo view
        recyclerView = findViewById(R.id.recyclerViewProducts);
        spinnerBrand = findViewById(R.id.spinnerBrandFilter);
        spinnerName = findViewById(R.id.spinnerNameFilter);
        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // Lấy dữ liệu
        dbHelper = new DatabaseHelper(this);
        fullProductList = dbHelper.getAllProducts();

        // Setup RecyclerView
        productAdapter = new ProductAdapter(this, fullProductList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(productAdapter);

        // Setup Spinner: Brand
        List<String> brandList = dbHelper.getAllBrands();
        brandList.add(0, "All Brands");
        ArrayAdapter<String> brandAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, brandList);
        brandAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBrand.setAdapter(brandAdapter);

        // Setup Spinner: Name
        List<String> nameList = dbHelper.getAllProductNames();
        nameList.add(0, "All Products");
        ArrayAdapter<String> nameAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nameList);
        nameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerName.setAdapter(nameAdapter);

        AdapterView.OnItemSelectedListener filterListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterProducts();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        spinnerBrand.setOnItemSelectedListener(filterListener);
        spinnerName.setOnItemSelectedListener(filterListener);
    }

    private void filterProducts() {
        String selectedBrand = spinnerBrand.getSelectedItem().toString();
        String selectedName = spinnerName.getSelectedItem().toString();

        List<Product> filtered = dbHelper.getFilteredProducts(
                selectedBrand.equals("All Brands") ? null : selectedBrand,
                selectedName.equals("All Products") ? null : selectedName
        );
        productAdapter.updateList(filtered);
    }
}
