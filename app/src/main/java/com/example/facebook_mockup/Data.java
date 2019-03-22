package com.example.facebook_mockup;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;



import java.util.ArrayList;
import java.util.List;

public class Data {
    public static FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static CollectionReference users = db.collection("users");
    public static DocumentReference documentReference;
    public static List<DocumentSnapshot> documentSnapshots;
    public static Users user = null;
    public static Users talkToUser = null;
    public static String chatName = "";
    public static int lastIndexer = 0;
    public static DatabaseReference realtime = FirebaseDatabase.getInstance().getReference();
    public static CollectionReference chats = db.collection("chats");
    public static StorageReference storage = FirebaseStorage.getInstance().getReference();
}
