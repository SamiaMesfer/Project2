package com.example.finalmovieapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import androidx.appcompat.widget.Toolbar;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Movie> moveList;
   // ArrayList<Movie> mTopTopRatedList;
    MyOwnAdapter adapter;
    RecyclerView r1;
    Movie[] movies;
    String query = "popular";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        r1=(RecyclerView) findViewById(R.id.r1);
        ArrayList<Movie> movies= new ArrayList<Movie>();
        moveList = new ArrayList<>();
        LinearLayoutManager layoutManger;
        //mTopTopRatedList = new ArrayList<>();
        loadData();
        layoutManger= new GridLayoutManager(this,2);
        r1.setLayoutManager(layoutManger);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItemSelected = item.getItemId();

        if (menuItemSelected == R.id.action_popular) {
            query = "popular";
            loadData();
            return true;
        }

        if (menuItemSelected == R.id.action_top_rated) {
            query = "top_rated";
            loadData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void loadData() {
        new fetchTask().execute(query);
    }

    public  class fetchTask extends AsyncTask<String, Void, Movie[]> {

        final static String baseUrl = "https://api.themoviedb.org/3/movie";
        final static String apiKey = "api_key";
        //please insert key below
        final static String key = "please insert key here !!!";
        final static String lang = "language";
        final static String langUS = "en-US";


        public fetchTask() {
            super();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected Movie [] doInBackground(String... x) {

            try {
                if(networkStatus(MainActivity.this)){
                    moveList = fetchData(buildUrl(x[0]).toString());
                }else{
                    Toast.makeText(MainActivity.this,"No Internet Connection",Toast.LENGTH_LONG).show();
                }

                return moveList.toArray(new Movie[moveList.size()]);

            } catch (IOException e){
                e.printStackTrace();
            }

           return null;
        }

        @Override
        protected void onPostExecute(Movie[] movies) {
            adapter = new MyOwnAdapter(getApplicationContext(),movies);
            r1.setAdapter(adapter);
        }



        public  URL buildUrl(String theMovieDbSearchQuery){
            Uri builtUri = Uri.parse(baseUrl).buildUpon()
                    .appendEncodedPath(theMovieDbSearchQuery)
                    .appendQueryParameter(apiKey, key)
                    .appendQueryParameter(lang, langUS)
                    .build();

            URL url = null;
            try{
                url = new URL(builtUri.toString());
            } catch (MalformedURLException e){
                e.printStackTrace();
            }
            return url;
        }


        public  ArrayList<Movie> fetchData(String url) throws IOException {
            ArrayList<Movie> movies = new ArrayList<Movie>();
            try {

                URL new_url = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) new_url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                StringBuilder stringBuilder = new StringBuilder();
                String line = null;
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
                while ((line = bufferedReader.readLine()) != null)  {
                    stringBuilder.append(line);
                }

                String results= stringBuilder.toString();


                parseJson(results,movies);
                inputStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return movies;
        }

        public  void parseJson(String data, ArrayList<Movie> list){

            try {
                JSONObject mainObject = new JSONObject(data);
                JSONArray resArray = mainObject.getJSONArray("results");
                for (int i = 0; i < resArray.length(); i++) {
                    JSONObject jsonObject = resArray.getJSONObject(i);
                    Movie movie = new Movie();
                    movie.setTitle(jsonObject.getString("title"));
                    movie.setVoteAverage(jsonObject.getInt("vote_average"));
                    movie.setOverview(jsonObject.getString("overview"));
                    movie.setReleaseDate(jsonObject.getString("release_date"));
                    movie.setPosterPath(jsonObject.getString("poster_path"));
                    list.add(movie);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        public  Boolean networkStatus(Context context){
            ConnectivityManager manager = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()){
                return true;
            }
            return false;
        }

    }

}

