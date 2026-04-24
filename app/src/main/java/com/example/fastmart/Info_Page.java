package com.example.fastmart;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Info_Page extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;

    FirebaseDatabase fbDatabase;
    DatabaseReference dbReference;
    TextInputEditText etFullName, etAccountType, etPhoneNumber, etCountry, etAddress, etCountryCode;
    MaterialButtonToggleGroup genderToggleGroup;
    Button btnMale, btnFemale;
    CheckBox cbTerms;
    MaterialButton btnSaveProfile;
    String email, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_info_page);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();
        setupGenderToggle();

        btnSaveProfile.setOnClickListener(v -> {
            collectInputsAndSave();
        });
    }

    private void init() {
        Intent i = getIntent();
        email = i.getStringExtra("email");
        password = i.getStringExtra("password");

        auth = FirebaseAuth.getInstance();
        auth.getFirebaseAuthSettings().setAppVerificationDisabledForTesting(true);
        user = auth.getCurrentUser();

        fbDatabase = FirebaseDatabase.getInstance();
        dbReference = fbDatabase.getReference("Users");

        etFullName = findViewById(R.id.etFullName);
        etAccountType = findViewById(R.id.etAccountType);
        etCountryCode = findViewById(R.id.countryCode);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        genderToggleGroup = findViewById(R.id.genderToggleGroup);
        btnMale = findViewById(R.id.btnMale);
        btnFemale = findViewById(R.id.btnFemale);
        etCountry = findViewById(R.id.etCountry);
        etAddress = findViewById(R.id.etAddress);
        cbTerms = findViewById(R.id.cbTerms);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);
    }

    private void setupGenderToggle() {
        genderToggleGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                if (checkedId == R.id.btnMale) {
                    btnMale.setTextColor(Color.BLACK);
                    btnFemale.setTextColor(Color.GRAY);
                } else if (checkedId == R.id.btnFemale) {
                    btnFemale.setTextColor(Color.BLACK);
                    btnMale.setTextColor(Color.GRAY);
                }
            }
        });
    }

    private void collectInputsAndSave() {
        String fullName = etFullName.getText().toString().trim();
        String accountType = etAccountType.getText().toString().trim();
        String code = etCountryCode.getText().toString().trim();
        String phone = etPhoneNumber.getText().toString().trim();
        String country = etCountry.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        boolean agreed = cbTerms.isChecked();

        // Get selected gender
        int checkedId = genderToggleGroup.getCheckedButtonId();
        String gender = "";
        if (checkedId == R.id.btnMale) {
            gender = "Male";
        } else if (checkedId == R.id.btnFemale) {
            gender = "Female";
        }

        // Validation
        if (fullName.isEmpty() || accountType.isEmpty() || code.isEmpty() || phone.isEmpty() ||
                country.isEmpty() || address.isEmpty() || gender.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!agreed) {
            Toast.makeText(this, "Please agree to the Terms and Conditions", Toast.LENGTH_SHORT).show();
            return;
        }

        MyApplication.user = new User(email, password, fullName, accountType, code, phone, gender, country, address);

        if (user != null) {
            dbReference.child(user.getUid()).setValue(MyApplication.user)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(Info_Page.this, "Profile Saved Successful!", Toast.LENGTH_SHORT).show();
                        user.sendEmailVerification();
                        if ("Seller".equalsIgnoreCase(accountType)) {
                            startActivity(new Intent(Info_Page.this, Seller_Home.class));
                        } else {
                            startActivity(new Intent(Info_Page.this, Main.class));
                        }
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(Info_Page.this, "Database Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("Database Error", e.getMessage());
                    });
        } else {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
        }
    }
}
