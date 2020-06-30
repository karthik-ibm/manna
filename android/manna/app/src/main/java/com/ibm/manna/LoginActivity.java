package com.ibm.manna;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.ibm.manna.db_handler.Manna_DB_Handler;
import com.ibm.manna.pojo.AuthenticationRequest;
import com.ibm.manna.pojo.AuthenticationResponse;
import com.ibm.manna.restapi.APIClient;
import com.ibm.manna.restapi.APIInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, LocationListener {

    TextInputLayout phone,otp;
    MaterialButton getOtp,signin;
    TextView login_err_txt;
    LocationManager locationManager;

    double latitude,longitude;
    String deviceId;

    AuthenticationRequest authenticationRequest;
    APIInterface apiInterface;
    Manna_DB_Handler manna_db_handler;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phone = findViewById(R.id.phone);
        otp=findViewById(R.id.otp);
        getOtp = findViewById(R.id.getotp);
        signin= findViewById(R.id.signin);
        login_err_txt = findViewById(R.id.login_err_txt);

        getOtp.setOnClickListener(this);
        signin.setOnClickListener(this);

        apiInterface = APIClient.getClient().create(APIInterface.class);
        TelephonyManager telephonyManager;
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        /*getting DeviceID*/
        if (Build.VERSION.SDK_INT >= 26) {
            /*if (telephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA) {
                deviceId = telephonyManager.getMeid();
            } else if (telephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_GSM) {
                deviceId = telephonyManager.getImei();
            } else {
                deviceId = ""; // default!!!
            }*/
        } else {
            deviceId = telephonyManager.getDeviceId();
        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

    }

    @Override
    public void onClick(View v) {
        String OTP,phoneNbr;
        login_err_txt.setVisibility(View.GONE);
        switch (v.getId())
        {
            case R.id.getotp:

                break;
            case R.id.signin:
                phoneNbr = phone.getEditText().getText().toString();
                OTP = otp.getEditText().getText().toString();
                if(phoneNbr.isEmpty() && OTP.isEmpty())
                {
                    login_err_txt.setText("Phone number and OTP are required fields");
                    login_err_txt.setVisibility(View.VISIBLE);
                }
                else if (phoneNbr.isEmpty())
                {
                    login_err_txt.setText("Phone number is a required fields");
                    login_err_txt.setVisibility(View.VISIBLE);
                }
                else if(OTP.isEmpty())
                {
                    login_err_txt.setText("OTP is a required fields");
                    login_err_txt.setVisibility(View.VISIBLE);
                }else{
                    authenticationRequest = new AuthenticationRequest();
                    authenticationRequest.setMobile(phoneNbr);
                    authenticationRequest.setType("android");
                    authenticationRequest.setDeviceID("090898abcd");
                    //authenticationRequest.setLongitude(47.3478);
                    authenticationRequest.setLongitude(longitude);
                    authenticationRequest.setLatitude(latitude);
                    //authenticationRequest.setDeviceID(deviceId);
                    //authenticationRequest.setLatitude(79.9870);
                    checkAuthentication();
                }


              /*  System.out.println("phone: " +phone.getEditText().getText().toString());
                Intent main= new Intent(LoginActivity.this, MainActivity.class);
                startActivity(main);
                finish();*/
                break;
        }

    }

    public void checkAuthentication()
    {
        Call<AuthenticationResponse> call = apiInterface.validateOTP(authenticationRequest);
        call.enqueue(new Callback<AuthenticationResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<AuthenticationResponse> call, Response<AuthenticationResponse> response) {

                if(response.body()!=null) {
                    AuthenticationResponse authResponse= response.body();
                   manna_db_handler = new Manna_DB_Handler(getApplicationContext());
                    manna_db_handler.Add_User_Profile(authResponse.getUser_profile().get(0));
                    manna_db_handler.Add_Catalog_Table(authResponse.getCat_items());
                    if(authResponse.getAll_requests()!=null)
                        manna_db_handler.addRequests(authResponse.getAll_requests(),"all");
                    if(authResponse.getMy_request()!=null)
                        manna_db_handler.addRequests(authResponse.getMy_request(),"my");

                    System.out.println("reponse: "+authResponse.getCat_items().size() );
                    /*for(int i=0; i<authResponse.getCat_items().size(); i++) {

                        System.out.println("Cat items "+i+ " "+ authResponse.getCat_items().get(i).getCat_descr());
                    }*/

                   Intent main= new Intent(LoginActivity.this, MainActivity.class);
                    /*main.putExtra("response",authResponse);
                    main.putExtra("catalog", authResponse.getCat_items());*/
                    startActivity(main);
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(), "500 Internal Server Error", Toast.LENGTH_SHORT).show();
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
