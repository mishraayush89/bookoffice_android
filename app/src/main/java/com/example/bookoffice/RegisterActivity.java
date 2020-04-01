package com.example.bookoffice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private Button register;
    private EditText Name,Phone,Password;
    private ProgressDialog loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register=findViewById(R.id.registerbtn);
        Name=findViewById(R.id.name);
        Phone=findViewById(R.id.reg_ph_number);
        Password=findViewById(R.id.regpassword);
        loadingbar=new ProgressDialog(this);
        
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });
    }

    private void createAccount() {
        String name=Name.getText().toString();
        String password=Password.getText().toString();
        String phone=Phone.getText().toString();

        if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Please enter your Phone Number", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter your Password", Toast.LENGTH_SHORT).show();
        }
        else{
            loadingbar.setTitle("Creating Account");
            loadingbar.setMessage("please wait while we are checking the credentials!");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();

            validate(name,phone,password);


        }




    }

    private void validate(final String name, final String phone, final String password) {
        final DatabaseReference rootref;
        rootref= FirebaseDatabase.getInstance().getReference();

        rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.child("Users").child(phone).exists()){
                    HashMap<String,Object> userdata=new HashMap<>();
                    userdata.put("phone",phone);
                    userdata.put("password",password);
                    userdata.put("name",name);

                    rootref.child("Users").child(phone).updateChildren(userdata)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this, "Your account has been created!", Toast.LENGTH_SHORT).show();
                                        loadingbar.dismiss();
                                        Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                                        startActivity(intent);


                                    }
                                    else{
                                        loadingbar.dismiss();
                                        Toast.makeText(RegisterActivity.this, "Account Not created please try again!", Toast.LENGTH_SHORT).show();

                                    }

                                }
                            });





                }
                else{
                    Toast.makeText(RegisterActivity.this, "This"+phone+"Already Exists!", Toast.LENGTH_SHORT).show();
                    loadingbar.dismiss();
                    Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}

