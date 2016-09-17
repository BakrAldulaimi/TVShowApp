package com.example.tvshow;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tvshow.model.Flower;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * Created by bakr- on 9/17/2016.
 */
public class MyAdapter extends ArrayAdapter<Flower> {

    private Context context;
    private List<Flower> flowerList;

    // least recently used catch/ a map which has a key and a value
    private LruCache<Integer,Bitmap> imageCache;

    public MyAdapter(Context context, int resource, List<Flower> objects) {
        super(context, resource, objects);
        this.context = context;
        flowerList = objects;

        // Set max available memory
        final int maxMemory = (int)(Runtime.getRuntime().maxMemory() /1024);

        // Calculate catch size
        final int catchSize = maxMemory / 8;
        imageCache = new LruCache<>(catchSize);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.items, parent, false);

        Flower flower = flowerList.get(position);
        TextView tv = (TextView) view.findViewById(R.id.textView1);
        tv.setText(flower.getName());

        // Display photo in ImageVIew widget
        // Return the bitmap that associated with the productId
        Bitmap bitmap = imageCache.get(flower.getProductId());
        if(bitmap != null) {
            ImageView imageView = (ImageView) view.findViewById(R.id.imageView1);
            imageView.setImageBitmap(flower.getBitmap());
        } else {
            FlowerView container = new FlowerView();
            container.flower = flower;
            container.view = view;

            ImageLoader losder = new ImageLoader();
            losder.execute(container);
        }
        return view;
    }


    // Get the images for the view
    // A class to pass parameters
    class FlowerView {
        public Flower flower;
        public View view;
        public Bitmap bitmap;
    }

    private class ImageLoader extends AsyncTask<FlowerView, Void, FlowerView> {

        @Override
        protected FlowerView doInBackground(FlowerView... params) {
            // get an instance
            FlowerView container = params[0];
            Flower flower = container.flower;

            try {
                String imageURL = MainActivity.PHOTO_BASE_URL + flower.getPhoto();
                InputStream in = (InputStream) new URL(imageURL).getContent();
                Bitmap bitmap = BitmapFactory.decodeStream(in);
                flower.setBitmap(bitmap);
                in.close();
                container.bitmap = bitmap;

        return container;
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}
        @Override
        protected void onPostExecute(FlowerView result) {
            // Display photo in ImageVIew widget
            ImageView imageView = (ImageView) result.view.findViewById(R.id.imageView1);
            imageView.setImageBitmap(result.bitmap);

            // Save bitmap for future use by adding it to the flower object
            // result.flower.setBitmap(result.bitmap);
            imageCache.put(result.flower.getProductId(), result.bitmap);
        }
    }
}