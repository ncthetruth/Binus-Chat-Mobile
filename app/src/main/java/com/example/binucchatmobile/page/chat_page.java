package com.example.binucchatmobile.page;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.binucchatmobile.R;
import com.example.binucchatmobile.adapter.ChatRecyclerAdapter;
import com.example.binucchatmobile.model.ChatMessageModel;
import com.example.binucchatmobile.model.ChatroomModel;
import com.example.binucchatmobile.model.UserModel;
import com.example.binucchatmobile.utils.AndroidUtil;
import com.example.binucchatmobile.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;


import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class chat_page extends AppCompatActivity {
    UserModel currentUser;
    TextView other_username;
    String currentChatroomId;
    ChatroomModel currentChatroomModel;
    ChatRecyclerAdapter chatRecyclerAdapter;
    EditText chat_message_input;
    ImageView message_send_btn, back_btn, profPicIV;
    RecyclerView chat_recycler_view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_page);

        //get UserModel
        currentUser = AndroidUtil.getUserModelFromIntent(getIntent());
        currentChatroomId = FirebaseUtil.getChatroomId(FirebaseUtil.currentUserId(),currentUser.getUserId());

        chat_message_input = findViewById(R.id.chat_message_input);
        message_send_btn = findViewById(R.id.message_send_btn);
        back_btn = findViewById(R.id.back_btn);
        other_username = findViewById(R.id.other_username);
        chat_recycler_view = findViewById(R.id.chat_recycler_view);
        profPicIV = findViewById(R.id.profPicIV);

        back_btn.setOnClickListener((v)->{
            Intent intent = new Intent(chat_page.this, home_page.class);
            startActivity(intent);
        });
        other_username.setText(currentUser.getUsername());

        message_send_btn.setOnClickListener((v -> {
            String message = chat_message_input.getText().toString().trim();
            if(message.isEmpty())
                return;
            sendMessage(message);
        }));

        getOrCreateChatroomModel();
        setupChatRecyclerView();

        FirebaseUtil.getOtherProfPicStorage(currentUser.getUserId()).getDownloadUrl()
                .addOnCompleteListener(t -> {
                    if(t.isSuccessful()){
                        Uri uri = t.getResult();
                        AndroidUtil.setProfPic(this, uri, profPicIV);
                    }
                });
    }

    void setupChatRecyclerView(){
        Query query = FirebaseUtil.getChatroomMessageReference(currentChatroomId)
                .orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ChatMessageModel> options = new FirestoreRecyclerOptions.Builder<ChatMessageModel>()
                .setQuery(query,ChatMessageModel.class).build();

        chatRecyclerAdapter = new ChatRecyclerAdapter(options,getApplicationContext());
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        chat_recycler_view.setLayoutManager(manager);
        chat_recycler_view.setAdapter(chatRecyclerAdapter);
        chatRecyclerAdapter.startListening();
        chatRecyclerAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                chat_recycler_view.smoothScrollToPosition(0);
            }
        });
    }

    void sendMessage(String message){
        currentChatroomModel.setLastMessageTimestamp(Timestamp.now());
        currentChatroomModel.setLastMessageSenderId(FirebaseUtil.currentUserId());
        currentChatroomModel.setLastMessage(message);
        FirebaseUtil.getChatroomReference(currentChatroomId).set(currentChatroomModel);

        ChatMessageModel chatMessageModel = new ChatMessageModel(message,FirebaseUtil.currentUserId(),Timestamp.now());
        FirebaseUtil.getChatroomMessageReference(currentChatroomId).add(chatMessageModel)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isSuccessful()){
                            chat_message_input.setText("");
                        }
                    }
                });
    }

    void getOrCreateChatroomModel(){
        FirebaseUtil.getChatroomReference(currentChatroomId).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                currentChatroomModel = task.getResult().toObject(ChatroomModel.class);



                if(currentChatroomModel==null){
                    //first time chat
                    currentChatroomModel = new ChatroomModel(
                            currentChatroomId,
                            Arrays.asList(FirebaseUtil.currentUserId(),currentUser.getUserId()),
                            Timestamp.now(),
                            ""
                    );
                    FirebaseUtil.getChatroomReference(currentChatroomId).set(currentChatroomModel);
                }
            }
        });
    }
}