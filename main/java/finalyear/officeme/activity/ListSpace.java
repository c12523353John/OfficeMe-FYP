package finalyear.officeme.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import finalyear.officeme.DatabaseHandler.Config;
import finalyear.officeme.DatabaseHandler.RequestHandler;
import finalyear.officeme.R;
import finalyear.officeme.Singletons.AddressSingleton;
import finalyear.officeme.Singletons.DeskTypeSingleton;
import finalyear.officeme.Singletons.ListingsSingleton;
import finalyear.officeme.Singletons.LoggedIn;
import finalyear.officeme.Singletons.PicturesSingleton;
import finalyear.officeme.Singletons.UserID;
import finalyear.officeme.model.Address;
import finalyear.officeme.model.DeskType;
import finalyear.officeme.model.Listing;
import finalyear.officeme.model.Picture;
import finalyear.officeme.parse.PopulateSingletonLists;

public class ListSpace extends AppCompatActivity {

    EditText etAddListingTitle, etAddListingDescription, etAddListingNoOfDesks, etAddListingPricePerMonth, etListingStreet1, etListingStreet2, etListingCity, etListingCounty, etListingCountry;
    ImageView listSpaceMainImage, listSpaceSmallImg1, listSpaceSmallImg2, listSpaceSmallImg3, listSpaceSmallImg4;
    Bitmap mainImgBitmap, smallImg1Bitmap, smallImg2Bitmap, smallImg3Bitmap, smallImg4Bitmap;
    Spinner addListingSpinner;
    ArrayAdapter<String> deskTypeAdapter;
    ArrayList<DeskType> deskTypes;
    ArrayList<String> spinnerList;
    String ALL_LISTINGS_JSON_STRING, ALL_PICTURES_JSON_STRING, ALL_ADDRESSES_JSON_STRING;
    private ArrayList<Listing> listingsList = new ArrayList<>();
    private ArrayList<Picture> picturesList = new ArrayList<>();
    private ArrayList<Address> addressList = new ArrayList<>();
    String mainImgString, smallImg1String, smallImg2String, smallImg3String, smallImg4String;
    ArrayList<String> images;
    String globalListingID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_space);

        etAddListingTitle = (EditText) findViewById(R.id.etAddListingTitle);
        etAddListingDescription = (EditText) findViewById(R.id.etAddListingDescription);
        etAddListingNoOfDesks = (EditText) findViewById(R.id.etAddListingNoOfDesks);
        etAddListingPricePerMonth = (EditText) findViewById(R.id.etAddListingPricePerMonth);
        etListingStreet1 = (EditText) findViewById(R.id.etAddListingStreet1);
        etListingStreet2 = (EditText) findViewById(R.id.etAddListingStreet2);
        etListingCity = (EditText) findViewById(R.id.etAddListingCity);
        etListingCounty = (EditText) findViewById(R.id.etAddListingCounty);
        etListingCountry = (EditText) findViewById(R.id.etAddListingCountry);
        listSpaceMainImage = (ImageView) findViewById(R.id.listSpaceMainImage);
        listSpaceSmallImg1 = (ImageView) findViewById(R.id.listSpaceSmallImg1);
        listSpaceSmallImg2 = (ImageView) findViewById(R.id.listSpaceSmallImg2);
        listSpaceSmallImg3 = (ImageView) findViewById(R.id.listSpaceSmallImg3);
        listSpaceSmallImg4 = (ImageView) findViewById(R.id.listSpaceSmallImg4);

        final Button addBtn = (Button) findViewById(R.id.btnAddListing);

        //Adding to Spinner
        deskTypes = new ArrayList<>();
        spinnerList = new ArrayList<>();
        for(int i=0; i<DeskTypeSingleton.getInstance().getDeskTypes().size(); i++) {
            int id = DeskTypeSingleton.getInstance().getDeskTypes().get(i).getDeskTypeID();
            String deskType = DeskTypeSingleton.getInstance().getDeskTypes().get(i).getDeskType().toString();
            DeskType dt = new DeskType(id, deskType);
            deskTypes.add(dt);
            spinnerList.add(deskType);
        }
        addListingSpinner = (Spinner) findViewById(R.id.addListingSpinner);
        deskTypeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerList);
        addListingSpinner.setAdapter(deskTypeAdapter);





        listSpaceMainImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });

        listSpaceSmallImg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), 2);
            }
        });

        listSpaceSmallImg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), 3);
            }
        });

        listSpaceSmallImg3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 4);
            }
        });

        listSpaceSmallImg4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 5);
            }
        });


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addListing();
            }
        });

