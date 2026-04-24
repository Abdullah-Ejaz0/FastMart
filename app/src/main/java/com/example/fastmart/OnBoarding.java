package com.example.fastmart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

public class OnBoarding extends AppCompatActivity {
    MaterialButton onBoardBtn;

    SharedPreferences sPref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_on_boarding);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();
    }

    private void init(){
        onBoardBtn = findViewById(R.id.onBoardBtn);

        sPref = getSharedPreferences("User", MODE_PRIVATE);
        editor = sPref.edit();

        if(!sPref.getBoolean("First", true)){
            startActivity(new Intent(this, Welcome.class));
            finish();
        }
        onBoardBtn.setOnClickListener(v -> {
            editor.putBoolean("First", false).commit();
            startActivity(new Intent(this, Welcome.class));
            finish();
        });
    }
}