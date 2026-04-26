package com.example.fastmart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatListActivity extends AppCompatActivity {

    private ListView conversationListView;
    private TextView emptyTextView;
    private ArrayList<User> chatUsers;
    private ChatListAdapter adapter;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        currentUserId = FirebaseAuth.getInstance().getUid();
        if (currentUserId == null) {
            finish();
            return;
        }

        initUI();
        loadConversations();
    }

    private void initUI() {
        conversationListView = findViewById(R.id.conversationListView);
        emptyTextView = findViewById(R.id.emptyTextView);
        findViewById(R.id.backButton).setOnClickListener(v -> finish());

        chatUsers = new ArrayList<>();
        adapter = new ChatListAdapter();
        conversationListView.setAdapter(adapter);

        conversationListView.setOnItemClickListener((parent, view, position, id) -> {
            User selectedUser = chatUsers.get(position);
            Intent intent = new Intent(ChatListActivity.this, ChatActivity.class);
            intent.putExtra("otherUserId", selectedUser.getCode());
            startActivity(intent);
        });
    }

    private void loadConversations() {
        if (MyApplication.user == null) {
            FirebaseDatabase.getInstance().getReference("Users").child(currentUserId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            MyApplication.user = snapshot.getValue(User.class);
                            if (MyApplication.user != null) {
                                fetchUsersByRole();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {}
                    });
        } else {
            fetchUsersByRole();
        }
    }

    private void fetchUsersByRole() {
        String targetRole = "Seller".equalsIgnoreCase(MyApplication.user.getAccountType()) ? "Buyer" : "Seller";
        
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatUsers.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    User u = ds.getValue(User.class);
                    if (u != null && targetRole.equalsIgnoreCase(u.getAccountType())) {
                        u.setCode(ds.getKey()); 
                        chatUsers.add(u);
                    }
                }
                
                if (chatUsers.isEmpty()) {
                    emptyTextView.setText("No " + targetRole + "s found");
                    emptyTextView.setVisibility(View.VISIBLE);
                } else {
                    emptyTextView.setVisibility(View.GONE);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ChatListActivity.this, "Failed to load users", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class ChatListAdapter extends BaseAdapter {
        @Override
        public int getCount() { return chatUsers.size(); }
        @Override
        public Object getItem(int position) { return chatUsers.get(position); }
        @Override
        public long getItemId(int position) { return position; }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.item_chat, parent, false);
            }
            User u = chatUsers.get(position);
            TextView name = convertView.findViewById(R.id.userName);
            TextView email = convertView.findViewById(R.id.userEmail);
            
            name.setText(u.getName());
            email.setText(u.getEmail());
            
            return convertView;
        }
    }
}
