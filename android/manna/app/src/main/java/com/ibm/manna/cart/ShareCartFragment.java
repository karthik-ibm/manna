package com.ibm.manna.cart;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.ibm.manna.HomeFragment;
import com.ibm.manna.R;
import com.ibm.manna.db_handler.Manna_DB_Handler;
import com.ibm.manna.pojo.Cart;
import com.ibm.manna.pojo.Catalog;
import com.ibm.manna.pojo.CatalogItems;
import com.ibm.manna.pojo.Coord;
import com.ibm.manna.pojo.CreateRequestResponse;
import com.ibm.manna.pojo.RequestItems;
import com.ibm.manna.pojo.Requests;
import com.ibm.manna.pojo.UserProfile;
import com.ibm.manna.restapi.APIClient;
import com.ibm.manna.restapi.APIInterface;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.function.DoubleToIntFunction;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class ShareCartFragment extends Fragment implements View.OnClickListener {
    View view;
    Button button;
    TextView cartTotal,recyclerviewTotal,address1,address2,address3,address4,locationText,time;
    ImageView location;
    RecyclerView recyclerView;
    CartItemAdapter catalogItemAdapter;

    Cart cart;
    Requests requests;
    ArrayList<Cart> needCartList;
    int PLACE_PICKER_REQUEST = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_share_cart, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.shareitemToolbar);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("on back press 1");
            }
        });

        cart = new Cart();
        Bundle args = getArguments();
        if (args != null) {
            cart = args.getParcelable("request");
        }

        /*if(cart.getCatalogItemsArrayList()!=null){
            ArrayList<RequestItems> requestItemsArrayList = new ArrayList<>();
            for(int i=0;i<cart.getCatalogItemsArrayList().size();i++)
            {
                requestItemsArrayList.add(new RequestItems(cart.getCatalogItemsArrayList().get(i).getCatalog().getDescr(),cart.getCatalogItemsArrayList().get(i).getCatalog().getDescr(),cart.getCatalogItemsArrayList().get(i).getItems(),cart.getCatalogItemsArrayList().get(i).getItems(),Integer.parseInt(String.valueOf(cart.getCatalogItemsArrayList().get(i).getQuantity())),"kg",cart.getCatalogItemsArrayList().get(i).getImage()));
            }
        }*/

        button =view.findViewById(R.id.tapshareButton);
        cartTotal = view.findViewById(R.id.share_cart_quantity);
        recyclerviewTotal = view.findViewById(R.id.share_total);
        address1 = view.findViewById(R.id.address_line1);
        address2 = view.findViewById(R.id.address_line2);
        address3 = view.findViewById(R.id.address_line3);
        address4 = view.findViewById(R.id.address_line4);
        locationText= view.findViewById(R.id.marklocation);
        location = view.findViewById(R.id.location);
        recyclerView = view.findViewById(R.id.share_cart_list);
        time = view.findViewById(R.id.selecttime);

        location.setOnClickListener(this);
        locationText.setOnClickListener(this);
        time.setOnClickListener(this);
        button.setOnClickListener(this);

        address1.setText("1-11-193, Shyamlal Buildings");
        address2.setText("Begumpet");
        address3.setText("Hyderabad");
        address4.setText("Telangana - 500016");



        cartTotal.setText(String.valueOf(cart.getCount()));
        recyclerviewTotal.setText(String.valueOf(cart.getCount()));

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        catalogItemAdapter = new CartItemAdapter(getContext(),cart.getRequests().getReqItems(), new CartItemAdapter.DetailsAdapterListener() {
            @Override
            public void addOnClick(View v, int position) {

                System.out.println("item "+cart.getRequestItemsArrayList().get(position).getItem_descr());
                addItems(cart.getRequestItemsArrayList(), position);
            }

            @Override
            public void subOnClick(View v, int position) {
                removeItems(cart.getRequestItemsArrayList(), position);
            }
        });
        recyclerView.setAdapter(catalogItemAdapter);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.location:
            case R.id.marklocation:
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                //PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.selecttime:
                TimePicker();
                break;
            case R.id.tapshareButton:
                submitRequest();
                //submitDialog();
                break;
        }
    }

    public void addItems(ArrayList<RequestItems> requestItemsArrayList, int position)
    {

        if(cart.getRequests().getReqItems().indexOf(requestItemsArrayList.get(position))>=0)
        {
            int index = cart.getRequests().getReqItems().indexOf(requestItemsArrayList.get(position));
            cart.getRequests().getReqItems().get(index).setItem_qty(requestItemsArrayList.get(position).getItem_qty() + 1);
            cart.setCount(cart.getCount()+1);

        }
        else
        {
            requestItemsArrayList.get(position).setItem_qty(requestItemsArrayList.get(position).getItem_qty() +1);
            ArrayList<RequestItems> tmp = cart.getRequests().getReqItems();
            tmp.add(requestItemsArrayList.get(position));
            cart.getRequests().setReqItems(tmp);
            cart.setCount(cart.getCount()+1);

        }
        cartTotal.setText(String.valueOf(cart.getCount()));
        recyclerviewTotal.setText(String.valueOf(cart.getCount()));
        catalogItemAdapter.notifyDataSetChanged();
    }

    public void removeItems(ArrayList<RequestItems> requestItemsArrayList , int position)
    {
        if(cart.getRequests().getReqItems().indexOf(requestItemsArrayList.get(position))>=0)
        {
            if(requestItemsArrayList.get(position).getItem_qty()>0) {
                int index = cart.getRequests().getReqItems().indexOf(requestItemsArrayList.get(position));
                cart.getRequests().getReqItems().get(index).setItem_qty(cart.getRequests().getReqItems().get(index).getItem_qty()-1);
                if (cart.getCount() > 0)
                    cart.setCount(cart.getCount() - 1);
            }
        }
        else
        {
            if(requestItemsArrayList.get(position).getItem_qty()>0) {
                requestItemsArrayList.get(position).setItem_qty(cart.getRequests().getReqItems().get(position).getItem_qty()-1);
                ArrayList<RequestItems> tmp = cart.getRequests().getReqItems();
                tmp.add(requestItemsArrayList.get(position));
                cart.getRequests().setReqItems(tmp);
                if (cart.getCount() > 0)
                    cart.setCount(cart.getCount() - 1);
            }

        }
        cartTotal.setText(String.valueOf(cart.getCount()));
        recyclerviewTotal.setText(String.valueOf(cart.getCount()));
        catalogItemAdapter.notifyDataSetChanged();
    }

    public void TimePicker()
    {
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        if (hourOfDay<12)
                            time.setText(hourOfDay + ":" + minute+" AM");
                        else
                        if (hourOfDay == 12)
                            time.setText(hourOfDay + ":" + minute+" PM");
                        else
                            time.setText((hourOfDay-12) + ":" + minute+" PM");
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = (Place) PlacePicker.getPlace(data, getActivity());
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(getContext(), toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void submitDialog()
    {
        TextView line1,line2, line3;
        Button otherInfo,home;
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.submit_dialog,null, false);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();

        line1 = dialogView.findViewById(R.id.sub_line1);
        line2 = dialogView.findViewById(R.id.sub_line2);
        line3 = dialogView.findViewById(R.id.sub_line3);
        otherInfo = dialogView.findViewById(R.id.button_seeothers);
        home = dialogView.findViewById(R.id.button_home);

        line1.setText(R.string.share_sub_title);
        line2.setText(R.string.share_sub_msg);
        line3.setText(R.string.sub_caption);

        otherInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeFragment homeFragment = new HomeFragment();
                loadFragment(homeFragment);
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public void submitRequest()
    {
        Manna_DB_Handler manna_db_handler = new Manna_DB_Handler(getContext());
        UserProfile userProfile = manna_db_handler.getUserProfileInfo();
        requests = cart.getRequests();
        requests.setMannaID(userProfile.getMannaID());
        requests.setFname(userProfile.getFname());
        requests.setLname(userProfile.getLname());
        requests.setReqType("S");
        Coord coord = new Coord();
        coord.setLongitude(userProfile.getLongitude());
        coord.setLatitude(userProfile.getLatitude());
        requests.setPickupCoord(coord);
/*        requests.setReqCat(cart.getRequestItemsArrayList().get(0).getCat_descr());
        requests.setReqItems(cart.getRequestItemsArrayList());*/

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<CreateRequestResponse> call = apiInterface.createRequest(requests);
        call.enqueue(new Callback<CreateRequestResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<CreateRequestResponse> call, Response<CreateRequestResponse> response) {

                if(response.body()!=null) {
                    CreateRequestResponse requestResponse= response.body();
                    if(requestResponse.getStatusCode()==200)
                        submitDialog();
                    else
                        Toast.makeText(getContext(), "Unable to Process the request", Toast.LENGTH_LONG).show();

                }
                else {
                    Toast.makeText(getContext(), "500 Internal Server Error", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<CreateRequestResponse> call, Throwable t)
            {
                System.out.println("Error Block");
                call.cancel();
            }
        });
    }

    public void loadFragment(Fragment fragment)
    {
        //Fragment fragment = new HomeFragment();
        //Fragment fragment = new NeedsMapFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.homefragment, fragment);
        for(int i=0; i<fragmentManager.getBackStackEntryCount();i++)
            fragmentManager.popBackStack();
        fragmentTransaction.commit();
    }

}
