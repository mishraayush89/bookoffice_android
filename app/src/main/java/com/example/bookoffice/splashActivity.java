package com.example.bookoffice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.bookoffice.Model.Users;
import com.example.bookoffice.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class splashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Paper.init(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        LogoLauncher logoLauncher=new LogoLauncher();
        logoLauncher.start();


    }
    public class LogoLauncher extends Thread{


        private String parentDbName = "Users";
        public void run(){

            try{
                sleep(1000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }


            String UserPhoneKey = Paper.book().read(Prevalent.UserPhoneKey);
            String UserPasswordKey = Paper.book().read(Prevalent.UserPasswordKey);
            if (UserPhoneKey != "" && UserPasswordKey != "") {
                if (!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordKey)) {
                    AllowAccess(UserPhoneKey, UserPasswordKey);

                }
                else{
                    Intent i;
                    i = new Intent(splashActivity.this,MainActivity.class);
                    startActivity(i);
                    splashActivity.this.finish();

                }
            }
            else{
                Intent i;
                i = new Intent(splashActivity.this,MainActivity.class);
                startActivity(i);
                splashActivity.this.finish();

            }




        }
        private void AllowAccess(final String phone, final String password) {




            final DatabaseReference RootRef;
            RootRef = FirebaseDatabase.getInstance().getReference();


            RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {

                    if (dataSnapshot.child(parentDbName).child(phone).exists())
                    {
                        Users usersData = dataSnapshot.child(parentDbName).child(phone).getValue(Users.class);

                        if (usersData.getPhone().equals(phone))
                        {
                            if ( usersData.getPassword().equals(password))
                            {


                                //Toast.makeText(splashActivity.this, "logged in Successfully...", Toast.LENGTH_SHORT).show();


                                Intent intent = new Intent(splashActivity.this, HomeActivity.class);
                                Prevalent.currentOnlineUser = usersData;
                                startActivity(intent);
                                splashActivity.this.finish();

                            }
                            else
                            {

                                Toast.makeText(splashActivity.this, "Password is incorrect.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    else
                    {
                        Toast.makeText(splashActivity.this, "Account with this " + phone + " number do not exists.", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(splashActivity.this, "Error", Toast.LENGTH_SHORT).show();

                }
            });
        }



    }
}
