package com.example.prashantbhardwaj.teqnitask;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

/**
 * Created by prashantbhardwaj on 11/08/17.
 */

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder>{
    private ImageLoader imageLoader;
    private Context context;

    //List of superHeroes
    List<SuperHeroes> superHeroes;

    public CardAdapter(List<SuperHeroes> superHeroes, Context context){
        super();
        //Getting all the superheroes
        this.superHeroes = superHeroes;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.superheroes_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final SuperHeroes superHero =  superHeroes.get(position);

        imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        imageLoader.get(superHero.getImageUrl(), ImageLoader.getImageListener(holder.imageView, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));

        holder.imageView.setImageUrl(superHero.getImageUrl(), imageLoader);
        holder.textViewDate.setText(String.valueOf(superHero.getDate()));
        holder.textViewTimeDuration.setText(superHero.getTimeDuration());
        holder.bEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, EditPostActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("postid", superHero.getPostid().toString());
                i.putExtra("timeDuration", superHero.getTimeDuration().toString());
                i.putExtra("pos", superHero.getPos().toString());
                context.startActivity(i);
            }
        });
        holder.bDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, DeletePostActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("postid", superHero.getPostid().toString());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return superHeroes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public NetworkImageView imageView;
        public TextView textViewDate;
        public TextView textViewTimeDuration;
        public ImageButton bEdit;
        public ImageButton bDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (NetworkImageView) itemView.findViewById(R.id.imageViewHero);
            textViewDate= (TextView) itemView.findViewById(R.id.textViewDate);
            textViewTimeDuration= (TextView) itemView.findViewById(R.id.textViewTimeDuration);
            bEdit = (ImageButton) itemView.findViewById(R.id.bEdit);
            bDelete = (ImageButton) itemView.findViewById(R.id.bDelete);
        }
    }
}
