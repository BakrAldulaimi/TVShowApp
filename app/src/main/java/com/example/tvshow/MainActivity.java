package com.example.tvshow;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tvshow.model.Flower;
import com.example.tvshow.parsers.JSONParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ListActivity {
    ProgressBar progressBar;
    List<MyTask> tasks;
    List<Flower> flowerList;

    @SuppressWarnings("unused")
    private static final String PHOTO_BASE_URL = "http://services.hanselandpetal.com/photos/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //output = (TextView) findViewById(R.id.textView);
        //output.setMovementMethod(new ScrollingMovementMethod());

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        // Store refrences to all my tasks.
        tasks = new ArrayList<>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_do_task) {
            if(isOnline()) {
                requestData("http://services.hanselandpetal.com/feeds/flowers.json");
            } else {
                Toast.makeText(this, "Network isn't available", Toast.LENGTH_LONG).show();
            }
        }
        return false;
    }

    private void requestData(String uri) {
        // Create an instance of my Task
        MyTask myTask = new MyTask();

        // Work parralial
        myTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, uri);
    }

    // Update the view
    public void updateDisplay() {


        if(flowerList != null) {
            MyAdapter adapter = new MyAdapter(this, R.layout.items, flowerList);
            setListAdapter(adapter);
        }

    }

    // Method to check weather the network connection is available
    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        // Check now for connectivity
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    private class MyTask extends AsyncTask<String, String, List<Flower>> {

        // Executed before diInBackGround
        @Override
        protected void onPreExecute() {
            //updateDisplay();

            if (tasks.size() == 0) {
                progressBar.setVisibility(View.VISIBLE);
            }
            tasks.add(this);
        }

        // The three ... mean you can pass a list of parameters
         @Override
        protected List<Flower> doInBackground(String... params) {
            // InputStream in = null;
            String content = HttpManager.getData(params[0]);
            flowerList = JSONParser.parserFeed(content);

             // Request for the photo
         /*    for (Flower flower : flowerList) {
                 try {
                     String imageURL = PHOTO_BASE_URL + flower.getPhoto();
                     InputStream in = (InputStream) new URL(imageURL).getContent();
                     Bitmap bitmap = BitmapFactory.decodeStream(in);
                     flower.setBitmap(bitmap);
                     in.close();
                 } catch (Exception e) {
                     e.printStackTrace();
                 }
             }
             */
            return flowerList;
        }

        // Receive result
        // Executed after doInBackground
        @Override
        protected void onPostExecute(List<Flower> s) {
            updateDisplay();

            tasks.remove(this);
            if (tasks.size() == 0) {
                progressBar.setVisibility(View.INVISIBLE);
            }

            if(s == null) {
                Toast.makeText(MainActivity.this, "Can't connect to web service", Toast.LENGTH_LONG).show();
                return;
            }

            flowerList = (s);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            //updateDisplay(values[0]);
        }
    }

}
