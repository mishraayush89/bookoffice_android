package com.example.bookoffice;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.fxn.utility.ImageQuality;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class AdminActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private ImageView bookimage;
    private EditText Inputname, Inputauthor, Inputprice;
    //private Button add;
    private Uri imageuri;
    private static final int gallerypic = 1;
    public String name, author, price, category, date, time, key, downloadImageUrl;
    private StorageReference ProductImagesRef;
    private DatabaseReference ProductsRef;
    private ProgressDialog loadingBar;
    private Spinner spinner;
    private ArrayAdapter<String> dataAdapter;
    private br.com.simplepass.loadingbutton.customViews.CircularProgressButton add;

    int RequestCode=100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        spinner = (Spinner) findViewById(R.id.spinner);
        bookimage = findViewById(R.id.book_image);
        Inputname = findViewById(R.id.Book_name);
        Inputauthor = findViewById(R.id.Author);
        Inputprice = findViewById(R.id.price);
        add = findViewById(R.id.addbtn);

        loadingBar = new ProgressDialog(this);


        bookimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opengallery();
            }


        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validatedata();
            }
        });


        spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);


        List<String> categories = new ArrayList<String>();
        categories.add(0, "Choose an Category...");
        categories.add("Science");
        categories.add("Novel");
        categories.add("Literature");
        categories.add("Engineering");
        categories.add("Comic");
        categories.add("Other");

        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(dataAdapter);


        //firebase storage and database references
        ProductImagesRef = FirebaseStorage.getInstance().getReference().child("Book Images");
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Books");


    }




    //select image from gallery
    private void opengallery() {


      /*  Intent gallery = new Intent();
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        gallery.setType("image/*");
        startActivityForResult(gallery, gallerypic);*/


        Pix.start(this, Options.init().setRequestCode(100));




    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == RequestCode) {
            ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            String path = returnValue.get(0);
            StringBuilder builder = new StringBuilder(path);
            builder.deleteCharAt(0);
            File file = new File(builder.toString());
            imageuri = Uri.fromFile(file);

            bookimage.setImageURI(imageuri);

        }
    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == gallerypic && resultCode == RESULT_OK && data != null) {
            imageuri = data.getData();
            bookimage.setImageURI(imageuri);

        }
    }*/

    //spinner
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        if (item.equals("Choose an Category...")) {
            //do nothing
        } else {
            Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
            category = item;
        }


    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

    //data validation function
    private void validatedata() {
        name = Inputname.getText().toString();
        author = Inputauthor.getText().toString();
        price = Inputprice.getText().toString();

        if (imageuri == null) {
            Toast.makeText(this, "Book image is mandatory", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Please enter book name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(author)) {
            Toast.makeText(this, "Please enter author's name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(price)) {
            Toast.makeText(this, "Please enter the price", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(category)) {
            Toast.makeText(this, "Pleases select the category", Toast.LENGTH_SHORT).show();
        } else {

            storeBookdata();
        }


    }

    // collect data and store image onto firebase storage
    private void storeBookdata() {
        /*loadingBar.setTitle("Adding New Product");
        loadingBar.setMessage("Dear Admin, please wait while we are adding the new product.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();*/
        add.startAnimation();



        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        date = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        time = currentTime.format(calendar.getTime());

        key = date + time;

        final StorageReference filePath = ProductImagesRef.child(imageuri.getLastPathSegment() + key + ".jpg");

        final UploadTask uploadTask = filePath.putFile(imageuri);


        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                String message = e.toString();
                Toast.makeText(AdminActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                add.revertAnimation();
                //loadingBar.dismiss();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminActivity.this, "Product Image uploaded Successfully...", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            downloadImageUrl = task.getResult().toString();

                            Toast.makeText(AdminActivity.this, "got the Product image Url Successfully...", Toast.LENGTH_SHORT).show();

                            SaveProductInfoToDatabase();
                        }
                    }
                });
            }
        });


    }

    //update the  firebase database
    private void SaveProductInfoToDatabase() {
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        String seller=user.getDisplayName();
        String sellerid=user.getUid();

        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid", key);
        productMap.put("date", date);
        productMap.put("time", time);
        productMap.put("author", author);
        productMap.put("image", downloadImageUrl);
        productMap.put("category", category);
        productMap.put("price", price);
        productMap.put("bookname", name);
        productMap.put("seller", seller);
        productMap.put("sellerid",sellerid);
        ProductsRef.child(key).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {


                            //loadingBar.dismiss();
                            //Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_done_white_48dp);


                            Toast.makeText(AdminActivity.this, "Product is added successfully..", Toast.LENGTH_SHORT).show();
                            Inputname.setText("");
                            Inputauthor.setText("");
                            Inputprice.setText("");
                            bookimage.setImageResource(R.drawable.camera);
                            spinner.setAdapter(dataAdapter);
                            add.stopAnimation();
                            add.revertAnimation();


                        } else {
                            //loadingBar.dismiss();

                            String message = task.getException().toString();
                            Toast.makeText(AdminActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                            add.revertAnimation();


                        }
                    }
                });

    }

}
