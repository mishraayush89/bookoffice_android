package com.example.bookoffice;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.bookoffice.Notifications.Token;
import com.example.bookoffice.Prevalent.Prevalent;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Arrays;
import java.util.HashMap;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {


 /*  private Button join,login;

    private ProgressDialog loadingBar;
    private String parentDbName = "Users";*/


 private FirebaseAuth auth;
 private FirebaseUser user;
 private LoginButton login;
 private CallbackManager callbackManager;
 private  String name,email,image,uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login=findViewById(R.id.login_button);



        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        if(user==null){

            FacebookSdk.sdkInitialize(getApplicationContext());
            callbackManager=CallbackManager.Factory.create();

            login.setPermissions(Arrays.asList("email","public_profile"));






        }

        else{
            Intent i=new Intent(MainActivity.this,HomeActivity.class);
            startActivity(i);
            MainActivity.this.finish();
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                login.setVisibility(View.GONE);
                LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        handleFacebookToken(loginResult.getAccessToken());

                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(MainActivity.this, "User Cancelled it", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onError(FacebookException error) {
                        Toast.makeText(MainActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
        updateToken(FirebaseInstanceId.getInstance().getToken());






       /* login = findViewById(R.id.login);
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
        String UserPasswordKey = Paper.book().read(Prevalent.UserPasswordKey);*/

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

    private void updateToken(String token) {
        DatabaseReference tokenRef = FirebaseDatabase.getInstance().getReference("Tokens");
        FirebaseUser fUser=FirebaseAuth.getInstance().getCurrentUser();
        Token token1 = new Token(token);
        tokenRef.child(fUser.getUid()).setValue(token1);
    }

    private void handleFacebookToken(AccessToken accessToken) {

        AuthCredential credential= FacebookAuthProvider.getCredential(accessToken.getToken());

        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser myuserobj=auth.getCurrentUser();
                    SaveUserInfo(myuserobj);
                    /*Intent i=new Intent(MainActivity.this,HomeActivity.class);
                    startActivity(i);*/

                }
                else{
                    Toast.makeText(MainActivity.this, "Could not register", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void SaveUserInfo(FirebaseUser myuserobj) {
        name=myuserobj.getDisplayName().toString();


        email=myuserobj.getEmail();
        if(email==null){
            email="";
        }


        image=myuserobj.getPhotoUrl().toString();
        uid=myuserobj.getUid();


        final DatabaseReference rootref;
        rootref= FirebaseDatabase.getInstance().getReference();

        rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.child("Users").child(uid).exists()){
                    HashMap<String,Object> userdata=new HashMap<>();
                    userdata.put("uid",uid);
                    userdata.put("email",email);
                    userdata.put("name",name);
                    userdata.put("imageurl",image);
                    userdata.put("status","offline");

                    rootref.child("Users").child(uid).updateChildren(userdata)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        //Toast.makeText(RegisterActivity.this, "Your account has been created!", Toast.LENGTH_SHORT).show();

                                        Intent intent=new Intent(MainActivity.this,HomeActivity.class);
                                        startActivity(intent);
                                        MainActivity.this.finish();



                                    }
                                    else{



                                       /* Toast.makeText(RegisterActivity.this, "Account Not created please try again!", Toast.LENGTH_SHORT).show();*/

                                    }

                                }
                            });





                }
                else{
                   /* Toast.makeText(RegisterActivity.this, "This"+phone+"Already Exists!", Toast.LENGTH_SHORT).show();
                    loadingbar.dismiss();
                    Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
                    startActivity(intent);*/

                    Intent intent=new Intent(MainActivity.this,HomeActivity.class);
                    startActivity(intent);
                    MainActivity.this.finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);

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







