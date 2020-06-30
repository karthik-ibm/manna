package com.ibm.manna.cart;

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
import com.ibm.manna.need.CatalogItemAdapter;
import com.ibm.manna.pojo.CatalogItems;
import com.ibm.manna.pojo.RequestItems;

import java.util.ArrayList;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.MyViewHolder> {

    ArrayList<RequestItems> requestItemsArrayList;
    public CartItemAdapter.DetailsAdapterListener onClickListener;
    Context context;

    public CartItemAdapter(Context context,ArrayList<RequestItems> requestItemsArrayList, CartItemAdapter.DetailsAdapterListener listener) {
        this.requestItemsArrayList = requestItemsArrayList;
        this.onClickListener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public CartItemAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_cart_item, parent, false);
        return new CartItemAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemAdapter.MyViewHolder holder, int position) {
        //holder.cardImage.setImageResource(requestItemsArrayList.get(position).getImage());
        holder.cardImage.setImageResource(context.getResources().getIdentifier("drawable/"+requestItemsArrayList.get(position).getImage(),null,context.getPackageName()));
        holder.cardTitle.setText(requestItemsArrayList.get(position).getItem_descr());
//        holder.cardunit.setText(catalogItemsArrayList.get(position).getUnits());
        holder.card_qnty.setText(String.valueOf(requestItemsArrayList.get(position).getItem_qty()));

    }

    @Override
    public int getItemCount() {
        return requestItemsArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView cardImage;
        TextView cardTitle,cardunit,card_qnty;
        MaterialButtonToggleGroup buttonGroup;
        Button add,sub;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardImage= itemView.findViewById(R.id.cart_item_img);
            cardTitle=itemView.findViewById(R.id.cart_item_title);
//            cardunit = itemView.findViewById(R.id.cardunit);
            card_qnty = itemView.findViewById(R.id.cart_item_qnt);
            // buttonGroup = itemView.findViewById(R.id.toggleButton);
            add = itemView.findViewById(R.id.cart_qnt_add);
            sub = itemView.findViewById(R.id.cart_qnt_sub);

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
