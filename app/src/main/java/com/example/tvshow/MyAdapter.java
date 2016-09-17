package com.example.tvshow;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tvshow.model.Flower;

import java.util.List;

/**
 * Created by bakr- on 9/17/2016.
 */
public class MyAdapter extends ArrayAdapter<Flower> {

    private Context context;
    private List<Flower> flowerList;

    public MyAdapter(Context context, int resource, List<Flower> objects) {
        super(context, resource, objects);
        this.context = context;
        flowerList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.items, parent, false);

        Flower flower = flowerList.get(position);
        TextView tv = (TextView) view.findViewById(R.id.textView1);
        tv.setText(flower.getName());

        // DIsplay photo in ImageVIew widget
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView1);
        imageView.setImageBitmap(flower.getBitmap());
        return view;
    }
}