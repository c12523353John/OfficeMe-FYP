package finalyear.officeme.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import finalyear.officeme.DatabaseHandler.Config;
import finalyear.officeme.DatabaseHandler.RequestHandler;
import finalyear.officeme.R;
import finalyear.officeme.Singletons.AddressSingleton;
import finalyear.officeme.Singletons.DeskTypeSingleton;
import finalyear.officeme.Singletons.ListingsSingleton;
import finalyear.officeme.Singletons.PicturesSingleton;
import finalyear.officeme.Singletons.UserList;
import finalyear.officeme.model.Address;
import finalyear.officeme.model.DeskType;
import finalyear.officeme.model.Listing;
import finalyear.officeme.model.Picture;
import finalyear.officeme.model.User;
import finalyear.officeme.parse.PopulateSingletonLists;

public class SplashActivity extends Activity {

    private static final int SPLASH_DISPLAY_TIME = 60000; /* 2 seconds */
    private ArrayList<User> userList = new ArrayList<>();
    private ArrayList<DeskType> deskTypeList = new ArrayList<>();
    private ArrayList<Listing> listingsList = new ArrayList<>();
    private ArrayList<Picture> picturesList = new ArrayList<>();
    private ArrayList<Address> addressList = new ArrayList<>();


    String ALL_USERS_JSON_STRING, ALL_DESK_TYPE_JSON_STRING, ALL_PICTURES_JSON_STRING, ALL_LISTINGS_JSON_STRING, ALL_ADDRESSES_JSON_STRING;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
//                startActivity(mainIntent);
//
//                SplashActivity.this.finish();
//                overridePendingTransition(R.anim.mainfadein,
//                        R.anim.splashfadeout);
//            }
//        }, SPLASH_DISPLAY_TIME);

//        PopulateSingletonLists ps = new PopulateSingletonLists();
//        ps.asyncPopulate();

