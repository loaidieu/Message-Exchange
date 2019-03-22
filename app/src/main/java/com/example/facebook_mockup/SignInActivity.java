package com.example.facebook_mockup;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SignInActivity extends AppCompatActivity {

    //all needed views
    EditText usernamrEditText, passwordEditText, passwordConfirmEditText;
    boolean duplicateUsername = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //get all the views
        usernamrEditText = findViewById(R.id.usernameEditText);
        passwordConfirmEditText = findViewById(R.id.confirmEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        Initialize();
    }

    //initialize
    private void Initialize() {
        getSupportActionBar().hide();
        usernamrEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (TextUtils.isEmpty(((EditText) v).getText().toString())) {
                        ((EditText) v).setError(Error.fieldIsRequired);
                    }
                }
            }

        });
        usernamrEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                for(DocumentSnapshot doc : Data.documentSnapshots)
                {
                    if (Objects.equals(doc.getString("userName"), usernamrEditText.getText().toString()))
                    {
                        duplicateUsername = true;
                        usernamrEditText.setError("The username does already exist.");
                        return;
                    }
                    else
                    {
                        duplicateUsername = false;
                    }
                }

   //             Toast.makeText(SignInActivity.this, collections.toString(),Toast.LENGTH_SHORT).show();
            }
        });
        Data.users.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                {
                    Data.documentSnapshots = task.getResult().getDocuments();
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
        passwordConfirmEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (TextUtils.isEmpty(((EditText) v).getText().toString())) {
                        ((EditText) v).setError(Error.fieldIsRequired);
                    }
                }
            }

        });
        passwordConfirmEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!Objects.equals(passwordConfirmEditText.getText().toString(), passwordEditText.getText().toString()))
                {
                    passwordConfirmEditText.setError("Confirming password does NOT match the password");
                }
            }
        });

    }

    public void SignIn(View v)
    {
        if (duplicateUsername != true && (Objects.equals(passwordConfirmEditText.getText().toString(),passwordEditText.getText().toString()))) {
            Map<String, Object> newUser = new HashMap<>();
            newUser.put("password", passwordConfirmEditText.getText().toString());
            newUser.put("avatar", Actions.ConvertImageToStringAndReturn(BitmapFactory.decodeResource(getResources(),R.drawable.welcome)));
            newUser.put("firstName", "Put your name");
            newUser.put("userName", usernamrEditText.getText().toString());
            newUser.put("active",false);
            Data.users.document(usernamrEditText.getText().toString()).set(newUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(SignInActivity.this, "Register user successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
    }
}
