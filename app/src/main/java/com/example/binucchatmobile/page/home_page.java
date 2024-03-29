package com.example.binucchatmobile.page;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.binucchatmobile.R;
import com.example.binucchatmobile.adapter.RecentChatRecyclerAdapter;
import com.example.binucchatmobile.model.ChatroomModel;
import com.example.binucchatmobile.model.UserModel;
import com.example.binucchatmobile.utils.AndroidUtil;
import com.example.binucchatmobile.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

public class home_page extends AppCompatActivity {
    RecyclerView recentChatRecyclerView;
    RecentChatRecyclerAdapter chatAdapter;
    ImageView NavProfile, NavSearchContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        NavProfile = findViewById(R.id.nav_profile_search);
        NavSearchContact =  findViewById(R.id.nav_search_search);
        recentChatRecyclerView = findViewById(R.id.recentchat_recyclerview);

        FirebaseUtil.getCurrProfPicStorage().getDownloadUrl()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Uri uri = task.getResult();
                        AndroidUtil.setProfPic(this, uri, NavProfile);
                    }
                });

        setupRecyclerView();

        NavProfile.setOnClickListener(v -> {
            Intent intentProfile = new Intent(home_page.this, setting_page.class);
            startActivity(intentProfile);
        });

        NavSearchContact.setOnClickListener(v -> {
            Intent intentSearch = new Intent(home_page.this, user_search_page.class);
            startActivity(intentSearch);
        });

    }
    void setupRecyclerView(){
        Query query = FirebaseUtil.allChatroomCollectionReference()
                .whereArrayContains("userIds", FirebaseUtil.currentUserId())
                .orderBy("lastMessageTimestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ChatroomModel> options = new FirestoreRecyclerOptions.Builder<ChatroomModel>()
                .setQuery(query,ChatroomModel.class).build();

        chatAdapter = new RecentChatRecyclerAdapter(options,getApplicationContext());
        recentChatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recentChatRecyclerView.setAdapter(chatAdapter);
        chatAdapter.startListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        if(chatAdapter != null)
            chatAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(chatAdapter != null)
            chatAdapter.stopListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(chatAdapter != null)
            chatAdapter.notifyDataSetChanged();
    }
}