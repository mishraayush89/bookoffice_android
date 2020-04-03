package com.example.bookoffice.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookoffice.Adapters.UserAdapter;
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

import java.util.ArrayList;
import java.util.List;


public class GalleryFragment extends Fragment {
    private UserAdapter userAdapter;
    private List<Users> musers;
    private List<Users> container;

    FirebaseUser fuser;
    DatabaseReference reference;

    private List<String> usersList;
    private RecyclerView recyclerView;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        recyclerView=view.findViewById(R.id.recycler_view_chat_home);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        fuser= FirebaseAuth.getInstance().getCurrentUser();

        usersList=new ArrayList<>();

        reference= FirebaseDatabase.getInstance().getReference("chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();

                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Chat chat=snapshot.getValue(Chat.class);

                    if(chat.getSender().equals(fuser.getUid())){
                        usersList.add(chat.getReciever());
                    }
                    if(chat.getReciever().equals(fuser.getUid())){
                        usersList.add(chat.getSender());

                    }
                }
                readChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






        return view;
    }


    private  void readChats(){
        musers=new ArrayList<>();
        container=new ArrayList<>();

        reference=FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                musers.clear();

                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Users user=snapshot.getValue(Users.class);

                    for(String id: usersList){
                        if(user.getUid().equals(id)){

                            if(musers.size()!=0){

                                for(Users user1 : new ArrayList< Users >(musers)){
                                    if(!user.getUid().equals(user1.getUid())){
                                        if(!container.contains(user)){
                                            musers.add(user);
                                            container.add(user);

                                        }


                                    }
                                }

                            }else {
                                musers.add(user);
                            }
                        }
                    }
                }
                UserAdapter userAdapter=new UserAdapter(getContext(),musers);
                recyclerView.setAdapter(userAdapter);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
