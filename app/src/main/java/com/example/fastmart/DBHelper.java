package com.example.fastmart;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
public class DBHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "FastMart.db";
    public static final String TABLE_CART = "cart";
    public static final String COL_MODEL = "model";
    public static final String COL_NAME = "name";
    public static final String COL_PRICE = "price";
    public static final String COL_IMAGE = "image";
    public static final String COL_QTY = "quantity";
    public DBHelper(Context context) {
        super(context, DBNAME, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create Table " + TABLE_CART + "(" + COL_MODEL + " TEXT primary key, " +
                COL_NAME + " TEXT, " + COL_PRICE + " TEXT, " + COL_IMAGE + " INTEGER, " + COL_QTY + " INTEGER)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop Table if exists " + TABLE_CART);
    }
    public boolean addToCart(items item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_MODEL, item.getModel());
        contentValues.put(COL_NAME, item.getName());
        contentValues.put(COL_PRICE, item.isDotd() ? item.getNewPrice() : item.getOriginalPrice());
        contentValues.put(COL_IMAGE, item.getImage());
        contentValues.put(COL_QTY, 1);
        long result = db.insertWithOnConflict(TABLE_CART, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        return result != -1;
    }
    public void updateQuantity(String model, int qty) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_QTY, qty);
        db.update(TABLE_CART, cv, COL_MODEL + "=?", new String[]{model});
    }
    public void deleteFromCart(String model) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, COL_MODEL + "=?", new String[]{model});
    }
    public void clearCart() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_CART);
    }
    public ArrayList<items> getCartItems() {
        ArrayList<items> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + TABLE_CART, null);
        if (cursor.moveToFirst()) {
            do {
                        items item = new items(
                        cursor.getString(1),
                        cursor.getString(0),
                        cursor.getString(2),
                        cursor.getString(2),
                        "", "",
                        cursor.getInt(3),
                        "", false
                );
                for(int i=1; i<cursor.getInt(4); i++) item.quantity++;
                list.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
}
