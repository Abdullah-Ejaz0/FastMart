package com.example.fastmart.activities;
import com.example.fastmart.MyApplication;
import com.example.fastmart.R;
import com.example.fastmart.models.*;
import com.example.fastmart.db.*;

import com.example.fastmart.models.*;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.HashSet;
import java.util.Set;
public class Splash extends AppCompatActivity {
    SharedPreferences sPref, userPref;
    SharedPreferences.Editor editor;
    TextView title, slogan;
    Animation titleAnim, sloganAnim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();
        applyAnimation();
        new Handler().postDelayed( () -> {
            navigate();
            }, 4000);
    }
    private void applyAnimation(){
        title.setAnimation(titleAnim);
        slogan.setAnimation(sloganAnim);
    }
    private void navigate(){
        if (userPref.getBoolean("login", false)) {
            String userId = userPref.getString("userId", "");
            String userName = userPref.getString("userName", "");
            String userEmail = userPref.getString("userEmail", "");
            String accountType = userPref.getString("accountType", "");
            String userPhone = userPref.getString("userPhone", "");
            String userGender = userPref.getString("userGender", "");
            String userCountry = userPref.getString("userCountry", "");
            String userAddress = userPref.getString("userAddress", "");
            MyApplication.user = new User(userEmail, userName, accountType, "", userPhone, userGender, userCountry, userAddress);
            if ("Seller".equalsIgnoreCase(accountType)) {
                startActivity(new Intent(this, Seller_Home.class));
            } else {
                startActivity(new Intent(this, Main.class));
            }
        } else {
            startActivity(new Intent(this, OnBoarding.class));
        }
        finish();
    }
    private void init(){
        sPref = getSharedPreferences("FavouritesPrefs", Context.MODE_PRIVATE);
        userPref = getSharedPreferences("User", Context.MODE_PRIVATE);
        editor = sPref.edit();
        Set<String> favModels = new HashSet<>(sPref.getStringSet("favModels", new HashSet<>()));
        MyApplication.stock.markFavourite(favModels);
        title = findViewById(R.id.title);
        slogan = findViewById(R.id.slogan);
        titleAnim = AnimationUtils.loadAnimation(this, R.anim.title_anim);
        sloganAnim = AnimationUtils.loadAnimation(this, R.anim.slogan_anim);
    }
}







