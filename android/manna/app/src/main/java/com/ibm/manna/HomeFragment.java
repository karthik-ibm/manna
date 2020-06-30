package com.ibm.manna;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ibm.manna.actioncenter.ActionCenterFragment;
import com.ibm.manna.db_handler.Manna_DB_Handler;
import com.ibm.manna.need.NeedsMapFragment;
import com.ibm.manna.pojo.AuthenticationResponse;

import com.ibm.manna.pojo.Requests;
import com.ibm.manna.pojo.UserProfile;
import com.ibm.manna.restapi.APIClient;
import com.ibm.manna.restapi.APIInterface;
import com.ibm.manna.share.ShareMapFragment;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements View.OnClickListener, OnMapReadyCallback {
    Button seeOther,seeNeed;
    View view;
    private GoogleMap map;
    MapView mMapView;
    ImageView signout;
    FloatingActionButton fab;
    TextView welcome;
    AuthenticationResponse response;
    ArrayList<Requests> requests;
    UserProfile userProfile;

    Context mContext;
    FragmentManager fragmentManager;
    SupportMapFragment mapFragment;

    APIInterface apiInterface;
    Manna_DB_Handler manna_db_handler;


    private static final int REQUEST_CODE_LOCATION_PERMISSION=1;

    @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();
        /*System.out.println("in on start block"+args);
        if (args != null) {
            response = args.getParcelable("response");
            System.out.println("homefragment: "+response.getStatus());
        }*/
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        mContext = getActivity();
        manna_db_handler = new Manna_DB_Handler(getContext());

        fab = view.findViewById(R.id.fab_notify);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        mContext = getActivity();
        mMapView = (MapView) view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        userProfile = manna_db_handler.getUserProfileInfo();
        welcome = view.findViewById(R.id.welcome);
        if(userProfile.getFname()!=null)
            if(!userProfile.getFname().isEmpty())
            welcome.setText("Welcome "+ userProfile.getFname());

        requests = manna_db_handler.getAllRequests("all");
        System.out.println("print "+requests.size());

        seeOther = view.findViewById(R.id.see_others);
        seeNeed=view.findViewById(R.id.see_need);
        signout = view.findViewById(R.id.signout);
        mMapView.getMapAsync(this);

        seeOther.setOnClickListener(this);
        seeNeed.setOnClickListener(this);
        fab.setOnClickListener(this);
        signout.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.see_others:
                loadFragment(new ShareMapFragment());
                break;
            case R.id.see_need:
                loadFragment(new NeedsMapFragment());
                break;
            case R.id.fab_notify:
                loadFragment(new ActionCenterFragment());
                break;
            case R.id.signout:
        }
    }

    public void loadFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putParcelable("response",response);
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.homefragment, fragment);
        fragmentTransaction.addToBackStack("tag");
        fragmentTransaction.commit();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMyLocationEnabled(true);

        UiSettings mapSettings;
        mapSettings = map.getUiSettings();
        mapSettings.setZoomControlsEnabled(true);

        // For dropping a marker at a point on the Map
        ArrayList<LatLng> markerList = new ArrayList<>();
        LatLng marker;
        //map.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));

        for(int i=0;i<requests.size();i++){
            marker = new LatLng(requests.get(i).getPickupCoord().getLatitude(),requests.get(i).getPickupCoord().getLongitude());
            markerList.add(i,marker);
            // where myresource (without the extension) is the file
            System.out.println("requests.get(i).getReqItems(): "+requests.get(i).getReqItems().size());
            int imageResource = getContext().getResources().getIdentifier("drawable/"+requests.get(i).getReqItems().get(0).getCategory()+"_"+requests.get(i).getReqItems().get(0).getItem_id(),null,getContext().getPackageName());
            map.addMarker(new MarkerOptions().position(marker).
                    icon(BitmapDescriptorFactory.fromBitmap(
                            createCustomMarker(getContext(),imageResource,requests.get(i).getReqItems().get(0).getItem_descr(),requests.get(i).getReqType())))).setTitle("Rice");
        }

            map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    if(requests.size()>0) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(markerList.get(0)); //Taking Point A (First LatLng)
        builder.include(markerList.get(markerList.size()-1)); //Taking Point B (Second LatLng)
        LatLngBounds bounds = builder.build();

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 200);
        map.moveCamera(cu);
                    }
                }
            });

        map.animateCamera(CameraUpdateFactory.zoomTo(16), 2000, null);


        // For zooming automatically to the location of the marker
        /*CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(15).build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));*/
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
    public Bitmap createCustomMarker(Context context, @DrawableRes int resource, String _name, String reqType) {

        View marker = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_map_layout, null);

        ImageView markerImage = (ImageView) marker.findViewById(R.id.map_card_img);
        markerImage.setImageResource(resource);
        TextView txt_name = (TextView)marker.findViewById(R.id.card_text);
        txt_name.setText(_name);
        System.out.println("name "+ _name);
        if (reqType =="S")
            txt_name.setBackgroundColor(getContext().getColor(R.color.buttonBlue));
        else
            txt_name.setBackgroundColor(getContext().getColor(R.color.buttonGreen));

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        marker.setLayoutParams(new ViewGroup.LayoutParams(100, 100));
        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        Bitmap bitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        marker.draw(canvas);

        return bitmap;
    }
    public void logout()
    {
        /*Call<AuthenticationResponse> call = apiInterface.validateOTP(authenticationRequest);
        call.enqueue(new Callback<AuthenticationResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<AuthenticationResponse> call, Response<AuthenticationResponse> response) {
                System.out.println(response.body().getStatus());
                if(response.body()!=null) {
                    System.out.println("in the block");
                    AuthenticationResponse response_schema = response.body();

                }
                else {
                    Toast.makeText(getContext(), "500 Internal Server Error", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<AuthenticationResponse> call, Throwable t)
            {
                *//*txtUser.setText("");
                txtPass.setText("");
                //txtError.setError("Connection Failure");
                txtError.setText("Connection Failure");
                progressBar.setVisibility(View.GONE);
                call.cancel();*//*
            }
        });*/
    }
}
