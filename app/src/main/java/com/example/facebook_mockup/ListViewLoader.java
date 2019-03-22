package com.example.facebook_mockup;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ListViewLoader extends AsyncTask<Void, Integer, ListView> {

    private Activity activity;
    ArrayList<Users> friendsList;
    UserAdapter friendAdapter;
    ListView friendsListView;
    ProgressBar progressBar;

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void setFriendsList(ArrayList<Users> friendsList) {
        this.friendsList = friendsList;
    }

    public void setFriendAdapter(UserAdapter friendAdapter) {
        this.friendAdapter = friendAdapter;
    }

    public void setFriendsListView(ListView friendsListView) {
        this.friendsListView = friendsListView;
    }

    public ListViewLoader(Activity activity, ArrayList<Users> friendsList, UserAdapter friendAdapter, ListView friendsListView) {
        this.activity = activity;
        this.friendsList = friendsList;
        this.friendAdapter = friendAdapter;
        this.friendsListView = friendsListView;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar = (ProgressBar) activity.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected ListView doInBackground(Void... voids) {
        Data.users.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                       // store users to friend list but not ourselves
                        Users newUser = new Users(doc.getString("userName"), doc.getString("password"), doc.getString("firstName"), doc.getString("avatar"),doc.getBoolean("active"));
                        if (!Objects.equals(activity.getIntent().getStringExtra("currentUser"), newUser.getUserName())) {
                            friendsList.add(newUser);
                        } else {
                            Data.user = newUser;
                        }
                    }
                    friendAdapter = new UserAdapter(activity, R.layout.friends, friendsList);
                    friendsListView.setAdapter(friendAdapter);
                    ((TextView) activity.findViewById(R.id.firstNameTextView)).setText(Data.user.getFirstName());
                    ((ImageView) activity.findViewById(R.id.avatarImageView)).setImageBitmap(Actions.ConvertStringToImage(Data.user.getAvatar()));
                }
            }
        });
        return friendsListView;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

        int number = values[0];
        progressBar.setProgress(number);
    }

    @Override
    protected void onPostExecute(ListView listView) {
        super.onPostExecute(listView);
        SystemClock.sleep(2000);
        progressBar.setVisibility(View.GONE);
    }
}
