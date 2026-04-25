package com.example.fastmart;
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
