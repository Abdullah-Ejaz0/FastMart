package com.example.fastmart.fragments;
import com.example.fastmart.MyApplication;
import com.example.fastmart.R;
import com.example.fastmart.adapters.DotdAdapter;
import com.example.fastmart.adapters.RecomAdapter;
import com.example.fastmart.models.*;
import com.example.fastmart.db.*;
import com.example.fastmart.activities.*;

import com.example.fastmart.models.*;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.HashSet;
import java.util.Set;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    RecyclerView rvDOTD, rvRecom;
    public DotdAdapter dotdAdapter;
    public RecomAdapter recomAdapter;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    public HomeFragment() {
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MyApplication.homeFragment = this;
        init(view);
    }
    private void init(View view){
        rvDOTD = view.findViewById(R.id.rvDOTD);
        rvRecom = view.findViewById(R.id.rvRecom);
        rvDOTD.setHasFixedSize(true);
        rvRecom.setHasFixedSize(true);
        
        dotdAdapter = new DotdAdapter(view.getContext(), MyApplication.stock.getDotdProducts());
        rvDOTD.setAdapter(dotdAdapter);
        rvDOTD.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));

        recomAdapter = new RecomAdapter(view.getContext(), MyApplication.stock.getRecomProducts());
        rvRecom.setAdapter(recomAdapter);
        rvRecom.setLayoutManager(new GridLayoutManager(view.getContext(), 2));

        dotdAdapter.updateList(MyApplication.stock.getDotdProducts());
        recomAdapter.updateList(MyApplication.stock.getRecomProducts());
    }
}







