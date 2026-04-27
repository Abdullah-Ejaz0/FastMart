package com.example.fastmart.fragments;
import com.example.fastmart.MyApplication;
import com.example.fastmart.R;
import com.example.fastmart.adapters.SearchAdapter;
import com.example.fastmart.models.*;
import com.example.fastmart.db.*;
import com.example.fastmart.activities.*;

import com.example.fastmart.models.*;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import java.util.HashSet;
import java.util.Set;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {
    TextInputEditText searchBar;
    Set<String> searchHistory;
    ImageView backArrow;
    RecyclerView rvSearch;
    SearchAdapter searchAdapter;
    SharedPreferences sPref;
    SharedPreferences.Editor editor;
    TextView clear_btn;
    boolean isDialogShowing = false;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    public SearchFragment() {
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
        return inflater.inflate(R.layout.fragment_search, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        clear_btn.setOnClickListener(v -> {
            editor.clear().commit();
            searchAdapter.clearall();
        });
        searchBar.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(searchBar, InputMethodManager.SHOW_IMPLICIT);
            }
        });
        searchBar.setOnEditorActionListener((v, actionId, event) -> {
            boolean isSearchAction = actionId == EditorInfo.IME_ACTION_SEARCH
                    || actionId == EditorInfo.IME_ACTION_DONE
                    || actionId == EditorInfo.IME_ACTION_GO;
            boolean isEnterKey = event != null
                    && event.getKeyCode() == KeyEvent.KEYCODE_ENTER
                    && event.getAction() == KeyEvent.ACTION_DOWN;
            if (isSearchAction || isEnterKey) {
                handleSearchQuery(searchBar.getText().toString(), v);
                return true;
            }
            return false;
        });
        backArrow.setOnClickListener(v -> {
            InputMethodManager imm = (InputMethodManager) requireContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchBar.getWindowToken(), 0);
            searchBar.clearFocus();
        });
    }
    private void init(View view){
        sPref = view.getContext().getSharedPreferences("search_history", Context.MODE_PRIVATE);
        editor = sPref.edit();
        searchHistory = sPref.getStringSet("Search_hash_key", new HashSet<>());
        searchHistory = new HashSet<>(searchHistory);
        backArrow = view.findViewById(R.id.back_arrow);
        searchBar = view.findViewById(R.id.search_tab);
        clear_btn = view.findViewById(R.id.clear_Search);
        rvSearch = view.findViewById(R.id.rv_search);
        searchAdapter = new SearchAdapter(view.getContext(), searchHistory);
        rvSearch.setHasFixedSize(true);
        rvSearch.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
        rvSearch.setAdapter(searchAdapter);
    }
    private void handleSearchQuery(String query, View parentView) {
        if (query == null || query.trim().isEmpty() || isDialogShowing) return;
        query = query.trim();
        isDialogShowing = true;
        searchHistory.add(query);
        editor.putStringSet("Search_hash_key", searchHistory).commit();
        View dialogView = LayoutInflater.from(parentView.getContext())
                .inflate(R.layout.search_result_dialogue, null);
        AlertDialog dialog = new AlertDialog.Builder(parentView.getContext())
                .setView(dialogView)
                .create();
        TextView info = dialogView.findViewById(R.id.dialog_info);
        MaterialButton ok = dialogView.findViewById(R.id.dialog_yes);
        MaterialButton cancel = dialogView.findViewById(R.id.dialog_no);
        ok.setOnClickListener(v -> dialog.dismiss());
        cancel.setOnClickListener(v -> dialog.dismiss());
        if(MyApplication.stock.getProduct(query) != null){
            info.setText("Product Found");
        } else {
            info.setText("Product Not Found");
        }
        dialog.setOnDismissListener(d -> isDialogShowing = false);
        dialog.show();
        searchAdapter.refreshData();
    }
}







