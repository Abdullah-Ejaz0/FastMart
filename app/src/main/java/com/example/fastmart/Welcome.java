package com.example.fastmart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.textfield.TextInputEditText;

public class Welcome extends AppCompatActivity {
    TabLayout WelcomeTab;
    ViewPager2 WelcomePager;
    WelcomePagerAdapter adapter;
    TabLayoutMediator mediator;

    SharedPreferences sPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

        if(sPref.getBoolean("login", false)){
            startActivity(new Intent(this, Main.class));
            finish();
        }
    }

    private void init(){
        WelcomeTab = findViewById(R.id.welcome_tab);
        WelcomePager = findViewById(R.id.welcome_Pager);
        adapter = new WelcomePagerAdapter(this);
        WelcomePager.setAdapter(adapter);

        mediator = new TabLayoutMediator(
                WelcomeTab,
                WelcomePager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int i) {
                        switch(i){
                            case 0:
                                tab.setText("Login");
                                break;
                            case 1:
                                tab.setText("Signup");
                                break;
                        }
                    }
                }
        );
        mediator.attach();

        sPref = getSharedPreferences("User", MODE_PRIVATE);
    }
}