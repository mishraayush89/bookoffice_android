package com.example.bookoffice.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.bookoffice.BookDetailsActivity;
import com.example.bookoffice.HomeActivity;
import com.example.bookoffice.Model.Books;
import com.example.bookoffice.R;
import com.example.bookoffice.ViewHolder.BookViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class HomeFragment extends Fragment {
    public RecyclerView books;
    private DatabaseReference bookreference;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        books= root.findViewById(R.id.recycler_menu);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());

        //reverse the layout manager
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        books.setLayoutManager(mLayoutManager);

        //firebase database reference
        bookreference= FirebaseDatabase.getInstance().getReference().child("Books");





        return root;


    }

    @Override
    public void onStart() {
        super.onStart();

        //Firebase recycler adapter

        FirebaseRecyclerOptions<Books> options=new FirebaseRecyclerOptions.Builder<Books>()
                .setQuery(bookreference,Books.class).build();

        FirebaseRecyclerAdapter<Books, BookViewHolder> adapter=new FirebaseRecyclerAdapter<Books, BookViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull BookViewHolder holder, int position, @NonNull final Books model) {
                holder.txtbookname.setText(model.getBookname());
                holder.txtbookauthor.setText("Author= "+model.getAuthor());
                holder.txtProductPrice.setText("Price = " + model.getPrice() + " Rupees");
                //to load image
                Picasso.get().load(model.getImage()).into(holder.imageview);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i=new Intent(getActivity(), BookDetailsActivity.class);
                        i.putExtra("pid",model.getPid());
                        i.putExtra("sellerid",model.getSellerid());
                        startActivity(i);

                    }
                });


            }

            @NonNull
            @Override
            public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.books_layout,parent,false);
                BookViewHolder holder =new BookViewHolder(view);
                return holder;
            }
        };

        //set the adapter to the recycler view
        books.setAdapter(adapter);
        adapter.startListening();



    }

}
