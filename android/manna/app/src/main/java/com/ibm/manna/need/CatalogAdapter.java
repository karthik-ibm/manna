package com.ibm.manna.need;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ibm.manna.R;
import com.ibm.manna.pojo.Catalog;

import java.util.ArrayList;

public class CatalogAdapter extends RecyclerView.Adapter<CatalogAdapter.MyViewHolder> {

    ArrayList<Catalog> catalogArrayList;
    Context context;
    public CatalogAdapter(Context context,ArrayList<Catalog> catalogArrayList) {
        this.catalogArrayList = catalogArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CatalogAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_catalogitem, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CatalogAdapter.MyViewHolder holder, int position) {
        holder.cardImage.setImageResource(context.getResources().getIdentifier("drawable/"+catalogArrayList.get(position).getImage(), null, context.getPackageName()));
        holder.cardTitle.setText(catalogArrayList.get(position).getCat_descr());
    }

    @Override
    public int getItemCount() {
        return catalogArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageButton cardImage;
        TextView cardTitle;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardImage= itemView.findViewById(R.id.cardImage);
            cardTitle=itemView.findViewById(R.id.cardTitle);
        }
    }
}
