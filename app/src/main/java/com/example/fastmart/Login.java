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
 * Use the {@link Login#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Login extends Fragment {
    Context context;
    MaterialButton Btn;
    TextInputEditText email, pass;
    SharedPreferences sPref;
    SharedPreferences.Editor editor;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Login() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Login.
     */
    // TODO: Rename and change types and number of parameters
    public static Login newInstance(String param1, String param2) {
        Login fragment = new Login();
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
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        context = requireContext();
        Btn.setOnClickListener(v -> {
            String Email, Password;
            Email = email.getText().toString().trim();
            Password = pass.getText().toString().trim();

            if (Email.isEmpty() || Password.isEmpty()) {
                Toast.makeText(context, "Data is missing", Toast.LENGTH_SHORT).show();
                return;
            }

            if (Email.equals(sPref.getString("email", "")) && Password.equals(sPref.getString("password", ""))) {
                editor.putBoolean("login", true).commit();
                startActivity(new Intent(context, Main.class));
                requireActivity().finish();
            }else{
                Toast.makeText(context, "Email or Password is incorrect", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init(View view){
        Btn = view.findViewById(R.id.loginBtn);
        email = view.findViewById(R.id.tiet_email);
        pass = view.findViewById(R.id.tiet_pass);
        sPref = view.getContext().getSharedPreferences("User", Context.MODE_PRIVATE);
        editor = sPref.edit();
    }
}