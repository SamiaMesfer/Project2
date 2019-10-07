package com.example.finalmovieapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class detialsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.my_row_layout);

        TextView title = (TextView) findViewById (R.id.t1);
        TextView rate = (TextView) findViewById (R.id.t2);
        TextView release = (TextView) findViewById (R.id.t3);
        TextView overview = (TextView) findViewById (R.id.t4);
        ImageView poster = (ImageView) findViewById (R.id.image1);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        String baseUrl="https://image.tmdb.org/t/p/w185";
        title.setText(intent.getExtras().getString("title"));
        rate.setText (intent.getExtras().getInt("rate") + " / 10");
        overview.setText (intent.getExtras().getString("overview"));
//        Date date1=new Date();
//        try {
//            date1=new SimpleDateFormat("yyy-mm-dd").parse(intent.getExtras().getString("release"));
//        }catch (Exception e){
//
//        }
//        DateFormat dateFormat = new SimpleDateFormat("yyyy");
//        String strDate = dateFormat.format(date1.getYear());
      //  release.setText (strDate);
        release.setText (intent.getExtras().getString("release"));
        Picasso.with(this)
                .load(baseUrl+intent.getStringExtra("poster"))
                .fit()
                .error(R.mipmap.ic_launcher_round)
                .placeholder(R.mipmap.ic_launcher_round)
                .into(poster);


    }


    private void closeOnError() {
        Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT).show();
    }
}

