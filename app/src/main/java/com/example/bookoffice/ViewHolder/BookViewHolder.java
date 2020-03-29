package com.example.bookoffice.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookoffice.Interface.ItemClickListner;
import com.example.bookoffice.R;

public class BookViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtbookname,txtbookauthor,txtProductPrice;
    public ImageView imageview;
    public ItemClickListner listner;

    public BookViewHolder(@NonNull View itemView) {
        super(itemView);
        imageview=itemView.findViewById(R.id.product_image);
        txtbookname=itemView.findViewById(R.id.product_name);
        txtbookauthor=itemView.findViewById(R.id.Author);
        txtProductPrice = (TextView) itemView.findViewById(R.id.product_price);





    }

    public void setItemClickListner(ItemClickListner listner)
    {
        this.listner = listner;
    }


    @Override
    public void onClick(View v) {

    }
}
