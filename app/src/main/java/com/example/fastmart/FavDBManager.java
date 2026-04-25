package com.example.fastmart;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import java.util.ArrayList;
public class FavDBManager {
    public static final String DATABASE_NAME = "FavouritesDB";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "favourites";
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
    Context context;
    DBHelper helper;
    public FavDBManager(Context context) {
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
    public long addFavourite(items item) {
        SQLiteDatabase writeDb = helper.getWritableDatabase();
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
        long count = writeDb.insertWithOnConflict(TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        writeDb.close();
        return count;
    }
    public int removeFavourite(String model) {
        SQLiteDatabase writeDb = helper.getWritableDatabase();
        int count = writeDb.delete(TABLE_NAME, COLUMN_MODEL + "=?", new String[]{model});
        writeDb.close();
        return count;
    }
    public ArrayList<items> getAllFavourites() {
        SQLiteDatabase readDb = helper.getReadableDatabase();
        Cursor cursor = readDb.query(TABLE_NAME, null, null, null, null, null, null);
        ArrayList<items> favourites = new ArrayList<>();
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
            do {
                String model = cursor.getString(modelIdx);
                String name = cursor.getString(nameIdx);
                String nPrice = cursor.getString(nPriceIdx);
                String oPrice = cursor.getString(oPriceIdx);
                String desc = cursor.getString(descIdx);
                String sDesc = cursor.getString(sDescIdx);
                String type = cursor.getString(typeIdx);
                String url = cursor.getString(urlIdx);
                int res = cursor.getInt(resIdx);
                boolean dotd = cursor.getInt(dotdIdx) == 1;
                items item = new items(name, model, nPrice, oPrice, desc, sDesc, res, type, dotd);
                item.setImageUrl(url);
                item.setFavourite(true);
                favourites.add(item);
            } while (cursor.moveToNext());
            cursor.close();
        }
        readDb.close();
        return favourites;
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
                    + COLUMN_DOTD + " INTEGER)";
            db.execSQL(query);
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
}
