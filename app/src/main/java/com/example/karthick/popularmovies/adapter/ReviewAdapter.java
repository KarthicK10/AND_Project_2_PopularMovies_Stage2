package com.example.karthick.popularmovies.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.karthick.popularmovies.R;
import com.example.karthick.popularmovies.data.Review;

import java.util.ArrayList;

/**
 * Created by KarthicK on 10/22/2016.
 */

public class ReviewAdapter extends ArrayAdapter {

    public ReviewAdapter(Context context, ArrayList<Review> reviewArrayList){
        super(context, R.layout.list_item_review, reviewArrayList);
        //this.mContext = context;
        //this.mReviewArrayList = mReviewArrayList;
    }

    /**
     * {@inheritDoc}
     *
     * @param position
     * @param convertView
     * @param parent
     */
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get the data item for this position
        Review review = (Review) getItem(position);
        //Check if an existing view can be reused, else inflate a new view
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_review, parent, false);
        }
        //Lookup view for data population
        TextView userNameView = (TextView)convertView.findViewById(R.id.review_item_user_name);
        TextView contentView = (TextView)convertView.findViewById(R.id.review_item_content);

        //Populate data
        userNameView.setText(review.getAuthor());
        contentView.setText(review.getContent());

        //return the created/recycled view
        return convertView;
    }
}
