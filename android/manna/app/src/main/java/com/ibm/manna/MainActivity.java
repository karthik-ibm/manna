package com.ibm.manna;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.ibm.manna.actioncenter.ActionCenterFragment;
import com.ibm.manna.cart.NeedCartFragment;
import com.ibm.manna.pojo.AuthenticationResponse;
import com.ibm.manna.pojo.Catalog;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<Catalog> catalogArrayList = new ArrayList<>();
    Intent intent;
    public AuthenticationResponse response;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // initialize();
       /*Intent intent = getIntent();
        response= intent.getParcelableExtra("response");
        catalogArrayList = intent.getParcelableExtra("catalog");

        //System.out.println("reponse: "+response.getAll_requests().size() + ","+response.getUser_profile().get(0).getMannaID());//+",,"+response.getAll_requests().size());

        System.out.println("response total "+response.getUser_profile().get(0).getMannaID());
        System.out.println("main activity  profile"+response.getStatusCode());
        System.out.println("main activity  profile"+response.getUser_profile().size());
        System.out.println("main activity request"+response.getAll_requests().size() );
        System.out.println("main activity cat " +response.getCat_items().size());

        System.out.println("main activity catalog bundle" + catalogArrayList.size());*/
        //Bundle bundle = new Bundle();
        //bundle.putParcelable("response",response);


/*        shareCart = new Cart();
        needCart = new Cart();*/
        loadHomeFragment();
    }

    public void initialize()
    {
        Intent intent = getIntent();
        response= intent.getParcelableExtra("args");
        System.out.println("reponse: "+response.getStatusCode() + ","+response.getUser_profile().get(0).getMannaID());//+",,"+response.getAll_requests().size());
        Bundle bundle = new Bundle();
        bundle.putParcelable("response",response);
/*        shareCart = new Cart();
        needCart = new Cart();*/
        loadHomeFragment();
    }
    public void loadHomeFragment()
    {
        Fragment fragment = new HomeFragment();
        //Fragment fragment = new ActionCenterFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        //fragment.setArguments(args);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.homefragment, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
