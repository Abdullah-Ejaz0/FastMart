package com.example.fastmart;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class WelcomePagerAdapter extends FragmentStateAdapter {

    public WelcomePagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position)
        {
            case 0:
                return new Login();
            case 1:
                return new Signup();
            default:
                return new Login();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
