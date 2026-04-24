package com.example.fastmart;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Signup#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Signup extends Fragment {
    Context context;
    MaterialButton Btn;
    TextInputEditText email, pass, cPass;
    SharedPreferences sPref;
    SharedPreferences.Editor editor;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Signup() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Signup.
     */
    // TODO: Rename and change types and number of parameters
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
        // Inflate the layout for this fragment
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

            editor.putString("email", Email)
                    .putString("password", Password)
                    .putBoolean("login", true)
                    .commit();
            startActivity(new Intent(context, Main.class));
            requireActivity().finish();
        });
    }

    private void init(View view){
        Btn = view.findViewById(R.id.signupBtn);
        email = view.findViewById(R.id.tiet_email);
        pass = view.findViewById(R.id.tiet_pass);
        cPass = view.findViewById(R.id.tiet_cpass);
        sPref = view.getContext().getSharedPreferences("User", Context.MODE_PRIVATE);
        editor = sPref.edit();
    }
}