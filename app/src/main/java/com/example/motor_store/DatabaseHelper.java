package com.example.motor_store;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "motorstore.db";
    private static final int DATABASE_VERSION = 4;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT UNIQUE, " +
                "password TEXT, " +
                "role TEXT)");

        ContentValues admin = new ContentValues();
        admin.put("username", "admin");
        admin.put("password", "admin123");
        admin.put("role", "admin");
        db.insert("users", null, admin);

        db.execSQL("CREATE TABLE products(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "brand TEXT, " +
                "price REAL, " +
                "description TEXT, " +
                "image_uri TEXT" +
                ")");

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS products");
        onCreate(db);  // Tạo lại tất cả các bảng
    }


    public Boolean insertUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        values.put("role", "user");
        long result = db.insert("users", null, values);
        return result != -1;
    }


    public Boolean checkUserExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE username = ?", new String[]{username});
        Boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public boolean checkUserLogin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE username = ? AND password = ?", new String[]{username, password});
        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        return isValid;
    }

    public String getUserRole(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT role FROM users WHERE username = ?", new String[]{username});
        if (cursor.moveToFirst()) {
            String role = cursor.getString(0);
            cursor.close();
            return role;
        }
        cursor.close();
        return null;
    }

    public boolean addProduct(String name, String brand, double price, String description, String imageUri) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("brand", brand);
        values.put("price", price);
        values.put("description", description);
        values.put("image_uri", imageUri);
        long result = db.insert("products", null, values);
        return result != -1;
    }

    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM products", null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String brand = cursor.getString(2);
                double price = cursor.getDouble(3);
                String description = cursor.getString(4);
                String imageUri = cursor.getString(5);
                productList.add(new Product(id, name, brand, price, description, imageUri));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return productList;
    }

    public boolean updateProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", product.getName());
        values.put("brand", product.getBrand());
        values.put("price", product.getPrice());
        values.put("description", product.getDescription());
        values.put("image_uri", product.getImageUri());

        int rowsAffected = db.update("products", values, "id=?", new String[]{String.valueOf(product.getId())});
        db.close();
        return rowsAffected > 0;
    }

    public boolean deleteProduct(int productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete("products", "id=?", new String[]{String.valueOf(productId)});
        db.close();
        return rowsDeleted > 0;
    }

    public List<String> getAllBrands() {
        List<String> brands = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT brand FROM products", null);
        if (cursor.moveToFirst()) {
            do {
                brands.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return brands;
    }

    public List<String> getAllProductNames() {
        List<String> names = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT name FROM products", null);
        if (cursor.moveToFirst()) {
            do {
                names.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return names;
    }

    public List<Product> getFilteredProducts(String brand, String name) {
        List<Product> filteredList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM products WHERE 1=1";
        List<String> argsList = new ArrayList<>();

        if (brand != null) {
            query += " AND brand=?";
            argsList.add(brand);
        }
        if (name != null) {
            query += " AND name=?";
            argsList.add(name);
        }

        Cursor cursor = db.rawQuery(query, argsList.toArray(new String[0]));
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String pName = cursor.getString(1);
                String pBrand = cursor.getString(2);
                double price = cursor.getDouble(3);
                String description = cursor.getString(4);
                String imageUri = cursor.getString(5);
                filteredList.add(new Product(id, pName, pBrand, price, description, imageUri));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return filteredList;
    }


}
