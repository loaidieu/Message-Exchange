package com.example.facebook_mockup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;

public class UserAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<Users> usersList;


    public UserAdapter(Context context, int layout, ArrayList<Users> usersList) {
        this.context = context;
        this.layout = layout;
        this.usersList = usersList;

    }

    @Override
    public int getCount() {
        return usersList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(layout, null);
        holder.usernameTextView = (TextView) convertView.findViewById(R.id.usernameTextViewListView);
        holder.nameTextView = (TextView) convertView.findViewById(R.id.nameTextViewListView);
        holder.avatar = (ImageView) convertView.findViewById(R.id.avatarImageViewListView);
        holder.active =  (ImageView) convertView.findViewById(R.id.imageViewActive);
        holder.deactive =  (ImageView) convertView.findViewById(R.id.imageViewDeactive);
        Users users = usersList.get(position);
        convertView.setTag(holder);
        holder.usernameTextView.setText(users.getUserName());
        holder.nameTextView.setText(users.getFirstName());
        holder.avatar.setImageBitmap(Actions.ConvertStringToImage(users.getAvatar()));
        if (users.isActive())
        {
            holder.active.setVisibility(View.VISIBLE);
            holder.deactive.setVisibility(View.INVISIBLE);
        }
        else
        {
            holder.active.setVisibility(View.INVISIBLE);
            holder.deactive.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    public class Holder {
        TextView usernameTextView;
        TextView nameTextView;
        ImageView avatar;
        ImageView active;
        ImageView deactive;
    }

}
