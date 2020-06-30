package com.ibm.manna.actioncenter;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ibm.manna.R;
import com.ibm.manna.db_handler.Manna_DB_Handler;
import com.ibm.manna.pojo.Address;
import com.ibm.manna.pojo.Catalog;
import com.ibm.manna.pojo.Coord;
import com.ibm.manna.pojo.RequestItems;
import com.ibm.manna.pojo.Requests;
import com.ibm.manna.pojo.RequestsResponse;
import com.ibm.manna.pojo.UserProfile;
import com.ibm.manna.restapi.APIClient;
import com.ibm.manna.restapi.APIInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActionCenterFragment extends Fragment implements View.OnClickListener{
    View view;
    Button share,need;
    RecyclerView recyclerView;
    ArrayList<Requests> requests, requestByCatelogs;
    ArrayList<Requests> requests_new;

    APIInterface apiInterface;
    Manna_DB_Handler manna_db_handler;
    UserProfile userProfile;
    ActionAdapter actionAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_action_center, container, false);
        recyclerView = view.findViewById(R.id.recycler_action);
        need = view.findViewById(R.id.actn_button1);
        share = view.findViewById(R.id.actn_button2);

        need.setOnClickListener(this);
        share.setOnClickListener(this);
        need.setBackgroundColor(getContext().getColor(R.color.buttonGreen));

        apiInterface = APIClient.getClient().create(APIInterface.class);
        requests = new ArrayList<>();
        requests_new = new ArrayList<>();
        ArrayList<RequestItems> requestItems = new ArrayList();


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        actionAdapter = new ActionAdapter(getContext(),requests, new ActionAdapter.DetailsAdapterListener() {
            @Override
            public void acceptOnClick(View v, int position) {

            }

            @Override
            public void rejectOnClick(View v, int position) {

            }
        });
        getMyRequests("N");
        recyclerView.setAdapter(actionAdapter);
        actionAdapter.notifyDataSetChanged();




        return  view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.actn_button1:
                need.setBackgroundColor(getContext().getColor(R.color.buttonGreen));
                share.setBackgroundColor(getContext().getColor(R.color.colorWhite));
                getMyRequests("N");
                actionAdapter.notifyDataSetChanged();
                break;
            case R.id.actn_button2:
                share.setBackgroundColor(getContext().getColor(R.color.buttonBlue));
                need.setBackgroundColor(getContext().getColor(R.color.colorWhite));
                getMyRequests("S");
                //getRequests("S");
                actionAdapter.notifyDataSetChanged();
                break;
        }
    }
    public void getMyRequests(String reqType)
    {
        manna_db_handler = new Manna_DB_Handler(getContext());
        userProfile =manna_db_handler.getUserProfileInfo();
        Call<RequestsResponse> call = apiInterface.getMyRequests(userProfile.getMannaID());
        //location.getLongitude(),location.getLatitude());
        call.enqueue(new Callback<RequestsResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<RequestsResponse> call, Response<RequestsResponse> response) {

                if(response.body()!=null) {
                    RequestsResponse requestsResponse= response.body();

                    manna_db_handler.deleteMyRequestTable(userProfile.getMannaID());
                    manna_db_handler.addRequests(requestsResponse.getMyrequest(),"my");
                    requests_new = requestsResponse.getMyrequest();

                    actionAdapter.notifyDataSetChanged();
                    getRequests(reqType);
                }
                else {
                    Toast.makeText(getContext(), "500 Internal Server Error", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<RequestsResponse> call, Throwable t)
            {
                System.out.println("Error Block");
                call.cancel();
            }
        });

        actionAdapter.notifyDataSetChanged();

    }
    public void getRequests(String reqType)
    {
        requests.clear();
        for(int i = 0; i< requests_new.size();i++)
        {
            if(requests_new.get(i).getReqType().equals(reqType))
                requests.add(requests_new.get(i));
        }
        System.out.println("request count: "+requests_new.size() +",,"+requests.size()+",,"+reqType);
        recyclerView.setAdapter(actionAdapter);
        actionAdapter.notifyDataSetChanged();
    }
}
