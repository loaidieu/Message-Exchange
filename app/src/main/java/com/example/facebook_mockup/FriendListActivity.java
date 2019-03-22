package com.example.facebook_mockup;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;


public class FriendListActivity extends AppCompatActivity {

    ListView friendsListView;
    public static ArrayList<Users> friendsList;
    public static UserAdapter friendAdapter;
    String chatIndexer = "";
    TextView firstNameTextView;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Data.realtime.child("active").setValue(Data.user.getUserName());
        Data.users.document(Data.user.getUserName()).update("active",false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
        friendsList = new ArrayList<>();
        firstNameTextView = findViewById(R.id.firstNameTextView);

        GetChatIndexer();
    }


    public void Initialize() {
        getSupportActionBar().hide();
        friendsListView = findViewById(R.id.friendListView);
        ListViewLoader listViewLoader = new ListViewLoader(this, friendsList, friendAdapter, friendsListView);
        listViewLoader.execute();

        friendsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            String chatName = "";

            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Actions.DisableListView(friendsListView);
                Data.users.document(friendsList.get(position).getUserName()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot doc = task.getResult();
                            chatName = doc.getString(Data.user.getUserName());
                            if (chatName == null) {
                                String chatID = "chat" + chatIndexer;
                                Map<String, Object> data = new HashMap<>();
                                data.put("dummy data", "dummy data");
                                Data.db.collection(chatID).document("indexer").set(data);
                                //create who will be in the conversation with you
                                data.clear();
                                data.put(friendsList.get(position).getUserName(), chatID);
                                Data.users.document(Data.user.getUserName()).set(data, SetOptions.merge());
                                data.clear();
                                data.put(Data.user.getUserName(), chatID);
                                Data.users.document(friendsList.get(position).getUserName()).set(data, SetOptions.merge());
                                Data.db.collection("chats").document("chatIDs").update("indexer", String.valueOf(Integer.valueOf(chatIndexer) + 1));
                                //Show messengers
                                Data.talkToUser = friendsList.get(position);
                                Data.chatName = chatID;
                                Intent intent = new Intent(FriendListActivity.this, ChatActivity.class);
                                Actions.EnableListView(friendsListView);
                                startActivity(intent);
                            } else {
                                //show messenger
                                Data.talkToUser = friendsList.get(position);
                                Data.chatName = chatName;
                                Intent intent = new Intent(FriendListActivity.this, ChatActivity.class);
                                Actions.EnableListView(friendsListView);
                                startActivity(intent);
                            }
                        }
                    }
                });
            }
        });

        firstNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get prompts.xml view
                LayoutInflater li = LayoutInflater.from(FriendListActivity.this);
                View promptsView = li.inflate(R.layout.promp, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        FriendListActivity.this);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Set",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        if (!userInput.getText().toString().equals("")) {
                                            Data.users.document(Data.user.getUserName()).update("firstName", userInput.getText().toString());
                                            firstNameTextView.setText(userInput.getText().toString());
                                        } else {
                                            userInput.setError("This field should be filled out or click CANCEL");
                                        }
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

            }
        });


        findViewById(R.id.avatarImageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                final int request = 71;
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), request);
            }
        });
//   Update others user change
//        Timer t = new Timer();
//        //Set the schedule function and rate
//        t.scheduleAtFixedRate(new TimerTask() {
//
//            @Override
//            public void run() {
//                Data.users.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (DocumentSnapshot doc : task.getResult()) {
//                                Users fetchingUser = new Users(doc.getString("userName"), doc.getString("password"), doc.getString("firstName"), doc.getString("avatar"));
//                                if (friendsList.contains(fetchingUser)) {
//                                    for (int i = 0; i < friendsList.size(); i++) {
//                                        if (friendsList.get(i).getUserName().equals(fetchingUser.getUserName()) && !friendsList.get(i).getAvatar().equals(fetchingUser.getAvatar())) {
//                                            friendsList.get(i).setAvatar(fetchingUser.getAvatar());
//                                        }
//                                    }
//                                    for (int i = 0; i < friendsList.size(); i++) {
//                                        if (friendsList.get(i).getUserName().equals(fetchingUser.getUserName()) && !friendsList.get(i).getFirstName().equals(fetchingUser.getFirstName())) {
//                                            friendsList.get(i).setFirstName(fetchingUser.getFirstName());
//                                        }
//                                    }
//                                } else {
//                                    if (!fetchingUser.getUserName().equals(Data.user.getUserName())) {
//                                        friendsList.add(fetchingUser);
//                                    }
//                                }
//                            }
//                            friendAdapter.notifyDataSetChanged();
//                        }
//                    }
//                });
//            }
//        }, 2000, 2000);
    }

    //Change the user image when the user selected
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 71 && resultCode == RESULT_OK && data.getData() != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                Actions.ConvertImageToString(bitmap);
                ((ImageView) findViewById(R.id.avatarImageView)).setImageBitmap(Actions.ConvertStringToImage(Data.user.getAvatar()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //Get chat indexer
    private void GetChatIndexer() {
        Data.db.collection("chats").document("chatIDs").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    chatIndexer = doc.getString("indexer");
                    Initialize();
                }
            }
        });
    }

}
