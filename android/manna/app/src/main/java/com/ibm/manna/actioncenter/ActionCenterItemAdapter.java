package com.ibm.manna.actioncenter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ibm.manna.R;
import com.ibm.manna.pojo.RequestItems;

import java.util.ArrayList;

public class ActionCenterItemAdapter extends RecyclerView.Adapter<ActionCenterItemAdapter.MyViewHolder> {

    ArrayList<RequestItems> requestItemsArrayList;

    public ActionCenterItemAdapter(ArrayList<RequestItems> requestItemsArrayList) {
        this.requestItemsArrayList = requestItemsArrayList;
    }

    @NonNull
    @Override
    public ActionCenterItemAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_action_req_items, parent, false);
        return new ActionCenterItemAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ActionCenterItemAdapter.MyViewHolder holder, int position) {
        holder.actionItemType.setText(requestItemsArrayList.get(position).getItem_descr());
        holder.actionItemQnty.setText(String.valueOf(requestItemsArrayList.get(position).getItem_qty()));
        holder.actionItemMetric.setText(requestItemsArrayList.get(position).getMetric());
    }

    @Override
    public int getItemCount() {
        return requestItemsArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView actionItemType,actionItemQnty,actionItemMetric;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            actionItemType= itemView.findViewById(R.id.reqItem);
            actionItemQnty=itemView.findViewById(R.id.reqQnty);
            actionItemMetric = itemView.findViewById(R.id.reqMetric);
        }
    }

}
