package com.example.android.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

class ImageAdapter extends ArrayAdapter<Movie> {

    public ImageAdapter(Context context) {
        super(context, 0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Movie movie = getItem(position);

        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            //imageView = new ImageView(getContext());
            LayoutInflater inflater = LayoutInflater.from(getContext());
            imageView = (ImageView) inflater.inflate(R.layout.movie_poster_image_view, parent, false);
        } else {
            imageView = (ImageView) convertView;
        }

        Picasso.with(getContext()).load(movie.getMoviePosterUrl()).into(imageView);
        return imageView;
    }

}
