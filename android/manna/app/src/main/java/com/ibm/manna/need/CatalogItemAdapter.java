package com.ibm.manna.need;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.ibm.manna.R;
import com.ibm.manna.pojo.CatalogItems;

import java.util.ArrayList;

public class CatalogItemAdapter extends RecyclerView.Adapter<CatalogItemAdapter.MyViewHolder> {

    ArrayList<CatalogItems> catalogItemsArrayList;
    public DetailsAdapterListener onClickListener;
    Context context;
    public CatalogItemAdapter(Context context, ArrayList<CatalogItems> catalogItemsArrayList, DetailsAdapterListener listener) {
        this.catalogItemsArrayList = catalogItemsArrayList;
        this.onClickListener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public CatalogItemAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_subcatalog_item, parent, false);
        return new CatalogItemAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CatalogItemAdapter.MyViewHolder holder, int position) {
        holder.cardImage.setImageResource(context.getResources().getIdentifier("drawable/"+catalogItemsArrayList.get(position).getImage(), null, context.getPackageName()));
        holder.cardTitle.setText(catalogItemsArrayList.get(position).getItem_descr());
        holder.cardunit.setText(catalogItemsArrayList.get(position).getMetric());
        holder.card_qnty.setText(String.valueOf(catalogItemsArrayList.get(position).getQuantity()));

    }

    @Override
    public int getItemCount() {
        return catalogItemsArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageButton cardImage;
        TextView cardTitle,cardunit,card_qnty;
        MaterialButtonToggleGroup buttonGroup;
        Button add,sub;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardImage= itemView.findViewById(R.id.cardItemImage);
            cardTitle=itemView.findViewById(R.id.cardItemTitle);
            cardunit = itemView.findViewById(R.id.cardunit);
            card_qnty = itemView.findViewById(R.id.card_qnty);
           // buttonGroup = itemView.findViewById(R.id.toggleButton);
            add = itemView.findViewById(R.id.qnt_add);
            sub = itemView.findViewById(R.id.qnt_sub);

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.addOnClick(v, getAdapterPosition());
                }
            });

            sub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.subOnClick(v, getAdapterPosition());
                }
            });

        }
    }

    public interface DetailsAdapterListener {

        void addOnClick(View v, int position);

        void subOnClick(View v, int position);
    }
}

