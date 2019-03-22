package com.example.facebook_mockup;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<chats> chatList;

    public ChatAdapter(Context context, int layout, ArrayList<chats> chatList) {
        this.context = context;
        this.layout = layout;
        this.chatList = chatList;
    }

    @Override
    public int getCount() {
        return chatList.size();
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
        holder.usernameTextView = (TextView) convertView.findViewById(R.id.usernameTextViewChatListView);
        holder.content = (TextView) convertView.findViewById(R.id.contentTextViewChatListView);
        holder.avatar = (ImageView) convertView.findViewById(R.id.avatarImageViewListViewChatListView);
        holder.avatar2 =(ImageView) convertView.findViewById(R.id.avatarImageViewListViewChatListView2);
        chats chat = chatList.get(position);
        holder.usernameTextView.setText(chat.getSender());
        holder.content.setText(chat.getContent());
        if (chat.getSender().equals(Data.user.getUserName()))
        {
            holder.avatar.setImageBitmap(Actions.ConvertStringToImage(Data.user.getAvatar()));
            holder.content.setBackgroundColor(Color.rgb(146,188 ,222));
            holder.avatar2.setVisibility(View.INVISIBLE);
        }
        else
        {
            holder.avatar2.setImageBitmap(Actions.ConvertStringToImage(Data.talkToUser.getAvatar()));
            holder.avatar.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    public class Holder {
        TextView usernameTextView;
        TextView content;
        ImageView avatar;
        ImageView avatar2;
    }
}
