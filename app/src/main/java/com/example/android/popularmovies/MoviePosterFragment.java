package com.example.android.popularmovies;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MoviePosterFragment extends Fragment {

    private ImageAdapter movieAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isOnline()) {
            new FetchMovieDataTask().execute();
        } else {
            String text ="Can't load movies: no internet connection!";
            new Toast(getActivity()).makeText(getActivity(), text, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.movie_poster_fragment, container, false);

        GridView movieGridView = (GridView) rootView.findViewById(R.id.movie_poster_grid_view);
        movieAdapter = new ImageAdapter(getActivity());
        movieGridView.setAdapter(movieAdapter);
        movieGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Movie movie = (Movie) adapterView.getAdapter().getItem(i);
                String title = movie.getTitle();
                Intent displayMovieDetails = new Intent(getActivity(), MovieDetailActivity.class);
                String name = getActivity().getPackageName() + ".MOVIE";
                displayMovieDetails.putExtra(name, movie);
                startActivity(displayMovieDetails);
            }
        });
        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return getActivity().onOptionsItemSelected(item);
    }

    private boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return exitValue == 0;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    private class FetchMovieDataTask extends AsyncTask<Void, Void, Movie[]> {
        private final String LOG_TAG = FetchMovieDataTask.class.getSimpleName();
        private String baseUrl = "http://api.themoviedb.org/3/movie";
        private String imageBaseUrl = "http://image.tmdb.org/t/p/";
        private String imageSize = "w342";

        @Override
        protected Movie[] doInBackground(Void... params) {
            try {
                return getMoviesFromJson(fetchMovieData());
            } catch (JSONException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute (Movie[] result) {
            if (result != null) {
                movieAdapter.clear();
                movieAdapter.addAll(result);
            }
        }

        private String fetchMovieData() {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String sortBy = prefs.getString(getString(R.string.sort_preference_key),
                    getString(R.string.sort_preference_default_value));
            URL url = constructUrl(sortBy);
            HttpURLConnection urlConnection = null;
            String jsonMovies = null;
            BufferedReader reader = null;

            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    jsonMovies = null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                        /*
                        * Add a newline to the JSON String in order to make it more readable in case
                        * of debugging
                        */
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    jsonMovies = null;
                } else {
                    jsonMovies = buffer.toString();
                }

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error: ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream: ", e);
                    }
                }
            }  // end finally
            return jsonMovies;
        }

        private Movie[] getMoviesFromJson(String jsonMovieString) throws JSONException {
            JSONObject jsonMovie = new JSONObject(jsonMovieString);
            JSONArray resultArray = jsonMovie.getJSONArray("results");
            int nMovies = resultArray.length();
            Movie[] movies = new Movie[nMovies];

            for (int i = 0; i < nMovies; i++) {
                JSONObject movie = resultArray.getJSONObject(i);
                movies[i] = new Movie(movie.getString("title"));
                movies[i].setOverview(movie.getString("overview"));
                movies[i].setReleaseDate(movie.getString("release_date"));
                movies[i].setRating(movie.getDouble("vote_average"));
                movies[i].setMoviePosterUrl(imageBaseUrl + imageSize + movie.getString("poster_path"));
            }
            return movies;
        }

        private URL constructUrl(String sortBy) {
            Uri uri = Uri.parse(baseUrl)
                    .buildUpon()
                    .appendPath(sortBy)
                    .appendQueryParameter("api_key", BuildConfig.THE_MOVIE_DB_API_KEY)
                    .build();
            try {
                return new URL(uri.toString());
            } catch (MalformedURLException e) {
                return null;
            }
        }
    }
}
