package com.example.fastmart.activities;
import com.example.fastmart.MyApplication;
import com.example.fastmart.R;
import com.example.fastmart.adapters.MainPagerAdapter;
import com.example.fastmart.models.*;
import com.example.fastmart.db.*;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
public class Main extends AppCompatActivity {
    TabLayout MainTab;
    ViewPager2 MainPager;
    MainPagerAdapter adapter;
    TabLayoutMediator mediator;
    FragmentManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();
    }
    private void init(){
        manager = getSupportFragmentManager();
        MainTab = findViewById(R.id.MainTab);
        MainPager = findViewById(R.id.MainPager);
        findViewById(R.id.fabChat).setOnClickListener(v -> {
            startActivity(new android.content.Intent(Main.this, ChatActivity.class));
        });
        adapter = new MainPagerAdapter(this);
        MainPager.setAdapter(adapter);
        mediator = new TabLayoutMediator(
                MainTab,
                MainPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int i) {
                        switch (i)
                        {
                            case 0:
                                tab.setText("Home");
                                tab.setIcon(R.drawable.ic_home);
                                break;
                            case 1:
                                tab.setText("Browse");
                                tab.setIcon(R.drawable.ic_home);
                                break;
                            case 2:
                                tab.setText("Favourites");
                                tab.setIcon(R.drawable.ic_home);
                                MyApplication.favouritesBadge = tab.getOrCreateBadge();
                                int number = MyApplication.favModels.size();
                                MyApplication.favouritesBadge.setVisible(number > 0);
                                MyApplication.favouritesBadge.setNumber(number);
                                break;
                            case 3:
                                tab.setText("Cart");
                                tab.setIcon(R.drawable.ic_home);
                                break;
                            case 4:
                                tab.setText("Profile");
                                tab.setIcon(R.drawable.ic_home);
                                break;
                        }
                    }
                }
        );
        mediator.attach();
    }
}







