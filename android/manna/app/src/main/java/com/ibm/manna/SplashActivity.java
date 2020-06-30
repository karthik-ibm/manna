package com.ibm.manna;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Toast;

import com.ibm.manna.db_handler.Manna_DB_Handler;
import com.ibm.manna.pojo.AuthenticationRequest;
import com.ibm.manna.pojo.AuthenticationResponse;
import com.ibm.manna.pojo.Coord;
import com.ibm.manna.pojo.UserProfile;
import com.ibm.manna.restapi.APIClient;
import com.ibm.manna.restapi.APIInterface;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity implements LocationListener {

    APIInterface apiInterface;
    AuthenticationRequest request;
    double latitude, longitude;
    Manna_DB_Handler manna_db_handler;
    LocationManager locationManager;

    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;
    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_WIFI_STATE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        checkPermissions();


    }

    protected void checkPermissions() {
        final List<String> missingPermissions = new ArrayList<String>();
        // check all required dynamic permissions
        for (final String permission : REQUIRED_SDK_PERMISSIONS) {
            final int result = ContextCompat.checkSelfPermission(this, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission);
            }
        }
        if (!missingPermissions.isEmpty()) {
            // request all missing permissions
            final String[] permissions = missingPermissions
                    .toArray(new String[missingPermissions.size()]);
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_ASK_PERMISSIONS);
        } else {
            final int[] grantResults = new int[REQUIRED_SDK_PERMISSIONS.length];
            Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED);
            onRequestPermissionsResult(REQUEST_CODE_ASK_PERMISSIONS, REQUIRED_SDK_PERMISSIONS,
                    grantResults);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                for (int index = permissions.length - 1; index >= 0; --index) {
                    if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                        // exit the app if one permission is not granted
                        Toast.makeText(this, "Required permission '" + permissions[index]
                                + "' not granted, exiting", Toast.LENGTH_LONG).show();
                        finish();
                        return;
                    }
                }
                // all permissions were granted
                initialize();
                break;
        }
    }

    @SuppressLint("MissingPermission")
    public void initialize()
    {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        request = new AuthenticationRequest();

        TelephonyManager telephonyManager;
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        /*@SuppressLint("MissingPermission")
        String deviceId = telephonyManager.getMeid();
*/

        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                // This method will be executed once the timer is over
                manna_db_handler = new Manna_DB_Handler(getApplicationContext());
                UserProfile userProfile = manna_db_handler.getUserProfileInfo();

                if(userProfile.getMannaID()!=null) {
                    if (!userProfile.getMannaID().isEmpty())
                    {
                        request.setDeviceID("09089hjhh");
                        request.setMannaID(userProfile.getMannaID());
                        /*request.setMannaID("0001");
                        request.setMobile("123456");*/
                        request.setMobile(userProfile.getMobile());
                        request.setType("android");
                        request.setLatitude(latitude);
                        request.setLongitude(longitude);
                        checkPreflight(request);


                    }   // checkPreflight(request);
                }
                else
                {
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }, 4000);

    }

    public void checkPreflight(AuthenticationRequest request)
    {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<AuthenticationResponse> call = apiInterface.preflight(request);
        call.enqueue(new Callback<AuthenticationResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<AuthenticationResponse> call, Response<AuthenticationResponse> response) {

                if(response.body()!=null) {

                    AuthenticationResponse authResponse= response.body();
                    if(authResponse.getStatus().equals("USER NOT FOUND"))
                    {
                        Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    }
                    else {
                        manna_db_handler = new Manna_DB_Handler(getApplicationContext());
                        manna_db_handler.deleteTable();
                        manna_db_handler.Add_User_Profile(authResponse.getUser_profile().get(0));
                        manna_db_handler.Add_Catalog_Table(authResponse.getCat_items());
                        System.out.println(authResponse.getAll_requests()+",,,"+authResponse.getMy_request());
                        if (authResponse.getAll_requests() != null)
                            manna_db_handler.addRequests(authResponse.getAll_requests(), "all");
                        if (authResponse.getMy_request() != null)
                            manna_db_handler.addRequests(authResponse.getMy_request(), "my");

                        System.out.println("reponse: " + authResponse.getCat_items().size());

                        Intent main = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(main);
                        finish();
                    }
                }
                else {
                    //Toast.makeText(getApplicationContext(), "500 Internal Server Error", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<AuthenticationResponse> call, Throwable t)
            {
                System.out.println("Error Block");
                call.cancel();
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude=location.getLongitude();
        //System.out.println("lat: "+latitude +",,lng"+longitude);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
