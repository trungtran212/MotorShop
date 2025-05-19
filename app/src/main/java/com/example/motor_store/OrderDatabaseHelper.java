package com.example.motor_store;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class OrderDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "motor_store.db";
    private static final int DATABASE_VERSION = 4;

    private static final String TABLE_ORDERS = "orders";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_NAME = "customerName";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_ADDRESS = "address";
    private static final String COLUMN_PRODUCT = "productName";
    private static final String COLUMN_TIMESTAMP = "timestamp";
    private static final String COLUMN_STATUS = "status";

    public OrderDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_ORDERS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT NOT NULL, " +
                COLUMN_NAME + " TEXT NOT NULL, " +
                COLUMN_PHONE + " TEXT NOT NULL, " +
                COLUMN_ADDRESS + " TEXT NOT NULL, " +
                COLUMN_PRODUCT + " TEXT NOT NULL, " +
                COLUMN_TIMESTAMP + " INTEGER NOT NULL, " +
                COLUMN_STATUS + " INTEGER DEFAULT 0" +
                ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        onCreate(db);
    }

    public void insertOrder(Order order) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, order.getUsername());
        values.put(COLUMN_NAME, order.getCustomerName());
        values.put(COLUMN_PHONE, order.getPhone());
        values.put(COLUMN_ADDRESS, order.getAddress());
        values.put(COLUMN_PRODUCT, order.getProductName());
        values.put(COLUMN_TIMESTAMP, order.getTimestamp());
        values.put(COLUMN_STATUS, order.getStatus());
        db.insert(TABLE_ORDERS, null, values);
        db.close();
    }

    public List<Order> getAllOrders() {
        List<Order> orderList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ORDERS, null, null, null, null, null, COLUMN_TIMESTAMP + " DESC");

        if (cursor.moveToFirst()) {
            do {
                orderList.add(cursorToOrder(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return orderList;
    }

    public List<Order> getOrdersByUsername(String username) {
        List<Order> orderList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ORDERS, null, COLUMN_USERNAME + "=?", new String[]{username}, null, null, COLUMN_TIMESTAMP + " DESC");

        if (cursor.moveToFirst()) {
            do {
                orderList.add(cursorToOrder(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return orderList;
    }

    public void updateOrderStatus(int id, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STATUS, status);
        db.update(TABLE_ORDERS, values, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    private Order cursorToOrder(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
        String username = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME));
        String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
        String phone = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE));
        String address = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS));
        String product = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT));
        long timestamp = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_TIMESTAMP));
        int status = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STATUS));
        return new Order(id, username, name, phone, address, product, timestamp, status);
    }
}
