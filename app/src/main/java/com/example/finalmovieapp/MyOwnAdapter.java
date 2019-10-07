package com.example.finalmovieapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyOwnAdapter extends RecyclerView.Adapter<MyOwnAdapter.MyOwnHolder> {

    Movie[]  movies;
    Context ct;

    public MyOwnAdapter(Context ct, Movie[] movie){
        this.movies=movie;
        this.ct=ct;

    }




    @NonNull
    @Override
    public MyOwnHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layotinf=LayoutInflater.from(parent.getContext ());
        ImageView view =(ImageView)layotinf.inflate(R.layout.image_view,parent,false);
        MyOwnHolder myOwnHolder= new MyOwnHolder(view);
        return myOwnHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull MyOwnHolder holder, int position) {
        final int positionn=position;
       String baseUrl="https://image.tmdb.org/t/p/w185";
        Picasso.with(ct)
                .load(baseUrl+movies[positionn].getPosterPath())
                .fit()
                .error(R.mipmap.ic_launcher_round)
                .placeholder(R.mipmap.ic_launcher_round)
                .into((ImageView)
                 holder.img.findViewById(R.id.imgView));

    }

    private void populateUI() {
    }

    @Override
    public int getItemCount() {
        return movies.length;
    }

    public class MyOwnHolder extends  RecyclerView.ViewHolder{
        ImageView img;


        public MyOwnHolder(@NonNull ImageView itemView) {
            super(itemView);
            img=itemView;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ct, detialsActivity.class);
                    intent.putExtra(Intent.EXTRA_TEXT, getAdapterPosition());
                    intent.putExtra("title", movies[getAdapterPosition()].getTitle());
                    intent.putExtra("poster", movies[getAdapterPosition()].getPosterPath());
                    intent.putExtra("rate", movies[getAdapterPosition()].getVoteAverage());
                    intent.putExtra("release", movies[getAdapterPosition()].getReleaseDate());
                    intent.putExtra("overview", movies[getAdapterPosition()].getOverview());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ct.startActivity(intent);
                }
            });
        }
    }

}
