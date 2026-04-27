package com.example.fastmart.db;
import com.example.fastmart.R;
import com.example.fastmart.models.*;
import com.example.fastmart.activities.*;

import com.example.fastmart.models.*;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import java.util.ArrayList;
public class CartDBManager {
    public static final String DATABASE_NAME = "CartDB";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "cart";
    public static final String COLUMN_MODEL = "model";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_NEW_PRICE = "newPrice";
    public static final String COLUMN_ORIGINAL_PRICE = "originalPrice";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_SDESC = "sDesc";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_IMAGE_URL = "imageUrl";
    public static final String COLUMN_IMAGE_RES = "imageRes";
    public static final String COLUMN_DOTD = "dotd";
    public static final String COLUMN_QUANTITY = "quantity";
    Context context;
    DBHelper helper;
    public CartDBManager(Context context) {
        this.context = context;
    }
    public void open() {
        helper = new DBHelper(context);
    }
    public void close() {
        if (helper != null) {
            helper.close();
        }
    }
    public long addItem(items item) {
        SQLiteDatabase writeDb = helper.getWritableDatabase();
        Cursor cursor = writeDb.query(TABLE_NAME, null, COLUMN_MODEL + "=?", new String[]{item.getModel()}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int qtyColIdx = cursor.getColumnIndex(COLUMN_QUANTITY);
            int currentQty = 1;
            if (qtyColIdx != -1) {
                currentQty = cursor.getInt(qtyColIdx);
            }
            cursor.close();
            return updateQuantity(item.getModel(), currentQty + 1);
        }
        if (cursor != null) cursor.close();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_MODEL, item.getModel());
        cv.put(COLUMN_NAME, item.getName());
        cv.put(COLUMN_NEW_PRICE, item.getNewPrice());
        cv.put(COLUMN_ORIGINAL_PRICE, item.getOriginalPrice());
        cv.put(COLUMN_DESCRIPTION, item.getDescription());
        cv.put(COLUMN_SDESC, item.getsDesc());
        cv.put(COLUMN_TYPE, item.getType());
        cv.put(COLUMN_IMAGE_URL, item.getImageUrl());
        cv.put(COLUMN_IMAGE_RES, item.getImage());
        cv.put(COLUMN_DOTD, item.isDotd() ? 1 : 0);
        cv.put(COLUMN_QUANTITY, item.getQuantity() > 0 ? item.getQuantity() : 1);
        long count = writeDb.insert(TABLE_NAME, null, cv);
        writeDb.close();
        return count;
    }
    public int updateQuantity(String model, int quantity) {
        SQLiteDatabase writeDb = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_QUANTITY, quantity);
        int count = writeDb.update(TABLE_NAME, cv, COLUMN_MODEL + "=?", new String[]{model});
        writeDb.close();
        return count;
    }
    public int removeItem(String model) {
        SQLiteDatabase writeDb = helper.getWritableDatabase();
        int count = writeDb.delete(TABLE_NAME, COLUMN_MODEL + "=?", new String[]{model});
        writeDb.close();
        return count;
    }
    public void clearCart() {
        SQLiteDatabase writeDb = helper.getWritableDatabase();
        writeDb.delete(TABLE_NAME, null, null);
        writeDb.close();
    }
    public ArrayList<items> getAllCartItems() {
        SQLiteDatabase readDb = helper.getReadableDatabase();
        Cursor cursor = readDb.query(TABLE_NAME, null, null, null, null, null, null);
        ArrayList<items> cartItems = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            int modelIdx = cursor.getColumnIndex(COLUMN_MODEL);
            int nameIdx = cursor.getColumnIndex(COLUMN_NAME);
            int nPriceIdx = cursor.getColumnIndex(COLUMN_NEW_PRICE);
            int oPriceIdx = cursor.getColumnIndex(COLUMN_ORIGINAL_PRICE);
            int descIdx = cursor.getColumnIndex(COLUMN_DESCRIPTION);
            int sDescIdx = cursor.getColumnIndex(COLUMN_SDESC);
            int typeIdx = cursor.getColumnIndex(COLUMN_TYPE);
            int urlIdx = cursor.getColumnIndex(COLUMN_IMAGE_URL);
            int resIdx = cursor.getColumnIndex(COLUMN_IMAGE_RES);
            int dotdIdx = cursor.getColumnIndex(COLUMN_DOTD);
            int qtyIdx = cursor.getColumnIndex(COLUMN_QUANTITY);
            do {
                items item = new items(
                        cursor.getString(nameIdx),
                        cursor.getString(modelIdx),
                        cursor.getString(nPriceIdx),
                        cursor.getString(oPriceIdx),
                        cursor.getString(descIdx),
                        cursor.getString(sDescIdx),
                        cursor.getInt(resIdx),
                        cursor.getString(typeIdx),
                        cursor.getInt(dotdIdx) == 1
                );
                item.setImageUrl(cursor.getString(urlIdx));
                item.setQuantity(cursor.getInt(qtyIdx));
                cartItems.add(item);
            } while (cursor.moveToNext());
            cursor.close();
        }
        readDb.close();
        return cartItems;
    }
    private class DBHelper extends SQLiteOpenHelper {
        public DBHelper(@Nullable Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            String query = "CREATE TABLE " + TABLE_NAME + " ("
                    + COLUMN_MODEL + " TEXT PRIMARY KEY, "
                    + COLUMN_NAME + " TEXT, "
                    + COLUMN_NEW_PRICE + " TEXT, "
                    + COLUMN_ORIGINAL_PRICE + " TEXT, "
                    + COLUMN_DESCRIPTION + " TEXT, "
                    + COLUMN_SDESC + " TEXT, "
                    + COLUMN_TYPE + " TEXT, "
                    + COLUMN_IMAGE_URL + " TEXT, "
                    + COLUMN_IMAGE_RES + " INTEGER, "
                    + COLUMN_DOTD + " INTEGER, "
                    + COLUMN_QUANTITY + " INTEGER)";
            db.execSQL(query);
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
}







