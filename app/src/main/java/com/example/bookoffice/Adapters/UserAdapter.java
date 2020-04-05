package com.example.bookoffice.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bookoffice.MessagingActivity;
import com.example.bookoffice.Model.Chat;
import com.example.bookoffice.Model.Users;
import com.example.bookoffice.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {


    private Context mcontext;
    private List<Users> musers;
    private boolean ischat;

    String TheLastMessage;


    public  UserAdapter(Context mcontext,List<Users> musers,boolean ischat){
        this.mcontext=mcontext;
        this.musers=musers;
        this.ischat=ischat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.user_item,parent,false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Users user=musers.get(position);
        holder.username.setText(user.getName());
        Glide.with(mcontext).load(user.getImageurl()).into(holder.profile_image);


        if(ischat){
            LastMessage(user.getUid(),holder.last_msg);
        }else{
            holder.last_msg.setVisibility(View.GONE);
        }

        if(ischat){
            if(user.getStatus().equals("online")){
                holder.img_on.setVisibility(View.VISIBLE);
                holder.img_off.setVisibility(View.GONE);
            }

            else{
                holder.img_on.setVisibility(View.GONE);
                holder.img_off.setVisibility(View.VISIBLE);
            }
        }
        else{

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(mcontext, MessagingActivity.class);
                i.putExtra("sellerid",user.getUid());
                mcontext.startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {

        return musers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView username,last_msg;
        public CircleImageView profile_image,img_on,img_off;

        public ViewHolder(View itemView){
            super(itemView);
            img_off=itemView.findViewById(R.id.img_off);
            img_on=itemView.findViewById(R.id.img_on);
            profile_image=itemView.findViewById(R.id.user_item_profile_pic);
            username=itemView.findViewById(R.id.user_item_username);
            last_msg=itemView.findViewById(R.id.last_message);


        }




    }

    private void LastMessage(final String userid, final TextView last_msg){
        TheLastMessage="default";
        final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Chat chat=snapshot.getValue(Chat.class);
                    if(chat.getReciever().equals(firebaseUser.getUid()) && chat.getSender().equals(userid)
                            ||chat.getReciever().equals(userid) && chat.getSender().equals(firebaseUser.getUid())
                    ){
                        TheLastMessage=chat.getMessage();

                    }
                }

                switch (TheLastMessage){

                    case "default":last_msg.setText("No Message");
                    break;
                    default:last_msg.setText(TheLastMessage);
                    break;


                }
                TheLastMessage="default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }




}
