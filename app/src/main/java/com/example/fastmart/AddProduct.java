package com.example.fastmart;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddProduct extends AppCompatActivity {

    TextInputEditText etProductName, etProductType, etProductPrice, etProductDescription;
    Button btnAddProduct;

    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();
    }

    private void init() {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("products");

        etProductName = findViewById(R.id.etProductName);
        etProductType = findViewById(R.id.etProductType);
        etProductPrice = findViewById(R.id.etProductPrice);
        etProductDescription = findViewById(R.id.etProductDescription);
        btnAddProduct = findViewById(R.id.btnAddProduct);

        btnAddProduct.setOnClickListener(v -> saveProduct());
    }

    private void saveProduct() {
        String name = etProductName.getText().toString().trim();
        String type = etProductType.getText().toString().trim();
        String price = etProductPrice.getText().toString().trim();
        String description = etProductDescription.getText().toString().trim();

        if (name.isEmpty() || type.isEmpty() || price.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference dbRef = database.getReference("products");
        String productId = dbRef.push().getKey();

        if (productId != null) {
            String priceWithSymbol = "$" + price;
            items newItem = new items(
                    name,
                    productId,
                    priceWithSymbol,
                    priceWithSymbol,
                    description,
                    type,
                    0,
                    type,
                    false
            );
            newItem.setImageUrl("");
            newItem.sellerId = user.getUid();

            dbRef.child(productId).setValue(newItem)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(AddProduct.this, "Product Added Successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(AddProduct.this, "Failed to add product: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }
}