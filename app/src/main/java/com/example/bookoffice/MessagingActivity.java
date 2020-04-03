package com.example.bookoffice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookoffice.Adapters.MessageAdapter;
import com.example.bookoffice.Model.Chat;
import com.example.bookoffice.Model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagingActivity extends AppCompatActivity {

    CircleImageView profile_image;
    TextView username;
    ImageButton send;
    EditText chatmessage;


    FirebaseUser fuser;
    DatabaseReference reference;

    MessageAdapter messageAdapter;
    RecyclerView recyclerView;
    List<Chat> mchat;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);


        Toolbar toolbar=findViewById(R.id.chat_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView=findViewById(R.id.chat_recyclerview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        profile_image=findViewById(R.id.chat_profile_image);
        username=findViewById(R.id.chat_username);
        send=findViewById(R.id.chat_send);
        chatmessage=findViewById(R.id.chat_message);
        final String sellerid=getIntent().getStringExtra("sellerid");
        fuser= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference().child("Users").child(sellerid);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message=chatmessage.getText().toString();
                if(!message.equals("")){
                    SendMessage(fuser.getUid(),sellerid,message);
                }
                else{
                    Toast.makeText(MessagingActivity.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
                }
                chatmessage.setText("");
            }


        });



        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users user=dataSnapshot.getValue(Users.class);
                username.setText(user.getName());
                Picasso.get().load(user.getImageurl()).into(profile_image);

                readMesage(fuser.getUid(),sellerid,user.getImageurl());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });











    }

    private void SendMessage(String sender, String reciever, String message) {

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("reciever",reciever);
        hashMap.put("message",message);

        reference.child("chats").push().setValue(hashMap);


    }

    private final void readMesage(final String myid, final String usrid, final String imageurl){
        mchat=new ArrayList<>();
        reference=FirebaseDatabase.getInstance().getReference("chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mchat.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Chat chat=snapshot.getValue(Chat.class);
                    if(chat.getReciever().equals(myid)&& chat.getSender().equals(usrid)||
                            chat.getReciever().equals(usrid)&& chat.getSender().equals(myid)
                    ){
                        mchat.add(chat);

                    }
                    messageAdapter=new MessageAdapter(MessagingActivity.this,mchat,imageurl);
                    recyclerView.setAdapter(messageAdapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

}