        asyncPopulate();

    }

    public void asyncPopulate()  {

        class GetData extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                // your code here
                                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                                startActivity(i);
                            }
                        },
                        1000
                );

            }

            @Override
            protected String doInBackground(Void... params) {
                String complete = "Loading Complete";
                RequestHandler rh = new RequestHandler();
                String allUsersString = rh.sendGetRequest(Config.URL_GET_ALL_USERS);
                String allDeskTypesString = rh.sendGetRequest(Config.URL_GET_ALL_DESK_TYPES);
                String allListingsString = rh.sendGetRequest(Config.URL__GET_ALL_LISTINGS);
                String allAddressesString = rh.sendGetRequest(Config.URL_GET_ALL_ADDRESSES);
                String allImagesString = rh.sendGetRequest(Config.URL_GET_ALL_PICTURES);



                if(allUsersString!=null){
                    ALL_USERS_JSON_STRING = allUsersString;
                    addUsersToList();
                    UserList.getInstance().setUserList(userList);
                    Log.d("UserList Size: ", Integer.toString(UserList.getInstance().getUserList().size()));
                }

                if(allDeskTypesString !=null) {
                    ALL_DESK_TYPE_JSON_STRING = allDeskTypesString;
                    addDeskTypesToList();
                    DeskTypeSingleton.getInstance().setDeskTypes(deskTypeList);
                    Log.d("DeskTypeList Size: ", Integer.toString(DeskTypeSingleton.getInstance().getDeskTypes().size()));
                }


                if(allImagesString !=null) {
                    Log.d("Gets to", "B");
                    ALL_PICTURES_JSON_STRING = allImagesString;
                    Log.d("Gets to", "C");
//                    addPicturesToList();
                    Log.d("Gets to", "D");
                    PicturesSingleton.getInstance().setPictures(picturesList);
                    Log.d("PicturesList Size", Integer.toString(PicturesSingleton.getInstance().getPictures().size()));


                }

                if(allListingsString !=null) {
                    ALL_LISTINGS_JSON_STRING = allListingsString;
                    addListingsToList();
                    ListingsSingleton.getInstance().setListings(listingsList);
                    Log.d("ListingsList Size: ", Integer.toString(ListingsSingleton.getInstance().getListings().size()));
                }

                if(allAddressesString !=null) {
                    ALL_ADDRESSES_JSON_STRING = allAddressesString;
                    addAddressesToList();
                    AddressSingleton.getInstance().setAddresses(addressList);
                    Log.d("AddressList Size: ", Integer.toString(AddressSingleton.getInstance().getAddresses().size()));
                }


                //return userList;
                return complete;
            }
        }
        GetData gd = new GetData();
        gd.execute();
    }



    public void addUsersToList(){
        JSONObject jsonObject = null;

        if(!userList.isEmpty()) {
            userList.clear();
        }
        try {
            jsonObject = new JSONObject(ALL_USERS_JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                int id = Integer.parseInt(jo.getString(Config.TAG_ID));
                String name = jo.getString(Config.TAG_NAME);
                String email = jo.getString(Config.TAG_EMAIL);
                String password = jo.getString(Config.TAG_PASSWORD);
                String phoneNumber = jo.getString(Config.TAG_PHONE_NUMBER);
                User user = new User(id, name, email, password, phoneNumber);
                userList.add(user);
                Log.d("PhoneNum: ", phoneNumber);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void addDeskTypesToList() {
        JSONObject jsonObject = null;

        if(!deskTypeList.isEmpty()) {
            deskTypeList.clear();
        }
        try {
            jsonObject = new JSONObject(ALL_DESK_TYPE_JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_DESK_TYPE_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                int id = Integer.parseInt(jo.getString(Config.TAG_DESK_TYPE_ID));
                String deskType = jo.getString(Config.TAG_DESK_TYPE_DESK_TYPE);
                DeskType dt = new DeskType(id, deskType);
                deskTypeList.add(dt);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addListingsToList() {
        JSONObject jsonObject = null;

        if(!listingsList.isEmpty()) {
            listingsList.clear();
        }
        try {
            jsonObject = new JSONObject(ALL_LISTINGS_JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_LISTING_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                int listingID = Integer.parseInt(jo.getString(Config.TAG_LISTING_ID));
                int userID = Integer.parseInt(jo.getString(Config.TAG_LISTING_USER_ID));
                int deskTypeID = Integer.parseInt(jo.getString(Config.TAG_LISTING_DESK_TYPE_ID));
                int desksAvailable = Integer.parseInt(jo.getString(Config.TAG_LISTING_DESKS_AVAILABLE));
                int listingPrice = Integer.parseInt(jo.getString(Config.TAG_LISTING_PRICE));
                String listingTitle = jo.getString(Config.TAG_LISTING_TITLE);
                String listingDescription = jo.getString(Config.TAG_LISTING_DESCRIPTION);

                Listing ls = new Listing(listingID, userID, deskTypeID, desksAvailable, listingPrice, listingTitle, listingDescription);
                listingsList.add(ls);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addPicturesToList() {
        JSONObject jsonObject = null;

        if(!picturesList.isEmpty()) {
            picturesList.clear();
        }

        try {
            jsonObject = new JSONObject(ALL_PICTURES_JSON_STRING);
//            Log.d("Gets to", "A");
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_PICTURE_ARRAY);
//            Log.d("Gets to", "B");

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                int pictureID = Integer.parseInt(jo.getString(Config.TAG_PICTURE_ID));
                int listingID = Integer.parseInt(jo.getString(Config.TAG_PICTURE_LISTING_ID));

                String listingImagePath = jo.getString(Config.TAG_PICTURE_LISTING_PATH);
                Log.d("listingImgPath", listingImagePath);
                Bitmap bitmap = getBitmapFromURL("http://www.johnrockfinalyearproject.com/" +listingImagePath);
                Log.d("Full Path", "http://www.johnrockfinalyearproject.com/" +listingImagePath);


                Picture picture = new Picture(pictureID, listingID, bitmap);
                picturesList.add(picture);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }
    private void addAddressesToList() {
        JSONObject jsonObject = null;

        if(!addressList.isEmpty()) {
            addressList.clear();
        }

        try {
            jsonObject = new JSONObject(ALL_ADDRESSES_JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ADDRESS_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                int addressID = Integer.parseInt(jo.getString(Config.TAG_ADDRESS_ID));
                int listingID = Integer.parseInt(jo.getString(Config.TAG_ADDRESS_LISTING_ID));
                String street1 = jo.getString(Config.TAG_ADDRESS_STREET1);
                String street2 = jo.getString(Config.TAG_ADDRESS_STREET2);
                String city = jo.getString(Config.TAG_ADDRESS_CITY);
                String county = jo.getString(Config.TAG_ADDRESS_COUNTY);
                String country = jo.getString(Config.TAG_ADDRESS_COUNTRY);

                Address address = new Address(addressID, listingID, street1, street2, city, county, country);
                addressList.add(address);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}
