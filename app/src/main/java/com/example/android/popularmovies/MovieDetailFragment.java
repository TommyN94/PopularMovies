package com.example.android.popularmovies;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetailFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ScrollView rootView = (ScrollView) inflater.inflate(R.layout.movie_detail_fragment, container, false);

        Intent intent = getActivity().getIntent();
        Movie movie = intent.getParcelableExtra(getActivity().getPackageName() + ".MOVIE");

        ImageView moviePoster = (ImageView) rootView.findViewById(R.id.movie_poster);
        Picasso.with(getActivity()).load(movie.getMoviePosterUrl()).into(moviePoster);

        TextView movieTitle = (TextView) rootView.findViewById(R.id.movie_title);
        movieTitle.setText(movie.getTitle());

        TextView movieRating = (TextView) rootView.findViewById(R.id.movie_rating);
        String rating = movie.getRating() + "/10";
        movieRating.setText(rating);

        TextView movieReleaseYear = (TextView) rootView.findViewById(R.id.movie_release_year);
        String releaseYear = movie.getReleaseDate().substring(0, 4);
        movieReleaseYear.setText(releaseYear);

        TextView movieOverview = (TextView) rootView.findViewById(R.id.movie_overview);
        movieOverview.setText(movie.getOverview());

        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return getActivity().onOptionsItemSelected(item);
    }
}
