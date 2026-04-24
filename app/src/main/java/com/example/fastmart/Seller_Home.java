package com.example.fastmart;

import android.content.Intent;
import android.os.Bundle;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class Seller_Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView rvTotalProducts;
    SellerAdapter sellerAdapter;
    TextView tvHello;
    FloatingActionButton fabAddProduct;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_seller_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        MyApplication.sellerHome = this;
        init();
        setupDrawer();
    }

    private void init() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        tvHello = findViewById(R.id.tvHello);
        rvTotalProducts = findViewById(R.id.rvTotalProducts);
        fabAddProduct = findViewById(R.id.fabAddProduct);

        // Setting user name from MyApplication.user
        String name = "User";
        if (MyApplication.user != null) {
            name = MyApplication.user.getName();
        }
        tvHello.setText(getString(R.string.hello_user, name));

        rvTotalProducts.setHasFixedSize(true);
        sellerAdapter = new SellerAdapter(this, MyApplication.stock.getProducts());
        rvTotalProducts.setAdapter(sellerAdapter);
        rvTotalProducts.setLayoutManager(new GridLayoutManager(this, 2));

        fabAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Logic to add a new product
            }
        });
    }

    private void setupDrawer() {
        navigationView.setNavigationItemSelectedListener(this);

        // Setup Header Data
        View headerView = navigationView.getHeaderView(0);
        if (headerView == null) {
            headerView = findViewById(R.id.drawer_content);
        }

        TextView navUserName = headerView.findViewById(R.id.nav_user_name);
        TextView navUserEmail = headerView.findViewById(R.id.nav_user_email);

        if (MyApplication.user != null) {
            if (navUserName != null) navUserName.setText(MyApplication.user.getName());
            if (navUserEmail != null) navUserEmail.setText(MyApplication.user.getEmail());
        }

        // Setup Theme Switcher logic
        SharedPreferences themePrefs = getSharedPreferences("ThemePrefs", Context.MODE_PRIVATE);
        boolean isDarkMode = themePrefs.getBoolean("isDarkMode", false);
        
        TextView btnLight = findViewById(R.id.btnLightTheme);
        TextView btnDark = findViewById(R.id.btnDarkTheme);

        updateThemeButtons(isDarkMode, btnLight, btnDark);

        btnLight.setOnClickListener(v -> {
            if (themePrefs.getBoolean("isDarkMode", false)) {
                setThemeMode(false);
            }
        });

        btnDark.setOnClickListener(v -> {
            if (!themePrefs.getBoolean("isDarkMode", false)) {
                setThemeMode(true);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            findViewById(R.id.home_content).setVisibility(View.VISIBLE);
            findViewById(R.id.fabAddProduct).setVisibility(View.VISIBLE);
            // Remove the fragment if it exists
            androidx.fragment.app.Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if (fragment != null) {
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
        } else if (id == R.id.nav_order_history) {
            startActivity(new Intent(this, OrderHistory.class));
        } else if (id == R.id.nav_account_settings) {
            findViewById(R.id.home_content).setVisibility(View.GONE);
            findViewById(R.id.fabAddProduct).setVisibility(View.GONE);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new ProfileFragment())
                    .addToBackStack(null)
                    .commit();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void updateThemeButtons(boolean isDarkMode, TextView btnLight, TextView btnDark) {
        if (isDarkMode) {
            btnDark.setBackgroundResource(R.drawable.tab_background);
            btnLight.setBackground(null);
        } else {
            btnLight.setBackgroundResource(R.drawable.tab_background);
            btnDark.setBackground(null);
        }
    }

    private void setThemeMode(boolean isDarkMode) {
        SharedPreferences themePrefs = getSharedPreferences("ThemePrefs", Context.MODE_PRIVATE);
        themePrefs.edit().putBoolean("isDarkMode", isDarkMode).apply();
        
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        recreate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.sellerHome = null;
    }
}
