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
import com.example.bookoffice.Model.Users;
import com.example.bookoffice.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {


    private Context mcontext;
    private List<Users> musers;


    public  UserAdapter(Context mcontext,List<Users> musers){
        this.mcontext=mcontext;
        this.musers=musers;
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

        public TextView username;
        public CircleImageView profile_image;

        public ViewHolder(View itemView){
            super(itemView);

            profile_image=itemView.findViewById(R.id.user_item_profile_pic);
            username=itemView.findViewById(R.id.user_item_username);


        }




    }




}