//        if(!PicturesSingleton.getInstance().getPictures().isEmpty()) {
//            listSpaceSmallImg1.setImageBitmap(PicturesSingleton.getInstance().getPictures().get(1).getListingImage());
//        }



    }


    public  void onActivityResult(int reqCode, int resCode, Intent data) {
        if(resCode == RESULT_OK) {
            if(reqCode == 1) {
                mainImgBitmap = getBitmap(data.getData());
                listSpaceMainImage.setImageBitmap(mainImgBitmap);
                mainImgString = encodeToBase64(mainImgBitmap, Bitmap.CompressFormat.JPEG, 80);
                Log.d("MIS", mainImgString);

            }

            if(reqCode == 2) {
                smallImg1Bitmap = getBitmap(data.getData());
                listSpaceSmallImg1.setImageBitmap(smallImg1Bitmap);
                smallImg1String = encodeToBase64(smallImg1Bitmap, Bitmap.CompressFormat.JPEG, 80);
                Log.d("MIS", smallImg1String);
                }

            if(reqCode == 3) {
                smallImg2Bitmap = getBitmap(data.getData());
                listSpaceSmallImg2.setImageBitmap(smallImg2Bitmap);
                smallImg2String = encodeToBase64(smallImg2Bitmap, Bitmap.CompressFormat.JPEG, 80);
                Log.d("MIS", smallImg2String);
            }

            if(reqCode == 4) {
                smallImg3Bitmap = getBitmap(data.getData());
                listSpaceSmallImg3.setImageBitmap(smallImg3Bitmap);
                smallImg3String = encodeToBase64(smallImg3Bitmap, Bitmap.CompressFormat.JPEG, 80);
                Log.d("MIS", smallImg3String);
            }

            if(reqCode == 5) {
                smallImg4Bitmap = getBitmap(data.getData());
                listSpaceSmallImg4.setImageBitmap(smallImg4Bitmap);
                smallImg4String = encodeToBase64(smallImg4Bitmap, Bitmap.CompressFormat.JPEG, 80);
                Log.d("MIS", smallImg4String);
            }
        }
    }



    private void addListing() {



        //final String deskType = addListingSpinner.getSelectedItem().toString();
        final String deskID = Long.toString(addListingSpinner.getSelectedItemId() + 1);
        final String title = etAddListingTitle.getText().toString().trim();
        final String description = etAddListingDescription.getText().toString().trim();
        final String noOfDesks = etAddListingNoOfDesks.getText().toString().trim();
        final String pricePerMonth = etAddListingPricePerMonth.getText().toString().trim();
        final String userID = Integer.toString(UserID.getInstance().getLoggedInId());

        final String street1 = etListingStreet1.getText().toString().trim();
        final String street2 = etListingStreet2.getText().toString().trim();
        final String city = etListingCity.getText().toString().trim();
        final String county = etListingCounty.getText().toString().trim();
        final String country = etListingCountry.getText().toString().trim();


//        Log.d("Main IMG Bitmap:", getMainImg());
        final String mainImg = getMainImg();
        final String smallImg1 = getSmallImg1String();
        final String smallImg2 = getSmallImg2String();
        final String smallImg3 = getSmallImg3String();
        final String smallImg4 = getSmallImg4String();

        class AddListing extends AsyncTask<Void, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ListSpace.this,"Adding...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();

                RequestHandler rh = new RequestHandler();
                String listingID = s;



                Toast.makeText(ListSpace.this, s, Toast.LENGTH_LONG).show();





                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                // your code here
                                Intent i = new Intent(ListSpace.this, HostingActivity.class);
                                startActivity(i);
                            }
                        },
                        1500
                );






            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                images = new ArrayList<>();
                images.add(mainImg);
                images.add(smallImg1);
                images.add(smallImg2);
                images.add(smallImg3);
                images.add(smallImg4);
                images.removeAll(Collections.singleton(null));


                HashMap<String, String> listingParams = new HashMap<>();
                listingParams.put(Config.KEY_LISTING_USER_ID, userID);
                listingParams.put(Config.KEY_LISTING_DESK_TYPE_ID, deskID);
                listingParams.put(Config.KEY_LISTING_DESKS_AVAILABLE, noOfDesks);
                listingParams.put(Config.KEY_LISTING_LISTING_PRICE, pricePerMonth);
                listingParams.put(Config.KEY_LISTING_LISTING_TITLE, title);
                listingParams.put(Config.KEY_LISTING_LISTING_DESCRIPTION, description);
                String listingID = rh.sendPostRequest(Config.URL_ADD_LISTING, listingParams);

                String allListingsString = rh.sendGetRequest(Config.URL__GET_ALL_LISTINGS);

                if(allListingsString !=null) {
                    ALL_LISTINGS_JSON_STRING = allListingsString;
                    addListingsToList();
                    ListingsSingleton.getInstance().setListings(listingsList);
//                    Log.d("ListingsList Size: ", Integer.toString(ListingsSingleton.getInstance().getListings().size()));
                }

                HashMap<String, String> addressParams = new HashMap<>();
                addressParams.put(Config.KEY_ADDRESS_LISTING_ID, listingID);
                addressParams.put(Config.KEY_ADDRESS_STREET1, street1);
                addressParams.put(Config.KEY_ADDRESS_STREET2, street2);
                addressParams.put(Config.KEY_ADDRESS_CITY, city);
                addressParams.put(Config.KEY_ADDRESS_COUNTY, county);
                addressParams.put(Config.KEY_ADDRESS_COUNTRY, country);
                rh.sendPostRequest(Config.URL_ADD_ADDRESS, addressParams);

                String allAddressString = rh.sendGetRequest(Config.URL_GET_ALL_ADDRESSES);
                if(allAddressString !=null) {
                    ALL_ADDRESSES_JSON_STRING = allAddressString;
                    addAddressesToList();
                    AddressSingleton.getInstance().setAddresses(addressList);
                }

                int j=1;
                for(int i=0; i<images.size(); i++) {
                    HashMap<String, String> mainImgParams = new HashMap<>();
                    mainImgParams.put(Config.KEY_PICTURE_LISTING_ID, listingID);
//                    Log.d("ListingID", listingID);
                    mainImgParams.put("encoded_string", images.get(i).toString());
                    mainImgParams.put("image_name", "listingimage" + (listingID) + j);
                    rh.sendPostRequest("http://www.johnrockfinalyearproject.com/postImage.php", mainImgParams);
                    j++;

//                    Log.d("id", mainImgParams.get(Config.KEY_PICTURE_LISTING_ID).toString());
                }

                String allImagesString = rh.sendGetRequest(Config.URL_GET_ALL_PICTURES);

                if(allImagesString !=null) {
//                                Log.d("Gets to", "B");
                    ALL_PICTURES_JSON_STRING = allImagesString;
//                    Log.d("Gets to", "C");
                    addPicturesToList();
//                    Log.d("Gets to", "D");
                    PicturesSingleton.getInstance().setPictures(picturesList);
//                    Log.d("PicturesList Size", Integer.toString(PicturesSingleton.getInstance().getPictures().size()));


                }
                return listingID;
            }
        }

        AddListing aL = new AddListing();
        aL.execute();


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
                int addressListingID = Integer.parseInt(jo.getString(Config.TAG_ADDRESS_LISTING_ID));
                String street1 = jo.getString(Config.TAG_ADDRESS_STREET1);
                String street2 = jo.getString(Config.TAG_ADDRESS_STREET2);
                String city = jo.getString(Config.TAG_ADDRESS_CITY);
                String county = jo.getString(Config.TAG_ADDRESS_COUNTY);
                String country = jo.getString(Config.TAG_ADDRESS_COUNTRY);

                Address a = new Address(addressID, addressListingID, street1, street2, city, county, country);
                addressList.add(a);
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
//                Log.d("listingImgPath", listingImagePath);
                Bitmap bitmap = getBitmapFromURL("http://www.johnrockfinalyearproject.com/" +listingImagePath);
//                Log.d("Full Path", "http://www.johnrockfinalyearproject.com/" +listingImagePath);


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


    public String getMainImg() {
        return mainImgString;
    }

    public String getSmallImg1String() {
        return smallImg1String;
    }


    public String getSmallImg2String() {
        return smallImg2String;
    }


    public String getSmallImg3String() {
        return smallImg3String;
    }


    public String getSmallImg4String() {
        return smallImg4String;
    }


    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality)
    {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public Bitmap getBitmap(Uri uri) {
        InputStream image_steam = null;
        try {
            image_steam = getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(image_steam);
        return bitmap;
    }

}
