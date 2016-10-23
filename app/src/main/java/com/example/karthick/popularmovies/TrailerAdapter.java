package com.example.karthick.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.karthick.popularmovies.data.Video;

import java.util.ArrayList;

/**
 * Created by KarthicK on 10/23/2016.
 *
 * Adapter for Trailers list in Movie Trailers screen
 */

public class TrailerAdapter extends ArrayAdapter {

    public TrailerAdapter(Context context, ArrayList<Video> videoArrayList){
        super(context, R.layout.list_item_trailer, videoArrayList);
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
        Video trailer = (Video) getItem(position);
        //Check if an existing view can be reused, else inflate a new view
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_trailer, parent, false);
        }
        //Lookup view for data population
        TextView trailerNameView = (TextView)convertView.findViewById(R.id.trailer_item_name);

        //Populate data
        trailerNameView.setText(trailer.getName());

        //return the created/recycled view
        return convertView;
    }
}
