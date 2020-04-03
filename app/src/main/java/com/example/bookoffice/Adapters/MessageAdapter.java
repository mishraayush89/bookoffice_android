package com.example.bookoffice.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookoffice.Model.Chat;
import com.example.bookoffice.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT=0;
    public static final int MSG_TYPE_RIGHT=1;

    private Context mcontext;
    private List<Chat> mchat;
    private String imgurl;

    public MessageAdapter(Context mcontext,List<Chat> mchat,String imgurl){
        this.mchat=mchat;
        this.mcontext=mcontext;
        this.imgurl=imgurl;
    }




    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==MSG_TYPE_RIGHT){
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_right,parent,false);
            return new MessageAdapter.ViewHolder(view);

        }
        else{
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.chati_tem_left,parent,false);
            return new MessageAdapter.ViewHolder(view);
        }



    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {

        Chat chat=mchat.get(position);
        holder.show_message.setText(chat.getMessage());

        //Picasso.get().load(imgurl).into(holder.profile_image);




    }

    @Override
    public int getItemCount() {
        return mchat.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView show_message;
        public ImageView profile_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            show_message=itemView.findViewById(R.id.show_message);
            profile_image=itemView.findViewById(R.id.chat_item_profile_image);
        }






    }

    @Override
    public int getItemViewType(int position){
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if(mchat.get(position).getSender().equals(user.getUid())){
            return MSG_TYPE_RIGHT;
        }

        else{
            return MSG_TYPE_LEFT;
        }
    }
}
