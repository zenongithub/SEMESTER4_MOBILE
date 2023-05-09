package com.shedenk.app.ui.beranda;

import com.smarteist.autoimageslider.SliderViewAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.shedenk.app.R;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.ViewHolder> {

    int[] images;

    public SliderAdapter(int[] images){
        this.images = images;
    }

    @Override
    public SliderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_slider,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        viewHolder.imageView.setImageResource(images[position]);
    }

    @Override
    public int getCount() {
        return images.length;
    }

    public class ViewHolder extends SliderViewAdapter.ViewHolder{

        ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_view);
        }
    }
}
