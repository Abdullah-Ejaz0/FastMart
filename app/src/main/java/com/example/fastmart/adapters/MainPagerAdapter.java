package com.example.fastmart.adapters;
import com.example.fastmart.R;
import com.example.fastmart.fragments.CartFragment;
import com.example.fastmart.fragments.FavouritesFragment;
import com.example.fastmart.fragments.HomeFragment;
import com.example.fastmart.fragments.ProfileFragment;
import com.example.fastmart.fragments.SearchFragment;
import com.example.fastmart.models.*;
import com.example.fastmart.db.*;
import com.example.fastmart.activities.*;

import com.example.fastmart.models.*;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
public class MainPagerAdapter extends FragmentStateAdapter {
    public MainPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position)
        {
            case 1:
                return new SearchFragment();
            case 2:
                return new FavouritesFragment();
            case 3:
                return new CartFragment();
            case 4:
                return new ProfileFragment();
            default:
                return new HomeFragment();
        }
    }
    @Override
    public int getItemCount() {
        return 5;
    }
}







