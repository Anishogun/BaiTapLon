package com.example.chatappwithfirebase.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chatappwithfirebase.MessageActivity;
import com.example.chatappwithfirebase.Model.Chat;
import com.example.chatappwithfirebase.Model.User;
import com.example.chatappwithfirebase.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    private Context mcontext;
    private List<Chat> mChats;
    private String imageURL;
    FirebaseUser fuser;

    //Khởi tạo
    public MessageAdapter(Context context, List<Chat> chats,String imageURL)
    {
        this.mChats = chats;
        this.mcontext = context;
        this.imageURL = imageURL;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(mcontext).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
        else
        {
            View view = LayoutInflater.from(mcontext).inflate(R.layout.chat_item_left,parent,false);
            return  new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder viewHolder, int position) {

        Chat chat = mChats.get(position);
        viewHolder.show_message.setText(chat.getMessage());
        if(imageURL.equals("default"))
        {
            viewHolder.profile_image.setImageResource(R.mipmap.ic_launcher);
        }
        else
            Glide.with(mcontext).load(imageURL).into(viewHolder.profile_image);

        //check for last message
        if(position == mChats.size()-1){
            if(chat.isIsseen() == true)
            {
                viewHolder.txt_seen.setText("Seen");
            }
            else {viewHolder.txt_seen.setText("Delivered");}
        }else
            viewHolder.txt_seen.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return mChats.size();
    }


    //Set item cho VỉewHolder
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView show_message;
        public ImageView profile_image;
        public TextView txt_seen;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            show_message = itemView.findViewById(R.id.show_message);
            profile_image = itemView.findViewById(R.id.profile_image);
            txt_seen = itemView.findViewById(R.id.txt_seen);
        }

    }

    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if(mChats.get(position).getSender().equals(fuser.getUid()))
        {
            return MSG_TYPE_RIGHT;
        }
        else
            return  MSG_TYPE_LEFT;
    }
}

