package com.markenriquez.nightlightwithtimer;


import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import static android.widget.Toast.LENGTH_LONG;

public class SlideAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;
    private boolean isPagingEnabled = true;


    public int[] images = {
    R.drawable.one,
            R.drawable.two,
            R.drawable.three,
            R.drawable.four,
            R.drawable.five,
            R.drawable.six,
            R.drawable.seven,
            R.drawable.eight,
            R.drawable.nine,
            R.drawable.ten,
            R.drawable.eleven,
            R.drawable.twelve,
            R.drawable.thirteen,
            R.drawable.fourteen,
            R.drawable.fifteen,
            R.drawable.sixteen,
            R.drawable.seventeen,
            R.drawable.eighteen,
            R.drawable.nineteen,
            R.drawable.twenty

    };
    @Override
    public int getCount() {
return images.length;
    }


    public SlideAdapter(Context context){
this.context = context;

    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view== (LinearLayout)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide, container,false);
        LinearLayout layoutside = view.findViewById(R.id.sliderLayout);
        ImageView imgslide = (ImageView) view.findViewById(R.id.sliderImage);
        imgslide.setImageResource(images[position ]);
        container.addView(view);
        return view;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout)object );
    }
}
