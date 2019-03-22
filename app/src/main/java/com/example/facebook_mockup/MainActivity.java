package com.example.facebook_mockup;

import android.content.Intent;
import android.drm.DrmStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    //Defining all views
    EditText usernameEditText, passwordEditText;
    TextView signInTextView;
    Button login;
    Switch showPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get all Views
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        signInTextView = findViewById(R.id.sigInTextView);
        login = findViewById(R.id.loginButton);
        showPassword = findViewById(R.id.switchShowPassword);

        Initialize();
    }

    //Initial views events
    private void Initialize() {
        getSupportActionBar().hide();
        //Set REQUIRED ATTRIBUTES to USERNAME edittext and PASSWORD edittext
        usernameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (TextUtils.isEmpty(((EditText) v).getText().toString())) {
                        ((EditText) v).setError(Error.fieldIsRequired);
                    }
                }
            }
        });
        passwordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (TextUtils.isEmpty(((EditText) v).getText().toString())) {
                        ((EditText) v).setError(Error.fieldIsRequired);
                    }
                }
            }
        });

        //set up the linked text for SIGNING AN ACCOUNT
        signInTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                startActivity(intent);
                return false;
            }
        });

        //Set up SHOW PASSWORD
        showPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        //set up listener for active
        Data.realtime.child("active").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String active = "";
                active = dataSnapshot.getValue().toString();
                try {
                    if (FriendListActivity.friendsList != null) {
                        for (Users user : FriendListActivity.friendsList) {
                            if (user.getUserName().equals(active)) {
                                user.setActive(true);
                                FriendListActivity.friendAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //Login();
    public void Login(View v) {
        if (!TextUtils.isEmpty(usernameEditText.getText().toString()) && !TextUtils.isEmpty(passwordEditText.getText().toString())) {
            Actions.DisableButton(login);
            Data.documentReference = Data.users.document(usernameEditText.getText().toString());
            Data.documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        if (Objects.equals(passwordEditText.getText().toString(), task.getResult().getString("password"))) {
                            Toast.makeText(MainActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, FriendListActivity.class);
                            intent.putExtra("currentUser", usernameEditText.getText().toString());
                            Data.realtime.child("active").setValue(usernameEditText.getText().toString());
                            Data.users.document(usernameEditText.getText().toString()).update("active", true);
                            passwordEditText.setText("");
                            usernameEditText.setText("");
                            Actions.EnableButton(login);
                            startActivity(intent);
                        } else {
                            passwordEditText.setText("");
                            Toast.makeText(MainActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }

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
}
