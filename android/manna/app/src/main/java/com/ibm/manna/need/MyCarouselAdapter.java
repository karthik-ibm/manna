package com.ibm.manna.need;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ibm.manna.R;
import com.ibm.manna.pojo.Catalog;
import com.ibm.manna.pojo.Photo;

import java.util.ArrayList;
import java.util.List;

import fr.rolandl.carousel.CarouselAdapter;
import fr.rolandl.carousel.CarouselItem;

public class MyCarouselAdapter extends CarouselAdapter<Catalog> {
    public static final class PhotoItem
            extends CarouselItem<Catalog>
    {

        private ImageView image;

        private TextView name;

        private Context context;

        public PhotoItem(Context context)
        {
            super(context, R.layout.card_carousel_image);
            this.context = context;
        }

        @Override
        public void extractView(View view)
        {
            image = (ImageView) view.findViewById(R.id.carousel_image);
            //name = (TextView) view.findViewById(R.id.name);
        }

        @Override
        public void update(Catalog catalog)
        {
            //image.setImageResource(catalog.getImage());
           // image.setImageResource(getResources().getIdentifier(catalog.image, "drawable", context.getPackageName()));
           // image.setImageResource(getResources().getIdentifier(catalog.image, "drawable", context.getPackageName()));
            //name.setText(photo.name);
        }

    }

    public MyCarouselAdapter(Context context, ArrayList<Catalog> catalog)
    {
        super(context, catalog);
    }

    @Override
    public CarouselItem<Catalog> getCarouselItem(Context context)
    {
        return new PhotoItem(context);
    }
}
