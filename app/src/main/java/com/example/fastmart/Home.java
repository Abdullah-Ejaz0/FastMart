package com.example.fastmart;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
public class Home extends AppCompatActivity {
    RelativeLayout DOTD, Recom_left, Recom_right;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();
        DOTD.setOnClickListener((v) ->{
            Intent i = new Intent(this, Product.class);
            i.putExtra("Name", "PodMic");
            startActivity(i);
        });
        Recom_left.setOnClickListener((v) ->{
            Intent i = new Intent(this, Product.class);
            i.putExtra("Name", "WH-1000XM4, Black");
            startActivity(i);
        });
        Recom_right.setOnClickListener((v) ->{
            Intent i = new Intent(this, Product.class);
            i.putExtra("Name", "WH-1000XM4, Beige");
            startActivity(i);
        });
    }
    private void init(){
        DOTD = findViewById(R.id.deal);
        Recom_left = findViewById(R.id.recom_left);
        Recom_right = findViewById(R.id.recom_right);
    }
}
