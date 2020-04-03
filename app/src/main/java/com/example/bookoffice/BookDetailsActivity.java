package com.example.bookoffice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookoffice.Model.Books;
import com.example.bookoffice.Model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class BookDetailsActivity extends AppCompatActivity {
    private TextView bookname,bookprice,bookseller,backbtn;
    private Button chat,map;
    private ImageView bookimage;

    private String pid,sellerid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        pid=getIntent().getStringExtra("pid");
        sellerid=getIntent().getStringExtra("sellerid");


        bookname=findViewById(R.id.bookdetails_name);
        bookimage=findViewById(R.id.bookdetails_image);
        bookprice=findViewById(R.id.bookdetails_price);
        bookseller=findViewById(R.id.bookdetails_seller);
        backbtn=findViewById(R.id.bookdetails_back);

        chat=findViewById(R.id.chatwith_user);
        getBookDetails();

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(BookDetailsActivity.this,MessagingActivity.class);
                i.putExtra("sellerid",sellerid);
                startActivity(i);
            }
        });




















    }

    private void getBookDetails() {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        final String uid= FirebaseAuth.getInstance().getUid();


        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.child("Users").child(uid).exists())
                {
                    Books bookdata = dataSnapshot.child("Books").child(pid).getValue(Books.class);

                    if (bookdata.getPid().equals(pid)) {
                        String name=bookdata.getBookname();
                        bookname.setText(name);


                        Picasso.get().load(bookdata.getImage()).into(bookimage);

                        bookprice.setText(bookdata.getPrice());
                        bookseller.setText(bookdata.getSeller());









                    }
                    else
                    {

                        //Toast.makeText(LoginActivity.this, "Password is incorrect.", Toast.LENGTH_SHORT).show();
                    }
                }

                else
                {
                    /*Toast.makeText(LoginActivity.this, "Account with this " + phone + " number do not exists.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();*/
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(BookDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
