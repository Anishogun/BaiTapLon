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
import com.example.chatappwithfirebase.MainActivity;
import com.example.chatappwithfirebase.MessageActivity;
import com.example.chatappwithfirebase.Model.Chat;
import com.example.chatappwithfirebase.Model.User;
import com.example.chatappwithfirebase.R;
import com.example.chatappwithfirebase.StartActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;

public class UserApdapter extends RecyclerView.Adapter<UserApdapter.ViewHolder> {
    private Context mcontext;
    private List<User> mUsers;
    private  Boolean ischat ;
    String thelastmess;

    //Khởi tạo
    public UserApdapter(Context context,List<User> users, Boolean ischat)
    {
        this.mUsers = users;
        this.mcontext = context;
        this.ischat = ischat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.user_item, viewGroup,false);
        return new UserApdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
       //Gán tên và hình vào trong các view user
        final User user = mUsers.get(position);
        viewHolder.username.setText(user.getUsername());
        if(user.getImageURL().equals("default"))
        {
            viewHolder.profile_image.setImageResource(R.mipmap.ic_launcher);
        }
        else {
            Glide.with(mcontext).load(user.getImageURL()).into(viewHolder.profile_image);
        }

        if(ischat)
        {
            lastMessage(user.getId(),viewHolder.last_mess);
        }else
            viewHolder.last_mess.setVisibility(View.GONE);

        if(ischat){
            if(user.getStatus().equals("online"))
            {
                viewHolder.img_On.setVisibility(View.VISIBLE);
                viewHolder.img_Off.setVisibility(View.GONE);
            }
            else{
                viewHolder.img_On.setVisibility(View.GONE);
                viewHolder.img_Off.setVisibility(View.VISIBLE);
            }

        }else
            {
            viewHolder.img_On.setVisibility(View.GONE);
            viewHolder.img_Off.setVisibility(View.GONE);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, MessageActivity.class);
                intent.putExtra("userid",user.getId());
                mcontext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
         public TextView username;
         public ImageView profile_image;

         private ImageView img_On;
         private ImageView img_Off;
         private TextView last_mess;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            profile_image = itemView.findViewById(R.id.profile_image);
            img_On = itemView.findViewById(R.id.img_on);
            img_Off = itemView.findViewById(R.id.img_off);
            last_mess = itemView.findViewById(R.id.last_mess);
        }
    }
    // check for last mess
    private void lastMessage(final String userid, final TextView lass_mess)
    {
        thelastmess ="default";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("chat");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren())
                {
                    Chat chat = snapshot.getValue(Chat.class);
                    if(chat.getReceiver().equals(firebaseUser.getUid())&& chat.getSender().equals(userid)
                            || chat.getReceiver().equals(userid) && chat.getSender().equals(firebaseUser.getUid()))
                    {
                        thelastmess = chat.getMessage();
                    }

                }
                switch (thelastmess){
                    case "default":
                        lass_mess.setText("No Message");
                        break;
                    default:
                        lass_mess.setText(thelastmess);
                        break;
                }
                thelastmess = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
