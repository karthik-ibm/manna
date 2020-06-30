package com.ibm.manna.need;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.ibm.manna.R;
import com.ibm.manna.pojo.Catalog;
import com.ibm.manna.pojo.CatalogItems;

import java.util.ArrayList;

public class MapCarouselAdapter extends RecyclerView.Adapter<MapCarouselAdapter.MyViewHolder>  {

    ArrayList<Catalog> catalogArrayList;
    Context context;

    public MapCarouselAdapter(Context context,ArrayList<Catalog> catalogArrayList) {
        this.context = context;
        this.catalogArrayList = catalogArrayList;
    }

    @NonNull
    @Override
    public MapCarouselAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_carousel_image, parent, false);
        return new MapCarouselAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MapCarouselAdapter.MyViewHolder holder, int position) {
       holder.cardImage.setImageResource(context.getResources().getIdentifier("drawable/"+catalogArrayList.get(position).getImage(), null, context.getPackageName()));
       // holder.cardTitle.setText(catalogArrayList.get(position).getCat_descr());
        /*holder.cardunit.setText(catalogItemsArrayList.get(position).getUnits());
        holder.card_qnty.setText(String.valueOf(catalogItemsArrayList.get(position).getQuantity()));*/

    }

    @Override
    public int getItemCount() {
        return catalogArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView cardImage;
        TextView cardTitle;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardImage= itemView.findViewById(R.id.carousel_image);
            /*cardunit = itemView.findViewById(R.id.cardunit);
            card_qnty = itemView.findViewById(R.id.card_qnty);*/
            // buttonGroup = itemView.findViewById(R.id.toggleButton);
//            add = itemView.findViewById(R.id.qnt_add);
//            sub = itemView.findViewById(R.id.qnt_sub);



        }
    }
}
