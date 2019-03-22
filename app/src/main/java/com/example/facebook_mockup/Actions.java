package com.example.facebook_mockup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;


public class Actions extends Activity {
    public static void ConvertImageToString(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, bos);
        byte[] byteArray = bos.toByteArray();
        String str = Base64.encodeToString(byteArray, Base64.DEFAULT);
        Data.user.setAvatar(str);
        Data.users.document(Data.user.getUserName()).set(Data.user);
    }

    public static String ConvertImageToStringAndReturn(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 5, bos);
        byte[] byteArray = bos.toByteArray();
        String str = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return str;
    }

    public static Bitmap ConvertStringToImage(String str) {
        InputStream stream = new ByteArrayInputStream(Base64.decode(str.getBytes(), Base64.DEFAULT));
        Bitmap bit = BitmapFactory.decodeStream(stream);
        return bit;
    }

    public static void Clear(Object[] objects) {
        for (Object obj : objects) {
            if (obj instanceof TextView) {
                ((TextView) obj).setText("");
            }
        }
    }

    //HIDE KEYBOARD
    public static void HideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    //Disable LoginButton
    public static void DisableButton(Button view) {
        view.setEnabled(false);
        view.setText("Login...");
    }

    //enable LoginButton
    public static void EnableButton(Button view) {
        view.setEnabled(true);
        view.setText("Login");
    }

    //Disable LoginButton
    public static void DisableListView(ListView view) {
        view.setEnabled(false);
    }

    //enable LoginButton
    public static void EnableListView(ListView view) {
        view.setEnabled(true);
    }
}
