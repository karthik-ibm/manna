package com.ibm.manna.db_handler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.textclassifier.TextSelection;

import androidx.annotation.Nullable;

import com.ibm.manna.pojo.Address;
import com.ibm.manna.pojo.Catalog;
import com.ibm.manna.pojo.CatalogItems;
import com.ibm.manna.pojo.Coord;
import com.ibm.manna.pojo.RequestDetails;
import com.ibm.manna.pojo.RequestItems;
import com.ibm.manna.pojo.Requests;
import com.ibm.manna.pojo.UserProfile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class Manna_DB_Handler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION=10;
    private static final String DATABASE_NAME="manna.db";
    private static final String USER_PROFILE_TABLE="USER_PROFILE_TABLE";
    private static final String USER_ADDRESS_TABLE="USER_ADDRESS_TABLE";
    private static final String CATALOG_TABLE="CATALOG_TABLE";
    private static final String CATALOG_ITEM_TABLE="CATALOG_ITEM_TABLE";
    private static final String REQUEST_TABLE="REQUEST_TABLE";
    private static final String REQUEST_ITEM_TABLE="REQUEST_ITEM_TABLE";
    private static final String REQUEST_TRANSACTION_TABLE="REQUEST_TRANSACTION_TABLE";
    private static final String REQUEST_TRANSACTION_ITEM_TABLE="REQUEST_TRANSACTION_ITEM_TABLE";



    private Context context;

    public Manna_DB_Handler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("in create method");
        String CREATE_USER_PROFILE_TABLE= "CREATE TABLE "+USER_PROFILE_TABLE+" ("+
                "MANNA_ID TEXT PRIMARY KEY, FIRST_NAME TEXT, LAST_NAME TEXT, " +
                "MOBILE TEXT,DEVICE_ID TEXT, DEVICE_TYPE TEXT,LATITUDE REAL," +
                "LONGITUDE REAL, UID TEXT, IMAGEURL TEXT, RATING REAL)";

        String CREATE_USER_ADDRESS_TABLE= "CREATE TABLE "+USER_ADDRESS_TABLE+" ("+
                "MANNA_ID TEXT PRIMARY KEY, LINE1 TEXT, LINE2 TEXT, " +
                "CITY TEXT,STATE TEXT, PIN TEXT)";

        String CREATE_CATALOG_TABLE= "CREATE TABLE "+CATALOG_TABLE+" ("+
                "CATEGORY TEXT PRIMARY KEY, REQ_CAT TEXT, CAT_DESCR TEXT, " +
                "IMAGE TEXT)";

        String CREATE_CATALOG_ITEM_TABLE= "CREATE TABLE "+CATALOG_ITEM_TABLE+" ("+
                "CATEGORY TEXT, ITEM_ID TEXT, ITEM_DESCR TEXT, " +
                "IMAGE TEXT, METRIC TEXT, PRIMARY KEY(CATEGORY,ITEM_ID))";

        String CREATE_REQUEST_TABLE = "CREATE TABLE "+REQUEST_TABLE+" ("+
                "REQ_ID TEXT,REQUEST_TYPE TEXT, MANNA_ID TEXT,MOBILE TEXT, FNAME TEXT, LNAME TEXT, REQ_TYPE TEXT, REQ_DT TEXT,REQ_CAT TEXT,"+
                "REQ_TM TEXT,REQEXP TEXT, LINE1 TEXT, LINE2 TEXT, CITY TEXT, STATE TEXT, PIN TEXT,LATITUDE REAL,LONGITUDE REAL ,PRIMARY KEY(REQ_ID, REQUEST_TYPE))";
        String CREATE_REQUEST_ITEM_TABLE = "CREATE TABLE "+REQUEST_ITEM_TABLE+" ("+
                "REQ_ID TEXT, REQUEST_TYPE TEXT, CATEGORY TEXT, CAT_DESCR TEXT, ITEM_ID TEXT,ITEM_DESCR TEXT, ITEM_QTY  REAL, METRIC TEXT, IMAGE TEXT, AVAIL_QTY REAL, PRIMARY KEY(REQ_ID,REQUEST_TYPE,ITEM_ID))";

        String CREATE_REQUEST_TRANSACTION_TABLE = "CREATE TABLE "+REQUEST_TRANSACTION_TABLE+" ("+
                "REQ_PARENT_ID TEXT,REQUEST_TYPE TEXT, REQ_ID TEXT, TRANSACTION_TYPE  TEXT, REQ_TYPE TEXT, REQ_DT TEXT, IMAGE TEXT, MANNA_ID TEXT,FNAME TEXT,LNAME TEXT, PHONE TEXT,"+
                "REQ_STATUS TEXT, REQ_ACPT_RJCT TEXT, LINE1 TEXT, LINE2 TEXT, CITY TEXT, STATE TEXT, PIN TEXT, LATITUDE REAL, LONGITUDE REAL, PRIMARY KEY(REQ_PARENT_ID,REQUEST_TYPE,REQ_ID,TRANSACTION_TYPE))";

        String CREATE_REQUEST_TRANSACTION_ITEM_TABLE = "CREATE TABLE "+REQUEST_TRANSACTION_ITEM_TABLE+" ("+
                "REQ_PARENT_ID TEXT,REQUEST_TYPE TEXT,REQ_ID TEXT,TRANSACTION_TYPE  TEXT, CATEGORY TEXT, CAT_DESCR TEXT, ITEM_ID TEXT,ITEM_DESCR TEXT, ITEM_QTY  REAL, METRIC TEXT, IMAGE TEXT, AVAIL_QTY REAL,PRIMARY KEY(REQ_PARENT_ID,REQUEST_TYPE,REQ_ID,TRANSACTION_TYPE,ITEM_ID))";


        db.execSQL(CREATE_USER_PROFILE_TABLE);
        db.execSQL(CREATE_USER_ADDRESS_TABLE);
        db.execSQL(CREATE_CATALOG_TABLE);
        db.execSQL(CREATE_CATALOG_ITEM_TABLE);

        db.execSQL(CREATE_REQUEST_TABLE);
        db.execSQL(CREATE_REQUEST_ITEM_TABLE);
        db.execSQL(CREATE_REQUEST_TRANSACTION_TABLE);
        db.execSQL(CREATE_REQUEST_TRANSACTION_ITEM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+USER_PROFILE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+USER_ADDRESS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+CATALOG_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+CATALOG_ITEM_TABLE);

        db.execSQL("DROP TABLE IF EXISTS "+REQUEST_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+REQUEST_ITEM_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+REQUEST_TRANSACTION_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+REQUEST_TRANSACTION_ITEM_TABLE);
    }


    public void deleteTable()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("DELETE FROM "+USER_PROFILE_TABLE);
        db.execSQL("DELETE FROM "+USER_ADDRESS_TABLE);
        db.execSQL("DELETE FROM "+CATALOG_TABLE);
        db.execSQL("DELETE FROM "+CATALOG_ITEM_TABLE);

        db.execSQL("DELETE FROM "+REQUEST_TABLE);
        db.execSQL("DELETE FROM "+REQUEST_ITEM_TABLE);
        db.execSQL("DELETE FROM "+REQUEST_TRANSACTION_TABLE);
        db.execSQL("DELETE FROM "+REQUEST_TRANSACTION_ITEM_TABLE);
        db.close();
    }


    public void Add_User_Profile(UserProfile userProfile){
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues contentValues=new ContentValues();
        System.out.println("IN DB Handler"+userProfile.getMannaID());
        contentValues.put("MANNA_ID",userProfile.getMannaID());
        if(userProfile.getFname()!=null)
            contentValues.put("FIRST_NAME",userProfile.getFname());
        else
            contentValues.put("FIRST_NAME"," ");
        if(userProfile.getLname()!=null)
            contentValues.put("LAST_NAME",userProfile.getFname());
        else
            contentValues.put("LAST_NAME"," ");
        contentValues.put("MOBILE",userProfile.getMobile());
        if(userProfile.getDeviceID()!=null)
            contentValues.put("DEVICE_ID",userProfile.getDeviceID());
        else
            contentValues.put("DEVICE_ID"," ");
        if(userProfile.getType()!=null)
        contentValues.put("DEVICE_TYPE",userProfile.getType());
        else
            contentValues.put("DEVICE_TYPE"," ");
        contentValues.put("LATITUDE",userProfile.getLatitude());
        contentValues.put("LONGITUDE",userProfile.getLongitude());
        if(userProfile.getUid()!=null)
            contentValues.put("UID",userProfile.getUid());
        else
            contentValues.put("UID"," ");
        if(userProfile.getImageurl()!=null)
            contentValues.put("IMAGEURL",userProfile.getImageurl());
        else
            contentValues.put("IMAGEURL"," ");
            contentValues.put("RATING",userProfile.getRating());

        try {
            db.insert(USER_PROFILE_TABLE,null,contentValues);
        }catch (SQLiteException e){
            e.printStackTrace();
        }


        Add_User_Address(userProfile);
        db.close();

    }

    public void Add_User_Address(UserProfile userProfile){
        SQLiteDatabase db=this.getWritableDatabase();

        if(userProfile.getAddress()!=null) {
            ContentValues contentValues = new ContentValues();
            System.out.println("IN DB Handler" + userProfile.getMannaID());
            contentValues.put("MANNA_ID", userProfile.getMannaID());
            contentValues.put("LINE1", userProfile.getAddress().getLine1());
            contentValues.put("LINE2", userProfile.getAddress().getLine2());
            contentValues.put("CITY", userProfile.getAddress().getCity());
            contentValues.put("STATE", userProfile.getAddress().getState());
            contentValues.put("PIN", userProfile.getAddress().getPin());

            try {
                db.insert(USER_ADDRESS_TABLE, null, contentValues);
            } catch (SQLiteException e) {
                e.printStackTrace();
            }

        }
        db.close();
    }

    public UserProfile getUserProfileInfo(){
        String selectQuery = "SELECT  * FROM " + USER_PROFILE_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        UserProfile userProfile = new UserProfile();
        Cursor cursor = db.rawQuery(selectQuery,null,null);
        if(cursor.moveToFirst()){
            do {

                userProfile.setMannaID(cursor.getString(cursor.getColumnIndex("MANNA_ID")));
                userProfile.setFname(cursor.getString(cursor.getColumnIndex("FIRST_NAME")));
                userProfile.setLname(cursor.getString(cursor.getColumnIndex("LAST_NAME")));
                userProfile.setMobile(cursor.getString(cursor.getColumnIndex("MOBILE")));
                userProfile.setDeviceID(cursor.getString(cursor.getColumnIndex("DEVICE_ID")));
                userProfile.setType(cursor.getString(cursor.getColumnIndex("DEVICE_TYPE")));
                userProfile.setLatitude(cursor.getDouble(cursor.getColumnIndex("LATITUDE")));
                userProfile.setLongitude(cursor.getDouble(cursor.getColumnIndex("LONGITUDE")));
                userProfile.setUid(cursor.getString(cursor.getColumnIndex("UID")));
                userProfile.setImageurl(cursor.getString(cursor.getColumnIndex("IMAGEURL")));
                userProfile.setRating(cursor.getDouble(cursor.getColumnIndex("RATING")));
                // userProfile.setLOGIN_DTTM_STAMP(cursor.getString(cursor.getColumnIndex("LOGIN_DTTM_STAMP")));
            } while (cursor.moveToNext());
        }
        cursor.close();

        selectQuery = "SELECT  * FROM " + USER_ADDRESS_TABLE;
        cursor = db.rawQuery(selectQuery,null,null);
        Address address = new Address();
        if(cursor.moveToFirst()){
            do {
                address.setLine1(cursor.getString(cursor.getColumnIndex("LINE1")));
                address.setLine2(cursor.getString(cursor.getColumnIndex("LINE2")));
                address.setCity(cursor.getString(cursor.getColumnIndex("CITY")));
                address.setState(cursor.getString(cursor.getColumnIndex("STATE")));
                address.setPin(cursor.getString(cursor.getColumnIndex("PIN")));
            } while (cursor.moveToNext());
        }

        userProfile.setAddress(address);
        //db.close();
        return userProfile;
    }

    public void Add_Catalog_Table(ArrayList<Catalog> catalogArrayList){


        for(int i=0;i<catalogArrayList.size();i++) {
            SQLiteDatabase db=this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            System.out.println("IN DB Handler" + catalogArrayList.get(i).getCat_descr());
            contentValues.put("CATEGORY", catalogArrayList.get(i).getCategory());
            contentValues.put("REQ_CAT", catalogArrayList.get(i).getReqCat());
            contentValues.put("CAT_DESCR", catalogArrayList.get(i).getCat_descr());
            contentValues.put("IMAGE", catalogArrayList.get(i).getImage());
            try {
                db.insert(CATALOG_TABLE, null, contentValues);
                Add_Catalog_Item_Table(catalogArrayList.get(i).getItem(),catalogArrayList.get(i).getCategory());
            } catch (SQLiteException e) {
                e.printStackTrace();
            }
            db.close();
        }


    }

    public void Add_Catalog_Item_Table(ArrayList<CatalogItems> catalogItemsArrayList,String catalog_id){


        for(int i=0;i<catalogItemsArrayList.size();i++) {
            SQLiteDatabase db=this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            // System.out.println("IN DB Handler" + catalogItemsArrayList.get(i).getCat_descr());
            contentValues.put("CATEGORY", catalog_id);
            contentValues.put("ITEM_ID", catalogItemsArrayList.get(i).getItem_id());
            contentValues.put("ITEM_DESCR", catalogItemsArrayList.get(i).getItem_descr());
            contentValues.put("IMAGE", catalogItemsArrayList.get(i).getImage());
            contentValues.put("METRIC", catalogItemsArrayList.get(i).getMetric());
            try {
                db.insert(CATALOG_ITEM_TABLE, null, contentValues);
            } catch (SQLiteException e) {
                e.printStackTrace();
            }
            db.close();
        }

    }

    public ArrayList<Catalog> getCatalog()
    {
        ArrayList<Catalog> catalogArrayList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + CATALOG_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null,null);
        if(cursor.moveToFirst()){
            do {
                Catalog catalog = new Catalog();
                catalog.setCategory(cursor.getString(cursor.getColumnIndex("CATEGORY")));
                catalog.setCat_descr(cursor.getString(cursor.getColumnIndex("CAT_DESCR")));
                catalog.setReqCat(cursor.getString(cursor.getColumnIndex("REQ_CAT")));
                catalog.setImage(cursor.getString(cursor.getColumnIndex("IMAGE")));
                catalog.setItem(getCatalogItems(catalog.getCategory()));
                catalogArrayList.add(catalog);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return catalogArrayList;
    }



    public ArrayList<CatalogItems> getCatalogItems(String category)
    {
        ArrayList<CatalogItems> catalogItemsArrayList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + CATALOG_ITEM_TABLE +" WHERE CATEGORY = '"+category+"'";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery,null,null);
        if(cursor.moveToFirst()){
            do {
                CatalogItems catalogItems  = new CatalogItems();
                catalogItems.setItem_descr(cursor.getString(cursor.getColumnIndex("ITEM_DESCR")));
                catalogItems.setItem_id(cursor.getString(cursor.getColumnIndex("ITEM_ID")));
                catalogItems.setMetric(cursor.getString(cursor.getColumnIndex("METRIC")));
                catalogItems.setImage(cursor.getString(cursor.getColumnIndex("IMAGE")));
                catalogItemsArrayList.add(catalogItems);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return catalogItemsArrayList;
    }

    public void deleteRequestTable()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("DELETE FROM "+REQUEST_TABLE);
        db.execSQL("DELETE FROM "+REQUEST_ITEM_TABLE);
        db.execSQL("DELETE FROM "+REQUEST_TRANSACTION_TABLE);
        db.execSQL("DELETE FROM "+REQUEST_TRANSACTION_ITEM_TABLE);
    }
    public void addRequests(ArrayList<Requests> requestsArrayList, String requestType)
    {


        for(int i=0;i<requestsArrayList.size();i++) {
            SQLiteDatabase db=this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            // System.out.println("IN DB Handler" + catalogItemsArrayList.get(i).getCat_descr());
            contentValues.put("REQ_ID",requestsArrayList.get(i).getReqID() );
            contentValues.put("REQUEST_TYPE",requestType );
            contentValues.put("MANNA_ID",requestsArrayList.get(i).getMannaID() );
            contentValues.put("MOBILE",requestsArrayList.get(i).getMobile() );
            contentValues.put("FNAME",requestsArrayList.get(i).getFname() );
            contentValues.put("LNAME",requestsArrayList.get(i).getLname() );
            contentValues.put("REQ_TYPE",requestsArrayList.get(i).getReqType() );
            contentValues.put("REQ_DT",requestsArrayList.get(i).getReqDt() );
            contentValues.put("REQ_CAT",requestsArrayList.get(i).getReqCat() );
            contentValues.put("REQ_TM",requestsArrayList.get(i).getReqtm() );
            contentValues.put("REQEXP",requestsArrayList.get(i).getReqExp() );
            contentValues.put("LINE1",requestsArrayList.get(i).getPickupAddress().getLine1() );
            contentValues.put("LINE2",requestsArrayList.get(i).getPickupAddress().getLine2() );
            contentValues.put("CITY",requestsArrayList.get(i).getPickupAddress().getCity());
            contentValues.put("STATE",requestsArrayList.get(i).getPickupAddress().getState() );
            contentValues.put("PIN",requestsArrayList.get(i).getPickupAddress().getPin() );
            contentValues.put("LATITUDE",requestsArrayList.get(i).getPickupCoord().getLatitude() );
            contentValues.put("LONGITUDE",requestsArrayList.get(i).getPickupCoord().getLongitude());
            try {
                db.insert(REQUEST_TABLE, null, contentValues);
                addRequestsItems(requestsArrayList.get(i).getReqItems(),requestsArrayList.get(i).getReqID());
                if(requestsArrayList.get(i).getOutgoingReqs()!=null)
                    addRequestsTransactions(requestsArrayList.get(i).getOutgoingReqs(),requestsArrayList.get(i).getReqID(),"outgoing");
                if(requestsArrayList.get(i).getIncomingReqs()!=null)
                    addRequestsTransactions(requestsArrayList.get(i).getIncomingReqs(),requestsArrayList.get(i).getReqID(),"incoming");
            } catch (SQLiteException e) {
                e.printStackTrace();
            }
            db.close();
        }
        //
    }

    public void addRequestsItems(ArrayList<RequestItems> requestsItemsArrayList, String requestID)
    {


        for(int i=0;i<requestsItemsArrayList.size();i++) {
            SQLiteDatabase db=this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            // System.out.println("IN DB Handler" + catalogItemsArrayList.get(i).getCat_descr());
            contentValues.put("REQ_ID",requestID);
            contentValues.put("CATEGORY",requestsItemsArrayList.get(i).getCategory());
            contentValues.put("CAT_DESCR",requestsItemsArrayList.get(i).getCat_descr());
            contentValues.put("ITEM_ID",requestsItemsArrayList.get(i).getItem_id());
            contentValues.put("ITEM_DESCR",requestsItemsArrayList.get(i).getItem_descr());
            contentValues.put("ITEM_QTY ",requestsItemsArrayList.get(i).getItem_qty());
            contentValues.put("METRIC",requestsItemsArrayList.get(i).getMetric());
            contentValues.put("IMAGE",requestsItemsArrayList.get(i).getImage());
            contentValues.put("AVAIL_QTY",requestsItemsArrayList.get(i).getAvail_qty());
            try {
                db.insert(REQUEST_ITEM_TABLE, null, contentValues);
            } catch (SQLiteException e) {
                e.printStackTrace();
            }
            db.close();
        }
       // db.close();
    }

    public void addRequestsTransactions(ArrayList<RequestDetails> requestDetailsArrayList, String requestID, String requestType)
    {
        if(requestDetailsArrayList!=null) {
            for (int i = 0; i < requestDetailsArrayList.size(); i++) {
                SQLiteDatabase db=this.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                // System.out.println("IN DB Handler" + catalogItemsArrayList.get(i).getCat_descr());
                contentValues.put("REQ_PARENT_ID", requestID);
                contentValues.put("REQ_ID", requestDetailsArrayList.get(i).getReqID());
                contentValues.put("TRANSACTION_TYPE ", requestType);
                contentValues.put("REQ_TYPE", requestDetailsArrayList.get(i).getReqType());
                contentValues.put("REQ_DT", requestDetailsArrayList.get(i).getReqDt());
                contentValues.put("IMAGE", requestDetailsArrayList.get(i).getImage());
                contentValues.put("MANNA_ID", requestDetailsArrayList.get(i).getMannaID());
                contentValues.put("FNAME", requestDetailsArrayList.get(i).getFirstname());
                contentValues.put("LNAME", requestDetailsArrayList.get(i).getLastname());
                contentValues.put("PHONE", requestDetailsArrayList.get(i).getPhone());
                contentValues.put("REQ_STATUS", requestDetailsArrayList.get(i).getReqStatus());
                contentValues.put("REQ_ACPT_RJCT", requestDetailsArrayList.get(i).getReqAcpt_Rjct());
                contentValues.put("LINE1", requestDetailsArrayList.get(i).getPickup_address().getLine1());
                contentValues.put("LINE2", requestDetailsArrayList.get(i).getPickup_address().getLine2());
                contentValues.put("CITY", requestDetailsArrayList.get(i).getPickup_address().getCity());
                contentValues.put("STATE", requestDetailsArrayList.get(i).getPickup_address().getState());
                contentValues.put("PIN", requestDetailsArrayList.get(i).getPickup_address().getPin());
                contentValues.put("LATITUDE", requestDetailsArrayList.get(i).getPickup_coords().getLatitude());
                contentValues.put("LONGITUDE", requestDetailsArrayList.get(i).getPickup_coords().getLongitude());
                try {
                    db.insert(REQUEST_TRANSACTION_TABLE, null, contentValues);
                    addRequestsTransItems(requestDetailsArrayList.get(i).getReqItems(), requestDetailsArrayList.get(i).getReqID(), requestType, requestID);
                } catch (SQLiteException e) {
                    e.printStackTrace();
                }
                db.close();
            }

        }
        //
    }

    public void addRequestsTransItems(ArrayList<RequestItems> requestsTransItemsArrayList, String requestID,String reqTypes,String req_parent_ID )
    {


        for(int i=0;i<requestsTransItemsArrayList.size();i++) {
            SQLiteDatabase db=this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            // System.out.println("IN DB Handler" + catalogItemsArrayList.get(i).getCat_descr());
            contentValues.put("REQ_PARENT_ID", requestID);
            contentValues.put("REQ_ID",requestID);
            contentValues.put("TRANSACTION_TYPE",reqTypes);
            contentValues.put("CATEGORY",requestsTransItemsArrayList.get(i).getCategory());
            contentValues.put("CAT_DESCR",requestsTransItemsArrayList.get(i).getCat_descr());
            contentValues.put("ITEM_ID",requestsTransItemsArrayList.get(i).getItem_id());
            contentValues.put("ITEM_DESCR",requestsTransItemsArrayList.get(i).getItem_descr());
            contentValues.put("ITEM_QTY ",requestsTransItemsArrayList.get(i).getItem_qty());
            contentValues.put("METRIC",requestsTransItemsArrayList.get(i).getMetric());
            contentValues.put("IMAGE",requestsTransItemsArrayList.get(i).getImage());
            contentValues.put("AVAIL_QTY",requestsTransItemsArrayList.get(i).getAvail_qty());
            try {
                db.insert(REQUEST_TRANSACTION_ITEM_TABLE, null, contentValues);
            } catch (SQLiteException e) {
                e.printStackTrace();
            }
            db.close();
        }
        //db.close();
    }

    public ArrayList<Requests> getAllRequests(String requestType)
    {
        String selectQuery = "SELECT  * FROM " + REQUEST_TABLE+" WHERE REQUEST_TYPE='"+requestType+"'";
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Requests> requestsArrayList = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectQuery,null,null);
        if(cursor.moveToFirst()){
            do {
                Requests requests = new Requests();
                requests.setMannaID(cursor.getString(cursor.getColumnIndex("MANNA_ID")));
                requests.setFname(cursor.getString(cursor.getColumnIndex("FNAME")));
                requests.setLname(cursor.getString(cursor.getColumnIndex("LNAME")));
                requests.setMobile(cursor.getString(cursor.getColumnIndex("MOBILE")));
                requests.setReqID(cursor.getString(cursor.getColumnIndex("REQ_ID")));
                requests.setReqType(cursor.getString(cursor.getColumnIndex("REQ_TYPE")));
                requests.setReqCat(cursor.getString(cursor.getColumnIndex("REQ_CAT")));
                requests.setReqDt(cursor.getString(cursor.getColumnIndex("REQ_DT")));
                requests.setReqtm(cursor.getString(cursor.getColumnIndex("REQ_TM")));
                requests.setReqExp(cursor.getString(cursor.getColumnIndex("REQEXP")));

                Address address = new Address();
                address.setLine1(cursor.getString(cursor.getColumnIndex("LINE1")));
                address.setLine2(cursor.getString(cursor.getColumnIndex("LINE2")));
                address.setCity(cursor.getString(cursor.getColumnIndex("CITY")));
                address.setState(cursor.getString(cursor.getColumnIndex("STATE")));
                address.setPin(cursor.getString(cursor.getColumnIndex("PIN")));
                requests.setPickupAddress(address);

                Coord coords = new Coord();
                coords.setLatitude(cursor.getDouble(cursor.getColumnIndex("LATITUDE")));
                coords.setLongitude(cursor.getDouble(cursor.getColumnIndex("LONGITUDE")));
                requests.setPickupCoord(coords);
                requests.setReqItems(getAllRequestItems(requests.getReqID()));
                requests.setOutgoingReqs(getRequestsTransactions(requests.getReqID(),"outgoing"));
                requests.setIncomingReqs(getRequestsTransactions(requests.getReqID(),"incoming"));
                requestsArrayList.add(requests);

            } while (cursor.moveToNext());
        }
        cursor.close();

        return requestsArrayList;
    }

    public ArrayList<RequestItems> getAllRequestItems(String reqId)
    {
        String selectQuery = "SELECT  * FROM " + REQUEST_ITEM_TABLE+" WHERE REQ_ID='"+reqId+"'";
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<RequestItems> requestItemsArrayList = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectQuery,null,null);
        if(cursor.moveToFirst()){
            do {
                RequestItems requestItems = new RequestItems();
                requestItems.setCategory(cursor.getString(cursor.getColumnIndex("CATEGORY")));
                requestItems.setCat_descr(cursor.getString(cursor.getColumnIndex("CAT_DESCR")));
                requestItems.setItem_id(cursor.getString(cursor.getColumnIndex("ITEM_ID")));
                requestItems.setItem_descr(cursor.getString(cursor.getColumnIndex("ITEM_DESCR")));
                requestItems.setItem_qty(cursor.getInt(cursor.getColumnIndex("ITEM_QTY")));
                requestItems.setMetric(cursor.getString(cursor.getColumnIndex("METRIC")));
                requestItems.setImage(cursor.getString(cursor.getColumnIndex("IMAGE")));
                requestItems.setAvail_qty(cursor.getInt(cursor.getColumnIndex("AVAIL_QTY")));
                requestItemsArrayList.add(requestItems);

            } while (cursor.moveToNext());
        }
        cursor.close();

        return requestItemsArrayList;
    }

    public ArrayList<RequestDetails> getRequestsTransactions(String reqID,String reqTransCat)
    {
        String selectQuery = "SELECT  * FROM " + REQUEST_TRANSACTION_TABLE +" WHERE REQ_PARENT_ID= '"+reqID+"' AND TRANSACTION_TYPE='"+reqTransCat+"'";
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<RequestDetails> requestDetailsArrayList = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectQuery,null,null);
        if(cursor.moveToFirst()){
            do {
                RequestDetails requestDetails = new RequestDetails();
                requestDetails.setMannaID(cursor.getString(cursor.getColumnIndex("MANNA_ID")));
                requestDetails.setFirstname(cursor.getString(cursor.getColumnIndex("FNAME")));
                requestDetails.setLastname(cursor.getString(cursor.getColumnIndex("LNAME")));
                requestDetails.setPhone(cursor.getString(cursor.getColumnIndex("PHONE")));
                requestDetails.setReqID(cursor.getString(cursor.getColumnIndex("REQ_ID")));
                requestDetails.setReqType(cursor.getString(cursor.getColumnIndex("REQ_TYPE")));
                requestDetails.setReqDt(cursor.getString(cursor.getColumnIndex("REQ_DT")));
                requestDetails.setImage(cursor.getString(cursor.getColumnIndex("IMAGE")));
                requestDetails.setReqStatus(cursor.getString(cursor.getColumnIndex("REQ_STATUS")));
                requestDetails.setReqAcpt_Rjct(cursor.getString(cursor.getColumnIndex("REQ_ACPT_RJCT")));

                Address address = new Address();
                address.setLine1(cursor.getString(cursor.getColumnIndex("LINE1")));
                address.setLine2(cursor.getString(cursor.getColumnIndex("LINE2")));
                address.setCity(cursor.getString(cursor.getColumnIndex("CITY")));
                address.setState(cursor.getString(cursor.getColumnIndex("STATE")));
                address.setPin(cursor.getString(cursor.getColumnIndex("PIN")));
                requestDetails.setPickup_address(address);

                Coord coords = new Coord();
                coords.setLatitude(cursor.getDouble(cursor.getColumnIndex("LATITUDE")));
                coords.setLongitude(cursor.getDouble(cursor.getColumnIndex("LONGITUDE")));
                requestDetails.setPickup_coords(coords);
                requestDetails.setReqItems(getAllTransRequestItems(requestDetails.getReqID()));
                requestDetailsArrayList.add(requestDetails);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return requestDetailsArrayList;
    }

    public ArrayList<RequestItems> getAllTransRequestItems(String reqId)
    {
        String selectQuery = "SELECT  * FROM " + REQUEST_TRANSACTION_ITEM_TABLE+" WHERE REQ_ID='"+reqId+"'";
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<RequestItems> requestItemsArrayList = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectQuery,null,null);
        if(cursor.moveToFirst()){
            do {
                RequestItems requestItems = new RequestItems();
                requestItems.setCategory(cursor.getString(cursor.getColumnIndex("CATEGORY")));
                requestItems.setCat_descr(cursor.getString(cursor.getColumnIndex("CAT_DESCR")));
                requestItems.setItem_id(cursor.getString(cursor.getColumnIndex("ITEM_ID")));
                requestItems.setItem_descr(cursor.getString(cursor.getColumnIndex("ITEM_DESCR")));
                requestItems.setItem_qty(cursor.getInt(cursor.getColumnIndex("ITEM_QTY")));
                requestItems.setMetric(cursor.getString(cursor.getColumnIndex("METRIC")));
                requestItems.setImage(cursor.getString(cursor.getColumnIndex("IMAGE")));
                requestItems.setAvail_qty(cursor.getInt(cursor.getColumnIndex("AVAIL_QTY")));
                requestItemsArrayList.add(requestItems);

            } while (cursor.moveToNext());
        }
        cursor.close();

        return requestItemsArrayList;
    }


    public void deleteMyRequestTable(String manna_id)
    {
        System.out.println("in the delete block");
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("DELETE FROM "+REQUEST_ITEM_TABLE +" WHERE REQ_ID IN (SELECT DISTINCT R.REQ_ID FROM "+REQUEST_TABLE+" R WHERE R.MANNA_ID ='"+manna_id+"')");
        db.execSQL("DELETE FROM "+REQUEST_TRANSACTION_TABLE +" WHERE REQ_PARENT_ID IN (SELECT DISTINCT R.REQ_ID FROM "+REQUEST_TABLE+" R WHERE R.MANNA_ID ='"+manna_id+"')");
        db.execSQL("DELETE FROM "+REQUEST_TRANSACTION_ITEM_TABLE +" WHERE REQ_PARENT_ID IN (SELECT DISTINCT R.REQ_ID FROM "+REQUEST_TABLE+" R WHERE R.MANNA_ID ='"+manna_id+"')");
        db.execSQL("DELETE FROM "+REQUEST_TABLE +" WHERE MANNA_ID ='"+manna_id+"'");
    }
}

