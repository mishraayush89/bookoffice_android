package com.example.bookoffice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.bookoffice.Model.Users;
import com.example.bookoffice.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {


   private Button join,login;

    private ProgressDialog loadingBar;
    private String parentDbName = "Users";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        login = findViewById(R.id.login);
        join = findViewById(R.id.join_now);
        loadingBar = new ProgressDialog(this);

        Paper.init(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(register);

            }
        });


        String UserPhoneKey = Paper.book().read(Prevalent.UserPhoneKey);
        String UserPasswordKey = Paper.book().read(Prevalent.UserPasswordKey);

//        if (UserPhoneKey != "" && UserPasswordKey != "") {
//            if (!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordKey)) {
//                AllowAccess(UserPhoneKey, UserPasswordKey);
//
//                loadingBar.setTitle("Already Logged in");
//                loadingBar.setMessage("Please wait.....");
//                loadingBar.setCanceledOnTouchOutside(false);
//                loadingBar.show();
//
//
//
//            }
//        }
    }

//    private void AllowAccess(final String phone, final String password) {
//
//
//
//
//        final DatabaseReference RootRef;
//        RootRef = FirebaseDatabase.getInstance().getReference();
//
//
//        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
//            {
//
//                if (dataSnapshot.child(parentDbName).child(phone).exists())
//                {
//                    Users usersData = dataSnapshot.child(parentDbName).child(phone).getValue(Users.class);
//
//                    if (usersData.getPhone().equals(phone))
//                    {
//                        if ( usersData.getPassword().equals(password))
//                        {
//
//
//                            Toast.makeText(MainActivity.this, "logged in Successfully...", Toast.LENGTH_SHORT).show();
//                            loadingBar.dismiss();
//
//                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
//                            Prevalent.currentOnlineUser = usersData;
//                            startActivity(intent);
//
//                        }
//                        else
//                        {
//                            loadingBar.dismiss();
//                            Toast.makeText(MainActivity.this, "Password is incorrect.", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }
//                else
//                {
//                    Toast.makeText(MainActivity.this, "Account with this " + phone + " number do not exists.", Toast.LENGTH_SHORT).show();
//                    loadingBar.dismiss();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
//
//            }
//        });
//    }
}







