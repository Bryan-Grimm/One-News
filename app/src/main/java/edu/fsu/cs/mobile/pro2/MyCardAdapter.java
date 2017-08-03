package edu.fsu.cs.mobile.pro2;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;



public class MyCardAdapter extends RecyclerView.Adapter<MyCardAdapter.MyCardViewHolder> {

    List<MyCard> cardList;

    public static class MyCardViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView newsTitle;
        TextView newsDescription;
        ImageView newsImage;

        MyCardViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            newsTitle = (TextView)itemView.findViewById(R.id.news_title);
            newsDescription = (TextView)itemView.findViewById(R.id.news_description);
            newsImage = (ImageView)itemView.findViewById(R.id.news_image);
        }
    }

    MyCardAdapter(List<MyCard> news) {
        cardList = news;
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    @Override
    public MyCardViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.news_card, viewGroup, false);
        MyCardViewHolder nvh = new MyCardViewHolder(v);
        return nvh;
    }

    @Override
    public void onBindViewHolder(MyCardViewHolder newsViewHolder, final int i) {
        newsViewHolder.newsTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyCard c = cardList.get(i);
                if (c.url != null) {
                    Intent bIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(c.url));
                    Intent bChooseIntent = Intent.createChooser(bIntent, "Select an App");
                    v.getContext().startActivity(bChooseIntent);
                }
            }
        });
        newsViewHolder.newsTitle.setText(cardList.get(i).title);
        newsViewHolder.newsDescription.setText(cardList.get(i).description);

        Picasso.with(newsViewHolder.newsImage.getContext()).load(cardList.get(i).image).resize(180,180).into(newsViewHolder.newsImage);

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
