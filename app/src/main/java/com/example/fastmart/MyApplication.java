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
    public static BadgeDrawable favouritesBadge;
    public static User user;

    @Override
    public void onCreate() {
        super.onCreate();
        stock = new ItemList();
        stock.populate(); // Start listening to Firebase
        cart = new ItemList();
        SharedPreferences sPref = getSharedPreferences("FavouritesPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sPref.edit();
        favModels = new HashSet<>(sPref.getStringSet("favModels", new HashSet<>()));
        favList = new ArrayList<>(favModels);
    }

    public static void updateCart(){
        if (cartFragment != null && cartFragment.cartAdapter != null){
            cartFragment.cartAdapter.notifyDataSetChanged();
            cartFragment.updateTotal();
        }
    }
    public static void notifyFavouritesChanged() {
        if (homeFragment != null && homeFragment.dotdAdapter != null && homeFragment.recomAdapter != null) {
            homeFragment.dotdAdapter.notifyDataSetChanged();
            homeFragment.recomAdapter.notifyDataSetChanged();
        }

        if (favouritesFragment != null && favouritesFragment.adapter != null) {
            favouritesFragment.adapter.notifyDataSetChanged();
        }

        if (sellerHome != null && sellerHome.sellerAdapter != null) {
            sellerHome.sellerAdapter.notifyDataSetChanged();
        }
    }
}
