package com.example.fastmart;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    public ProfileFragment() {
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextInputEditText name = view.findViewById(R.id.profileName);
        TextInputEditText email = view.findViewById(R.id.profileEmail);
        TextInputEditText dob = view.findViewById(R.id.profileDOB);
        TextInputEditText gender = view.findViewById(R.id.profileGender);
        TextInputEditText phone = view.findViewById(R.id.profilePhone);
        User currentUser = MyApplication.user;
        if (currentUser != null) {
            name.setText(currentUser.getName());
            email.setText(currentUser.getEmail());
            dob.setText("12/05/1998");
            gender.setText(currentUser.getGender());
            phone.setText(currentUser.getCode() + " " + currentUser.getPhone());
        }
        TextView btnLogout = view.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> {
            SharedPreferences sPref = requireContext().getSharedPreferences("User", Context.MODE_PRIVATE);
            sPref.edit().putBoolean("login", false).apply();
            MyApplication.user = null;
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(requireActivity(), Welcome.class);
            startActivity(intent);
            requireActivity().finish();
            Toast.makeText(getContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();
        });
    }
}
