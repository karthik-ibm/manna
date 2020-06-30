package com.ibm.manna.actioncenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.ibm.manna.R;
import com.ibm.manna.need.CatalogItemAdapter;
import com.ibm.manna.pojo.CatalogItems;
import com.ibm.manna.pojo.Requests;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActionAdapter extends RecyclerView.Adapter<ActionAdapter.MyViewHolder> {
    Context context;
    ArrayList<Requests> requestsArrayList;
    public ActionAdapter.DetailsAdapterListener onClickListener;

    public ActionAdapter(Context context, ArrayList<Requests> requestsArrayList, ActionAdapter.DetailsAdapterListener listener) {
        this.requestsArrayList = requestsArrayList;
        this.onClickListener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public ActionAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_action_center, parent, false);
        return new ActionAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ActionAdapter.MyViewHolder holder, int position) {
        String reqType, reqStatus,reqDt;
        if(requestsArrayList.get(position).getReqType().equals("N")) {
            reqType = "Needs Request";
            holder.card_action_items.setCardBackgroundColor(context.getColor(R.color.transparentGreen));
        }
        else {
            reqType = "Share Request";
            holder.card_action_items.setCardBackgroundColor(context.getColor(R.color.transparentBlue));
        }
        reqDt="Rq Dt: "+requestsArrayList.get(position).getReqDt();
        reqStatus= "Rq Status: Active";
        holder.actionType.setText(reqType);
        holder.reqDt.setText(reqDt);
        holder.reqStatus.setText(reqType);
        holder.name.setText(requestsArrayList.get(position).getFname()+","+requestsArrayList.get(position).getLname());
        holder.recyclerViewitem.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        ActionCenterItemAdapter adapter = new ActionCenterItemAdapter(requestsArrayList.get(position).getReqItems());
        holder.recyclerViewitem.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        System.out.println("adapter : "+requestsArrayList.size());
        return requestsArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView card_action_items;
        CircleImageView cardImage;
        TextView actionType,name,reqDt,reqStatus;
        Button accept,reject;
        RecyclerView recyclerViewitem;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            card_action_items = itemView.findViewById(R.id.card_action_items);
            cardImage= itemView.findViewById(R.id.actn_img);
            actionType=itemView.findViewById(R.id.actn_type);
            name = itemView.findViewById(R.id.actn_name);
            reqDt = itemView.findViewById(R.id.actn_req_dt);
            reqStatus = itemView.findViewById(R.id.actn_req_status);
            accept = itemView.findViewById(R.id.actn_accept);
            reject = itemView.findViewById(R.id.actn_reject);
            recyclerViewitem = itemView.findViewById(R.id.actn_itm_recycler);

            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.acceptOnClick(v, getAdapterPosition());
                }
            });

            reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.rejectOnClick(v, getAdapterPosition());
                }
            });

        }
    }

    public interface DetailsAdapterListener {

        void acceptOnClick(View v, int position);

        void rejectOnClick(View v, int position);
    }
}
