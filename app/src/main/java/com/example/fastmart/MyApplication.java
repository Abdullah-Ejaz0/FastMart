package com.example.fastmart;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.material.badge.BadgeDrawable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MyApplication extends Application {
    public static Set<String> favModels;
    public static HomeFragment homeFragment;
    public static FavouritesFragment favouritesFragment;
    public static CartFragment cartFragment;
    public static Seller_Home sellerHome;
    public static ArrayList<String> favList;
    public static ItemList stock;
    public static ItemList cart;
    public static BadgeDrawable favouritesBadge, cartBadge;
    public static User user;
    public static FavDBManager favDB;
    public static CartDBManager cartDB;

    @Override
    public void onCreate() {
        super.onCreate();
        favDB = new FavDBManager(this);
        favDB.open();

        cartDB = new CartDBManager(this);
        cartDB.open();

        stock = new ItemList();
        stock.populate(); // Start listening to Firebase
        cart = new ItemList();
        
        // Load favourites from SQLite
        ArrayList<items> savedFavs = favDB.getAllFavourites();
        favModels = new HashSet<>();
        favList = new ArrayList<>();
        for (items item : savedFavs) {
            favModels.add(item.getModel());
            favList.add(item.getModel());
        }
    }

    public static void updateCart(){
        if (cartFragment != null && cartFragment.cartAdapter != null){
            cartFragment.cartAdapter.updateData(cartDB.getAllCartItems());
            cartFragment.updateTotal();
        }
        int count = cartDB.getAllCartItems().size();
        if (cartBadge != null) {
            cartBadge.setVisible(count > 0);
            cartBadge.setNumber(count);
        }
    }
    public static void notifyFavouritesChanged() {
        if (homeFragment != null) {
            if (homeFragment.dotdAdapter != null) {
                homeFragment.dotdAdapter.updateList(stock.getDotdProducts());
            }
            if (homeFragment.recomAdapter != null) {
                homeFragment.recomAdapter.updateList(stock.getRecomProducts());
            }
        }

        if (favouritesFragment != null && favouritesFragment.adapter != null) {
            favouritesFragment.adapter.notifyDataSetChanged();
        }

        if (sellerHome != null && sellerHome.sellerAdapter != null) {
            sellerHome.sellerAdapter.notifyDataSetChanged();
        }
    }
}
