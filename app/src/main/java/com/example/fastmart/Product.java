package com.example.fastmart;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class Product extends AppCompatActivity {
    items product;
    ItemList stock;
    ImageView backbtn, img;
    TextView price, name, model, description;
    MaterialButton buybtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

        product = findProduct();

        if (product != null){
            img.setImageResource(product.image);
            price.setText(product.newPrice);
            name.setText(product.name);
            model.setText(product.model);
            description.setText(product.description);
        }

        backbtn.setOnClickListener((v) -> {
            startActivity(new Intent(this, Main.class));
            finish();
        });

        buybtn.setOnClickListener((v) -> {
            showDialog();
        });

    }

    private void showDialog(){
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_alert, null);

        AlertDialog alert = new AlertDialog.Builder(this)
                .setView(dialogView)
                .create();

        TextView info;
        MaterialButton btnYes, btnNo;

        info = dialogView.findViewById(R.id.dialog_info);
        btnYes = dialogView.findViewById(R.id.dialog_yes);
        btnNo = dialogView.findViewById(R.id.dialog_no);

        String text = "Are you sure you want to buy " + product.name + " Model: " + product.model + "?";

        info.setText(text);

        btnYes.setOnClickListener((v) -> {
            MyApplication.cart.addItem(product);
            MyApplication.updateCart();
            MyApplication.cartFragment.updateTotal();
            alert.dismiss();
        });

        btnNo.setOnClickListener((v) -> {
            alert.dismiss();
        });

        alert.show();
    }
    private items findProduct(){
        Intent i = getIntent();
        String model = i.getStringExtra("Name");
        return stock.getProduct(model);
    }
    private void init(){
        stock = new ItemList();

        backbtn = findViewById(R.id.prod_return);
        img = findViewById(R.id.prod_img);

        price = findViewById(R.id.prod_price);
        name = findViewById(R.id.prod_name);
        model = findViewById(R.id.prod_model);
        description = findViewById(R.id.prod_desc);

        buybtn = findViewById(R.id.buybtn);

        stock = MyApplication.stock;
        description.setMovementMethod(new android.text.method.ScrollingMovementMethod());
    }
}