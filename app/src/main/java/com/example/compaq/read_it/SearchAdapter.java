package com.example.compaq.read_it;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by COMPAQ on 14/02/18.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchHolder> {

    ArrayList<Book> books;
    Context context;

    public SearchAdapter(ArrayList<Book> books, Context context){
        this.books = books;
        this.context = context;
    }

    @Override
    public SearchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item,parent,false);
        SearchHolder bookHolder = new SearchHolder(v);
        return bookHolder;
    }

    @Override
    public void onBindViewHolder(SearchHolder holder, int position) {
        final Book current = books.get(position);
        holder.title.setText(current.getTitle());
        holder.sub.setText(current.getSubtitle());
        String authors = "";
        int i;
        for(i=0; i<current.getAuthors().size()-1;i++){
            authors = authors + current.getAuthors().get(i) + ", ";
        }
        authors += current.getAuthors().get(i);
        holder.author.setText(authors);
        Picasso.with(context).load(current.getImage_url()).into(holder.pic);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(context,DetailActivity.class);
//                i.putExtra("LINK",current.getSelflink());
//                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return books.size();
    }


    public class SearchHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView sub;
        TextView author;
        ImageView pic;
        CardView cardView;
        public SearchHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textView1);
            sub = itemView.findViewById(R.id.textView2);
            author = itemView.findViewById(R.id.textView3);
            pic = itemView.findViewById(R.id.pic);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
