package com.example.fastmart.activities;
import com.example.fastmart.R;
import com.example.fastmart.models.*;
import com.example.fastmart.db.*;

import com.example.fastmart.models.*;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private ListView chatListView;
    private EditText messageEditText;
    private ImageButton sendButton;
    private ArrayList<Message> messageList;
    private ChatAdapter adapter;

    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    private String currentUserId;
    private String otherUserId;
    private String chatRoomId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        otherUserId = getIntent().getStringExtra("otherUserId");
        initFirebase();
        
        if (otherUserId == null) {
            startActivity(new android.content.Intent(this, ChatListActivity.class));
            finish();
            return;
        }

        initUI();
        setupChatRoom();
        loadMessages();
    }

    private void initFirebase() {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        
        if (user != null) {
            currentUserId = user.getUid();
        } else {
            Toast.makeText(this, "Not logged in", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initUI() {
        chatListView = findViewById(R.id.chatListView);
        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);
        findViewById(R.id.backButton).setOnClickListener(v -> finish());

        messageList = new ArrayList<>();
        adapter = new ChatAdapter();
        chatListView.setAdapter(adapter);

        sendButton.setOnClickListener(v -> sendMessage());
    }

    private void setupChatRoom() {
        if (currentUserId.compareTo(otherUserId) < 0) {
            chatRoomId = currentUserId + "_" + otherUserId;
        } else {
            chatRoomId = otherUserId + "_" + currentUserId;
        }
        reference = database.getReference("chats").child(chatRoomId).child("messages");
    }

    private void loadMessages() {
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, String previousChildName) {
                Message msg = snapshot.getValue(Message.class);
                if (msg != null) {
                    messageList.add(msg);
                    adapter.notifyDataSetChanged();
                    chatListView.setSelection(messageList.size() - 1);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, String previousChildName) {}

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, String previousChildName) {}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void sendMessage() {
        String text = messageEditText.getText().toString().trim();
        if (!text.isEmpty()) {
            DatabaseReference newMessageRef = reference.push();
            
            Map<String, Object> msgMap = new HashMap<>();
            msgMap.put("senderId", currentUserId);
            msgMap.put("receiverId", otherUserId);
            msgMap.put("message", text);
            msgMap.put("timestamp", ServerValue.TIMESTAMP);

            newMessageRef.setValue(msgMap).addOnSuccessListener(aVoid -> {
                FirebaseDatabase.getInstance().getReference("user_chats")
                        .child(currentUserId).child(otherUserId).setValue(true);
                FirebaseDatabase.getInstance().getReference("user_chats")
                        .child(otherUserId).child(currentUserId).setValue(true);
            });
            messageEditText.setText("");
        }
    }

    private class ChatAdapter extends BaseAdapter {
        @Override
        public int getCount() { return messageList.size(); }
        @Override
        public Object getItem(int position) { return messageList.get(position); }
        @Override
        public long getItemId(int position) { return position; }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Message msg = messageList.get(position);

            LinearLayout layout = new LinearLayout(ChatActivity.this);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setPadding(20, 10, 20, 10);

            TextView textView = new TextView(ChatActivity.this);
            textView.setText(msg.getMessage());
            textView.setPadding(35, 25, 35, 25);
            textView.setTextColor(0xFFFFFFFF);
            textView.setTextSize(16);

            TextView timeView = new TextView(ChatActivity.this);
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
            String time = "00:00";
            if (msg.getTimestamp() > 0) {
                time = sdf.format(new Date(msg.getTimestamp()));
            }
            timeView.setText(time);
            timeView.setTextSize(10);
            timeView.setTextColor(0xFF888888);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            
            if (msg.getSenderId() != null && msg.getSenderId().equals(currentUserId)) {
                layout.setGravity(Gravity.END);
                textView.setBackgroundResource(R.drawable.bg_message_sent);
                params.gravity = Gravity.END;
                params.setMargins(100, 0, 0, 0);
            } else {
                layout.setGravity(Gravity.START);
                textView.setBackgroundResource(R.drawable.bg_message_received);
                params.gravity = Gravity.START;
                params.setMargins(0, 0, 100, 0);
                textView.setTextColor(0xFF000000); // Dark text for received bubble
            }

            layout.addView(textView, params);
            layout.addView(timeView, params);

            return layout;
        }
    }
}







