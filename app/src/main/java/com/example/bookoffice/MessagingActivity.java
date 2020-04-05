package com.example.bookoffice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookoffice.Adapters.MessageAdapter;
import com.example.bookoffice.Interface.APIService;
import com.example.bookoffice.Model.Chat;
import com.example.bookoffice.Model.Users;
import com.example.bookoffice.Notifications.Client;
import com.example.bookoffice.Notifications.Data;
import com.example.bookoffice.Notifications.MyResponse;
import com.example.bookoffice.Notifications.Sender;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.bookoffice.Notifications.Token;


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
    String userimageurl;

    ValueEventListener seenListner;

    APIService apiService;
    String sellerid;
    boolean notify=false;

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


        apiService= Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        recyclerView=findViewById(R.id.chat_recyclerview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        profile_image=findViewById(R.id.chat_profile_image);
        username=findViewById(R.id.chat_username);
        send=findViewById(R.id.chat_send);
        chatmessage=findViewById(R.id.chat_message);
        sellerid=getIntent().getStringExtra("sellerid");
        fuser= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference().child("Users").child(sellerid);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify=true;
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
                userimageurl=user.getImageurl();

                readMesage(fuser.getUid(),sellerid);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        seenMessage(sellerid);











    }

    private void seenMessage(final String userid){
        reference=FirebaseDatabase.getInstance().getReference("chats");

        seenListner=reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Chat chat=snapshot.getValue(Chat.class);
                    if(chat.getReciever().equals(fuser.getUid()) && chat.getSender().equals(userid)){
                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put("isseen",true);
                        snapshot.getRef().updateChildren(hashMap);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void SendMessage(String sender, final String reciever, String message) {

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("reciever",reciever);
        hashMap.put("message",message);
        hashMap.put("isseen",false);

        reference.child("chats").push().setValue(hashMap);
        final String msg=message;
        reference=FirebaseDatabase.getInstance().getReference().child("Users").child(fuser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users user=dataSnapshot.getValue(Users.class);

                if(notify){
                sendNotification(reciever,user.getName(),msg);

                }else{
                    /*Toast.makeText(MessagingActivity.this, "Hi There", Toast.LENGTH_SHORT).show();*/
                }
                notify=false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void sendNotification(String reciever, final String username, final String message){
        DatabaseReference tokens=FirebaseDatabase.getInstance().getReference("Tokens");
        Query query=tokens.orderByKey().equalTo(reciever);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Token token=snapshot.getValue(Token.class);
                    Data data=new Data(username+": "+message,sellerid
                            ,"New Message",fuser.getUid(),R.mipmap.ic_launcher);

                    Sender sender=new Sender(data,token.getToken());
                    apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
                        @Override
                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                            if(response.code()==200){
                                if(response.body().success!=1){
                                    Toast.makeText(MessagingActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                }
                            }

                        }

                        @Override
                        public void onFailure(Call<MyResponse> call, Throwable t) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private final void readMesage(final String myid, final String usrid){
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
                    messageAdapter=new MessageAdapter(MessagingActivity.this,mchat,userimageurl);
                    recyclerView.setAdapter(messageAdapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void currentUser(String userid){
        SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
        editor.putString("currentuser", userid);
        editor.apply();
    }


    private  void status(String status){

        FirebaseUser fuser=FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("status",status);

        reference.updateChildren(hashMap);




    }

    @Override
    protected void onStart(){
        super.onStart();
        status("online");
        currentUser(sellerid);
    }

    @Override
    protected void onPause(){
        super.onPause();
        reference.removeEventListener(seenListner);
        status("offline");
        currentUser("none");
    }

}
