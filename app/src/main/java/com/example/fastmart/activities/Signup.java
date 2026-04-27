package com.example.fastmart.activities;
import com.example.fastmart.R;
import com.example.fastmart.models.*;
import com.example.fastmart.db.*;

import com.example.fastmart.models.*;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends Fragment {
    Context context;
    MaterialButton Btn;
    TextInputEditText email, pass, cPass;
    SharedPreferences sPref;
    SharedPreferences.Editor editor;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference reference;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    public Signup() {
    }
    public static Signup newInstance(String param1, String param2) {
        Signup fragment = new Signup();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        context = requireContext();
        Btn.setOnClickListener(v -> {
            String Email, Password, cPassword;
            Email = email.getText().toString().trim();
            Password = pass.getText().toString().trim();
            cPassword = cPass.getText().toString().trim();
            if (Email.isEmpty() || Password.isEmpty() || cPassword.isEmpty()) {
                Toast.makeText(context, "Data is missing", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!Password.equals(cPassword)) {
                Toast.makeText(context, "passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }
            auth.createUserWithEmailAndPassword(Email, Password)
                    .addOnSuccessListener(authResult -> {
                        editor.putString("userEmail", Email);
                        editor.putString("userId", auth.getUid());
                        editor.apply();
                        Intent i = new Intent(context, Info_Page.class);
                        i.putExtra("email", Email);
                        i.putExtra("password", Password);
                        startActivity(i);
                        requireActivity().finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });
    }
    private void init(View view){
        Btn = view.findViewById(R.id.signupBtn);
        email = view.findViewById(R.id.tiet_email);
        pass = view.findViewById(R.id.tiet_pass);
        cPass = view.findViewById(R.id.tiet_cpass);
        sPref = view.getContext().getSharedPreferences("User", Context.MODE_PRIVATE);
        editor = sPref.edit();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
    }
}







