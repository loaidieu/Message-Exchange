package com.example.facebook_mockup;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class ChatActivity extends AppCompatActivity {

    ListView chatListView;
    ArrayList<chats> chatsArrayList;
    ChatAdapter chatAdapter;
    EditText messageBox;
    String chatName = "";
    int indexer = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatListView = findViewById(R.id.converstionListView);
        chatsArrayList = new ArrayList<chats>();
        messageBox = findViewById(R.id.messageEditText);

        Initialize();
    }

    //initiate
    private void Initialize() {
        getSupportActionBar().hide();
        Data.db.collection(Data.chatName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        if (doc.getString("sender") != null) {
                            chatsArrayList.add(new chats(doc.getString("sender"), doc.getString("content"), doc.getLong("time")));
                        }
                    }
                    Collections.sort(chatsArrayList);
                    chatAdapter = new ChatAdapter(ChatActivity.this, R.layout.chats, chatsArrayList);
                    chatListView.setAdapter(chatAdapter);
                    Data.realtime.child(Data.chatName).child(Data.user.getUserName()).setValue("");
                    Data.realtime.child(Data.chatName).child(Data.talkToUser.getUserName()).setValue("");
                    Data.realtime.child(Data.chatName).child(Data.talkToUser.getUserName()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.getValue().equals("")) {
                                chatsArrayList.add(new chats(Data.talkToUser.getUserName(), dataSnapshot.getValue(String.class)));
                                if (chatListView.getLastVisiblePosition() == chatListView.getCount() - 1) {
                                    chatAdapter.notifyDataSetChanged();
                                    chatListView.smoothScrollToPosition(chatListView.getCount() - 1);
                                } else {
                                    chatAdapter.notifyDataSetChanged();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(ChatActivity.this, "Sending is canceled", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(ChatActivity.this, "not success", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Hide KEYBOARD
    //HIDE SOFT KEYBOARD
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();

        if (v != null &&
                (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
                v instanceof EditText &&
                !v.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            v.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
            float y = ev.getRawY() + v.getTop() - scrcoords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
                Actions.HideKeyboard(this);
        }
        return super.dispatchTouchEvent(ev);
    }

    //send message
    public void SendMessage(View view) {
        if (!messageBox.getText().toString().equals("")) {
            Data.db.collection(Data.chatName).document("indexer").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot doc = task.getResult();
                        Map<String, Object> data = new HashMap<>();
                        data.put("content", messageBox.getText().toString());
                        data.put("sender", Data.user.getUserName());
                        data.put("time", new java.util.Date().getTime());
                        Data.db.collection(Data.chatName).add(data);
                        chatsArrayList.add(new chats(Data.user.getUserName(), messageBox.getText().toString()));
                        chatAdapter.notifyDataSetChanged();
                        Data.realtime.child(Data.chatName).child(Data.user.getUserName()).setValue(messageBox.getText().toString());
                        messageBox.setText("");
                        chatListView.smoothScrollToPosition(chatListView.getCount() - 1);
                    }
                }
            });
        }
    }
}
