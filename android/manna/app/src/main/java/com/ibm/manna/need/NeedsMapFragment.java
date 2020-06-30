package com.ibm.manna.need;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Location;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ibm.manna.LoginActivity;
import com.ibm.manna.MainActivity;
import com.ibm.manna.R;
import com.ibm.manna.RecyclerTouchListener;
import com.ibm.manna.cart.NeedCartFragment;
import com.ibm.manna.cart.ShareCartFragment;
import com.ibm.manna.db_handler.Manna_DB_Handler;
import com.ibm.manna.layout_manager.TurnLayoutManager;
import com.ibm.manna.pojo.Address;
import com.ibm.manna.pojo.RequestsResponse;
import com.ibm.manna.pojo.Cart;
import com.ibm.manna.pojo.Catalog;
import com.ibm.manna.pojo.Coord;
import com.ibm.manna.pojo.RequestDetails;
import com.ibm.manna.pojo.RequestItems;
import com.ibm.manna.pojo.Requests;
import com.ibm.manna.pojo.UserProfile;
import com.ibm.manna.restapi.APIClient;
import com.ibm.manna.restapi.APIInterface;

import java.util.ArrayList;
import java.util.StringTokenizer;

import fr.rolandl.carousel.Carousel;
import fr.rolandl.carousel.CarouselBaseAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NeedsMapFragment extends Fragment implements View.OnClickListener, OnMapReadyCallback {

    RecyclerView recyclerView;
    ArrayList<Requests> requests, requestByCatelogs;
    ArrayList<Catalog> catalogArrayList;
    RequestsResponse response;
    Cart cart;

    FloatingActionButton need_fab_refresh;
    Button carouselButton;
    TextView carouselText;

    View view;
    MapView mMapView;
    GoogleMap map;
    Manna_DB_Handler manna_db_handler;
    private Carousel carousel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_map_needs, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.needmapToolbar);
        //toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("on back press 1");
            }
        });

        mMapView = (MapView) view.findViewById(R.id.map_needs);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediate

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        need_fab_refresh = view.findViewById(R.id.need_fab_refresh);
        recyclerView = view.findViewById(R.id.recycler_maps_needs);
        carouselText = view.findViewById(R.id.recycler_image_text);
        carouselButton= view.findViewById(R.id.needs_map_share);

        carouselButton.setOnClickListener(this);
        need_fab_refresh.setOnClickListener(this);
        ArrayList<Requests> requests_new = new ArrayList<>();
        Manna_DB_Handler manna_db_handler = new Manna_DB_Handler(getContext());
        requests_new = manna_db_handler.getAllRequests("all");

        //carousel = (Carousel) view.findViewById(R.id.carousel);
        requests = new ArrayList<>();
        for(int i = 0; i< requests_new.size();i++)
        {
            if(requests_new.get(i).getReqType().equals("S"))
                requests.add(requests_new.get(i));
        }
        requestByCatelogs = new ArrayList<>();

        catalogArrayList = new ArrayList<>();
        //catalogArrayList= response.getCat_items();
        catalogArrayList= manna_db_handler.getCatalog();


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        MapCarouselAdapter adapter = new MapCarouselAdapter(getContext(),catalogArrayList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        carouselText.setText(catalogArrayList.get(0).getCat_descr());

        getRequestsByCatelog(0,requests);
        mMapView.getMapAsync(this);

       recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener(){

            @Override
            public void onClick(View view, int position) {
                /*System.out.println("Catalog detail : "+catalogArrayList.get(position).getDescr());
          */      carouselText.setText(catalogArrayList.get(position).getCat_descr());
                getRequestsByCatelog(position,requests);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        return view;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.clear();
        map.setMyLocationEnabled(true);


        UiSettings mapSettings;
        mapSettings = map.getUiSettings();
        mapSettings.setZoomControlsEnabled(true);
        map.setPadding(0   , 0,0   , 500);

        // For dropping a marker at a point on the Map
        ArrayList<LatLng> markerList = new ArrayList<>();
        LatLng marker;
        //map.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));

        for(int i=0;i<requestByCatelogs.size();i++){
            System.out.println("Needs map request"+requestByCatelogs.get(i).getReqItems().get(0).getCategory()+"_"+requestByCatelogs.get(i).getReqItems().get(0).getItem_id());
            marker = new LatLng(requestByCatelogs.get(i).getPickupCoord().getLatitude(),requestByCatelogs.get(i).getPickupCoord().getLongitude());
            markerList.add(i,marker);
            String title = requestByCatelogs.get(i).getReqItems().get(0).getItem_qty()+" "+requestByCatelogs.get(i).getReqItems().get(0).getMetric() +" "+requestByCatelogs.get(i).getReqItems().get(0).getItem_descr();
            //String snippet = "Rq.Dt: "+requestByCatelogs.get(i).getReqDt()+","+requestByCatelogs.get(i).getReqType();
            String snippet = "Rq.Dt: "+requestByCatelogs.get(i).getReqDt()+","+requestByCatelogs.get(i).getReqType()+","+requestByCatelogs.get(i).getReqID()+","+requestByCatelogs.get(i).getReqItems().get(0).getItem_id();
            map.addMarker(new MarkerOptions().position(marker).snippet(snippet).
                    icon(BitmapDescriptorFactory.fromBitmap(
                            createCustomMarker(getContext(),getContext().getResources().getIdentifier("drawable/"+requestByCatelogs.get(i).getReqItems().get(0).getCategory()+"_"+requestByCatelogs.get(i).getReqItems().get(0).getItem_id(),null,getContext().getPackageName()),requestByCatelogs.get(i).getReqItems().get(0).getItem_descr(),requestByCatelogs.get(i).getReqType()))))
                    .setTitle(title);
        }

        if (markerList.size()>0) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();

            builder.include(markerList.get(0)); //Taking Point A (First LatLng)
            builder.include(markerList.get(markerList.size() - 1)); //Taking Point B (Second LatLng)

            LatLngBounds bounds = builder.build();
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 200);
            map.moveCamera(cu);
        }
        MyInfoWindowAdapter infoWindowAdapter = new MyInfoWindowAdapter(getContext());
        map.setInfoWindowAdapter(infoWindowAdapter);
        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

                System.out.println(marker.getTitle());
                Cart cart = new Cart();
                StringTokenizer tokenizer = new StringTokenizer(marker.getSnippet(),",");
                String reqID = tokenizer.nextToken();
                reqID = tokenizer.nextToken();
                reqID = tokenizer.nextToken();
                String reqItem= tokenizer.nextToken();
                ArrayList<RequestItems> items = new ArrayList<>();
                for(int i = 0; i< requests.size();i++)
                {
                    if(requests.get(i).getReqID().equals(reqID))
                    {
                        for(int j=0;j<requests.get(i).getReqItems().size();j++)
                        {
                            if(requests.get(i).getReqItems().get(j).getItem_id().equals(reqItem)) {
                                items.add(requests.get(i).getReqItems().get(j));
                                Requests needReq = new Requests();

                                manna_db_handler = new Manna_DB_Handler(getContext());
                                UserProfile userProfile = manna_db_handler.getUserProfileInfo();
                                System.out.println("name :"+userProfile.getMannaID());
                                needReq.setMannaID(userProfile.getMannaID());
                                if (userProfile.getFname()!=null)
                                    needReq.setFname(userProfile.getFname());
                                if (userProfile.getLname()!=null)
                                    needReq.setLname(userProfile.getLname());
                                needReq.setReqType("N");
                                Coord coord = new Coord();
                                coord.setLongitude(userProfile.getLongitude());
                                coord.setLatitude(userProfile.getLatitude());
                                needReq.setPickupCoord(coord);
                                needReq.setReqItems(items);

                                ArrayList<RequestDetails> requestDetailsArrayList = new ArrayList<>();
                                RequestDetails details = new RequestDetails();
                                details.setReqID(requests.get(i).getReqID());
                                details.setMannaID(requests.get(i).getMannaID());
                                details.setFirstname(requests.get(i).getFname());
                                details.setLastname(requests.get(i).getLname());
                                details.setPhone(requests.get(i).getMobile());
                                details.setReqStatus("A");
                                details.setReqAcpt_Rjct("Y/N");
                                details.setPickup_address(requests.get(i).getPickupAddress());
                                details.setPickup_coords(requests.get(i).getPickupCoord());
                                details.setReqItems(requests.get(i).getReqItems());
                                requestDetailsArrayList.add(details);
                                needReq.setOutgoingReqs(requestDetailsArrayList);
                                cart.setRequests(needReq);
                                cart.getRequests().setReqItems(items);
                                cart.setCount(requests.get(i).getReqItems().get(j).getItem_qty());
                                Bundle bundle = new Bundle();
                                bundle.putParcelable("request",cart);
                                //bundle.putParcelable("response",response);
                                loadCartFragment(bundle);

                            }
                        }
                    }
                }
                /*Bundle bundle = new Bundle();
                bundle.putParcelable("cart",cart);
                bundle.putParcelable("response",response);
                loadCartFragment(bundle);*/
            }
        });
        map.animateCamera(CameraUpdateFactory.zoomTo(16), 2000, null);



        // For zooming automatically to the location of the marker
        /*CameraPosition cameraPosition = new CameraPosition.Builder().target(markerList.get(0)).zoom(16).build();
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

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.needs_map_share:
                loadCatalogFragment();
                break;
            case R.id.need_fab_refresh:
                fetchNearestRequests();
                break;
        }

    }

    public void  fetchNearestRequests()
    {
        Location location = map.getMyLocation();
        System.out.println("need map lat, long: "+location.getLatitude() +" ,,"+location.getLongitude());
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);

        Call<RequestsResponse> call = apiInterface.fetchNearest(80.4565,11.9889);
        //location.getLongitude(),location.getLatitude());
        call.enqueue(new Callback<RequestsResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<RequestsResponse> call, Response<RequestsResponse> response) {

                if(response.body()!=null) {
                    RequestsResponse requestsResponse= response.body();
                    manna_db_handler = new Manna_DB_Handler(getContext());
                    manna_db_handler.deleteRequestTable();
                    manna_db_handler.addRequests(requestsResponse.getNearest_neighbour(),"all");
                    ArrayList<Requests> requests_new = requestsResponse.getNearest_neighbour();
                    requests.clear();
                    for(int i = 0; i< requests_new.size();i++)
                    {
                        if(requests_new.get(i).getReqType().equals("S"))
                            requests.add(requests_new.get(i));
                    }
                    getRequestsByCatelog(0,requests);


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
        mMapView.getMapAsync(this);
    }

    public Bitmap createCustomMarker(Context context, @DrawableRes int resource, String _name, String reqType) {

        View marker = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_map_layout, null);

        ImageView markerImage = (ImageView) marker.findViewById(R.id.map_card_img);
        markerImage.setImageResource(resource);
        TextView txt_name = (TextView)marker.findViewById(R.id.card_text);
        txt_name.setText(_name);
        System.out.println("name "+ _name);
        if (reqType =="N")
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

    public void getRequestsByCatelog(int position,ArrayList<Requests> reqs)
    {
        requestByCatelogs = new ArrayList<>();
        System.out.println("in the needs maps");
        for(int i=0; i<reqs.size();i++)
        {
            ArrayList<RequestItems> reqItems = reqs.get(i).getReqItems();
            ArrayList<RequestItems> requestPointer = new ArrayList<>();
            System.out.println("test"+reqs.get(i).getMannaID());
            for(int j=0; j<reqItems.size();j++)
            {
                System.out.println("in maps"+reqItems.get(j).getCat_descr()+"--"+catalogArrayList.get(position).getCat_descr());
                if (reqItems.get(j).getCat_descr().equals(catalogArrayList.get(position).getCat_descr())) {
                    requestPointer.add(reqItems.get(j));
                    System.out.println(reqItems.get(j).getCat_descr()+"--"+catalogArrayList.get(position).getCat_descr());
                }
            }
            if(requestPointer.size()>0)
            {
                Requests req = new Requests(reqs.get(i).getMannaID(),reqs.get(i).getMobile(),reqs.get(i).getFname(),reqs.get(i).getLname(),reqs.get(i).getReqID(),reqs.get(i).getReqType()
                        ,reqs.get(i).getReqCat(),reqs.get(i).getReqDt(),reqs.get(i).getReqtm(),reqs.get(i).getReqExp(),reqs.get(i).getReqById(),reqs.get(i).getPickupAddress(),reqs.get(i).getPickupCoord(),requestPointer,reqs.get(i).getOutgoingReqs(),reqs.get(i).getIncomingReqs());
                requestByCatelogs.add(req);
            }
        }
        mMapView.getMapAsync(this);
    }

    public void loadCartFragment(Bundle args)
    {
        Fragment fragment = new NeedCartFragment();
        fragment.setArguments(args);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.homefragment, fragment);
        fragmentTransaction.addToBackStack("tag");
        fragmentTransaction.commit();
    }

    public void loadCatalogFragment()
    {
        Fragment fragment = new NeedsFragment();

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.homefragment, fragment);
        fragmentTransaction.addToBackStack("tag");
        fragmentTransaction.commit();
    }
}
