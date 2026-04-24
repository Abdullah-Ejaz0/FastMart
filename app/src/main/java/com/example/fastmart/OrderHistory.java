package com.example.fastmart;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderHistory extends AppCompatActivity {

    RecyclerView rvOrderHistory;
    OrderHistoryAdapter adapter;
    ArrayList<Order> orderList;
    ImageView btnBack;
    DatabaseReference dbRef;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_history);
        
        // Handle window insets for EdgeToEdge
        View mainView = findViewById(R.id.rvOrderHistory); // Use the RecyclerView or a parent layout ID
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        init();
        fetchOrderHistory();
    }

    private void init() {
        rvOrderHistory = findViewById(R.id.rvOrderHistory);
        btnBack = findViewById(R.id.btnBack);
        
        orderList = new ArrayList<>();
        adapter = new OrderHistoryAdapter(this, orderList);
        
        rvOrderHistory.setLayoutManager(new LinearLayoutManager(this));
        rvOrderHistory.setAdapter(adapter);

        btnBack.setOnClickListener(v -> finish());

        auth = FirebaseAuth.getInstance();
        
        // Based on the requirement: "Only the seller can view order history" 
        // and "stored... as part of the user's order history".
        // If it's a seller viewing their received orders, or a buyer viewing their own:
        if (auth.getCurrentUser() != null) {
            String uid = auth.getCurrentUser().getUid();
            // Checking if we are fetching global orders (for seller) or personal (for buyer)
            // The prompt says "Only the seller can view order history", suggesting a global branch or seller-specific.
            // But also says "as part of the user's order history".
            // We'll fetch from Users/{uid}/orderHistory for now as per "user's order history".
            dbRef = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("orderHistory");
        }
    }

    private void fetchOrderHistory() {
        if (dbRef == null) return;

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Order order = dataSnapshot.getValue(Order.class);
                    if (order != null) {
                        orderList.add(0, order); // Add at top (latest first)
                    }
                }
                adapter.notifyDataSetChanged();
                
                if (orderList.isEmpty()) {
                    Toast.makeText(OrderHistory.this, "No orders found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(OrderHistory.this, "Failed to load orders: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
