package finalyear.officeme.activity;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Geocoder;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import finalyear.officeme.CustomDatePicker;
import finalyear.officeme.CustomTimePicker;
import finalyear.officeme.DatabaseHandler.Config;
import finalyear.officeme.DatabaseHandler.RequestHandler;
import finalyear.officeme.R;
import finalyear.officeme.SetFavouriteAsync;
import finalyear.officeme.Singletons.AddressSingleton;
import finalyear.officeme.Singletons.DeskTypeSingleton;
import finalyear.officeme.Singletons.ListingsSingleton;
import finalyear.officeme.Singletons.LoggedIn;
import finalyear.officeme.Singletons.PicturesSingleton;
import finalyear.officeme.Singletons.UserID;
import finalyear.officeme.Singletons.UserList;
import finalyear.officeme.model.Address;
import finalyear.officeme.model.BookingRequest;
import finalyear.officeme.model.DeskType;
import finalyear.officeme.model.Favourite;
import finalyear.officeme.model.Listing;
import finalyear.officeme.model.Picture;

public class DetailedListing extends AppCompatActivity implements OnMapReadyCallback, SetFavouriteAsync {

    private GoogleMap mMap;
    ImageView mainImg, img2, img3, img4, img5;
    TextView detailedListingPrice, detailedListingTitle, detailedListingDescription, detailedListingOfficeType, detailedListingDesksAvailable;
    Listing listing;
    ArrayList<Listing> allListingsList;
    ArrayList<DeskType> allDeskTypes;
    ArrayList<Picture> allPictures;
    ArrayList<Bitmap> listingImages;
    ArrayList<Address> listingAddresses;
    String addressString;
    SupportMapFragment mapFragment;
    Bitmap mapImage;
    String mapPrice;
    Boolean isFavourite = false;
    CheckIfFavourite cf = new CheckIfFavourite();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_listing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        final Button favBtn = (Button) findViewById(R.id.btnFavDetailedListing);
        final ImageButton bookViewing = (ImageButton) findViewById(R.id.btnBookViewing);
        final ImageButton emailHost = (ImageButton) findViewById(R.id.btnDetailedListingEmailHost);
        final ImageButton phoneHost = (ImageButton) findViewById(R.id.btnDetailedListingCallHost);


        cf.delegate = this;
        cf.execute();


        allListingsList = new ArrayList<>();
        allListingsList.addAll(ListingsSingleton.getInstance().getListings());
        allDeskTypes = new ArrayList<>();
        allDeskTypes.addAll(DeskTypeSingleton.getInstance().getDeskTypes());
        allPictures = new ArrayList<>();
        allPictures.addAll(PicturesSingleton.getInstance().getPictures());
        listingImages = new ArrayList<>();
        listingAddresses = new ArrayList<>();
        listingAddresses.addAll(AddressSingleton.getInstance().getAddresses());


        getListingInfo();
        setListingInfo();
        setDeskInfo();
        setImages();
        addressString = getAddressString();
        callMap();
        if (isFavourite == true) {
            favBtn.setBackground(getResources().getDrawable(R.drawable.detailedfavred));
            favBtn.setTag(R.drawable.detailedfavred);
        }

        if (isFavourite == false) {
            favBtn.setTag(R.drawable.detailedfavwhite);
        }


        favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (LoggedIn.getInstance().getLoggedIn() == true && favBtn.getTag().equals(R.drawable.detailedfavwhite)) {
                    favBtn.setBackground(getResources().getDrawable(R.drawable.detailedfavred));
                    favBtn.setTag(R.drawable.detailedfavred);
                    addFavourite();
                } else if (LoggedIn.getInstance().getLoggedIn() == true && favBtn.getTag().equals(R.drawable.detailedfavred)) {
                    favBtn.setBackground(getResources().getDrawable(R.drawable.detailedfavwhite));
                    favBtn.setTag(R.drawable.detailedfavwhite);
                    removeFavourite();
                } else {
                    Toast.makeText(DetailedListing.this, "You must be logged in to add favourite", Toast.LENGTH_LONG).show();
                }

            }
        });


        bookViewing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (LoggedIn.getInstance().getLoggedIn() == true) {
                    final Dialog dialog = new Dialog(DetailedListing.this);
//                DatePicker dp = (DatePicker) findViewById(R.id.datePicker1);

//                View inflateBookViewing = getLayoutInflater().inflate(R.layout.book_viewing, null);
//                final CustomDatePicker dp = (CustomDatePicker) inflateBookViewing.findViewById(R.id.datePicker1);
                    dialog.setContentView(R.layout.book_viewing);
                    dialog.setTitle("Request Viewing");
                    final CustomDatePicker dp = (CustomDatePicker) dialog.findViewById(R.id.datePicker1);
                    dp.setMinDate(System.currentTimeMillis() - 1000);
                    final CustomTimePicker cp = (CustomTimePicker) dialog.findViewById(R.id.timePicker1);
                    dialog.setCancelable(true);


                    Button btnRequestViewing = (Button) dialog.findViewById(R.id.btnSendBookingRequest);
                    btnRequestViewing.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int day = dp.getDayOfMonth();
                            int month = dp.getMonth() + 1;
                            int year = dp.getYear();
                            int hour = cp.getCurrentHour();
                            int minute = cp.getCurrentMinute();

                            BookingRequest br = new BookingRequest(day, month, year, hour, minute, UserID.getInstance().getLoggedInId(), listing.getListingUserID(), listing.getListingID());

                            /////ADD ASYNC TO ADD BOOKING REQUEST
                            addBookingRequest(br);


                            ArrayList<BookingRequest> bReqs = new ArrayList<BookingRequest>();
                            bReqs.add(br);
                            Log.d("BookingRequestSize: ", Integer.toString(bReqs.size()));
                            Log.d("Booking Details:", " Day: " + Integer.toString(day) + " Month: " + Integer.toString(month) + " Year: " + Integer.toString(year)
                                    + " Hour: " + Integer.toString(hour) + " Minute: " + Integer.toString(minute) + " UserID: " + Integer.toString(UserID.getInstance().getLoggedInId()) + " Listing ID: " + Integer.toString(listing.getListingID()));

                        }
                    });

                    dialog.show();
                } else {
                    Toast.makeText(DetailedListing.this, "Your must login to make a booking", Toast.LENGTH_LONG).show();
                }
            }


        });

        emailHost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", getHostEmail(listing.getListingUserID()), null));
                intent.putExtra(Intent.EXTRA_SUBJECT, "Email Regarding " + listing.getListingTitle());
                startActivity(Intent.createChooser(intent, "Choose an Email client :"));

            }
        });

        phoneHost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + getHostPhone(listing.getListingUserID())));
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for Activity#requestPermissions for more details.
                        return;
                    }
                }
                startActivity(callIntent);
            }
        });
    }

    private void addBookingRequest(BookingRequest br) {
        final int day = br.getBookingRequestDay();
        final int month = br.getBookingRequestMonth();
        final int year = br.getBookingRequestYear();
        final int hour = br.getBookingRequestHour();
        final int minute = br.getBookingRequestMinute();
        final int hostID = br.getBookingRequestHostID();
        final int requestUserID = br.getBookingRequestVisitorID();
        final int listingID = br.getBookingRequestListingID();

        class AddUser extends AsyncTask<Void,Void,String> {
//
//            ProgressDialog loading;
////
////            @Override
////            protected void onPreExecute() {
////                super.onPreExecute();
////                loading = ProgressDialog.show(DetailedListing.this,"Adding...","Wait...",false,false);
////            }
//
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Toast.makeText(DetailedListing.this, s, Toast.LENGTH_LONG).show();


            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                params.put(Config.KEY_VIEWING_REQUEST_DAY,Integer.toString(day));
                params.put(Config.KEY_VIEWING_REQUEST_MONTH, Integer.toString(month));
                params.put(Config.KEY_VIEWING_REQUEST_YEAR, Integer.toString(year));
                params.put(Config.KEY_VIEWING_REQUEST_HOUR, Integer.toString(hour));
                params.put(Config.KEY_VIEWING_REQUEST_MINUTE, Integer.toString(minute));
                params.put(Config.KEY_VIEWING_REQUEST_HOST_ID, Integer.toString(hostID));
                params.put(Config.KEY_VIEWING_REQUEST_REQUEST_USER_ID, Integer.toString(requestUserID));
                params.put(Config.KEY_VIEWING_REQUEST_LISTING_ID, Integer.toString(listingID));
                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.URL_ADD_VIEWING_REQUEST, params);
                return res;
            }
        }

        AddUser au = new AddUser();
        au.execute();

    }

    private String getHostEmail(int userID) {
        String email = null;
        for(int i=0; i < UserList.getInstance().getUserList().size(); i++) {
            if(userID == UserList.getInstance().getUserList().get(i).getId()) {
                email = UserList.getInstance().getUserList().get(i).get_email();
                break;

            }
        }
        return email;
    }

    private String getHostPhone(int userID) {
        String phone = null;
        for(int i=0; i < UserList.getInstance().getUserList().size(); i++) {
            if(userID == UserList.getInstance().getUserList().get(i).getId()) {
                phone = UserList.getInstance().getUserList().get(i).getPhoneNumber();
                break;

            }
        }
        return phone;
    }

    private void callMap() {
        mapFragment.getMapAsync(this);
    }

    private String getAddressString() {
        String address = null;

        for(int i=0; i<listingAddresses.size(); i++) {
            if(getListingID() == listingAddresses.get(i).getAddressListingID()) {
                String street1 = listingAddresses.get(i).getAddressStreet1();
                String street2 = listingAddresses.get(i).getAddressStreet2();
                String city = listingAddresses.get(i).getAddressCity();
                String county = listingAddresses.get(i).getAddressCounty();
                String country = listingAddresses.get(i).getAddressCountry();

                address = (street1 + ", " + street2 + ", " + city + ", " + county + ", " + country);
            }
        }

        return address;
    }

    private void setImages() {
        for(int i=0; i<allPictures.size(); i++) {
            if(getListingID() == allPictures.get(i).getPictureListingID()) {
                Bitmap bitmap = allPictures.get(i).getListingImage();

                listingImages.add(bitmap);
            }
        }

        ImageView mainImg = (ImageView) findViewById(R.id.scrollViewMainImg);
        ImageView img2 = (ImageView) findViewById(R.id.scrollViewImg2);
        ImageView img3 = (ImageView) findViewById(R.id.scrollViewImg3);
        ImageView img4 = (ImageView) findViewById(R.id.scrollViewImg4);
        ImageView img5 = (ImageView) findViewById(R.id.scrollViewImg5);

        if(listingImages.size() > 0) {

            int count = 0;

            if(count < listingImages.size()) {
                Bitmap b = listingImages.get(count);
                mainImg.setImageBitmap(b);
            }

            if(count+1 < listingImages.size()) {
                Bitmap b = listingImages.get(count+1);
                img2.setImageBitmap(b);
            }

            if(count+2 < listingImages.size()) {
                Bitmap b = listingImages.get(count+2);
                img3.setImageBitmap(b);
            } else img3.setVisibility(View.GONE);

            if(count+3 < listingImages.size()) {
                Bitmap b = listingImages.get(count+3);
                img4.setImageBitmap(b);
            }

            if(count+4 < listingImages.size()) {
                Bitmap b = listingImages.get(count+4);
                img5.setImageBitmap(b);
                count++;
            }

        }
    }


    private void setDeskInfo() {
        for(int i=0; i<allDeskTypes.size(); i++) {
            if(listing.getDeskTypeID() == allDeskTypes.get(i).getDeskTypeID()) {
                TextView deskType = (TextView) findViewById(R.id.listImgOfficeType);
                deskType.setText(allDeskTypes.get(i).getDeskType());
                break;
            }
        }
    }

    private void setListingInfo() {
        TextView listingTitle = (TextView) findViewById(R.id.detailedListingTitle);
        listingTitle.setText(listing.getListingTitle());
        TextView listingDescription = (TextView) findViewById(R.id.detailedListingDescription);
        listingDescription.setText(listing.getListingDescription());
        TextView listingDesksAvailable = (TextView) findViewById(R.id.listImgDesksAvailable);
        listingDesksAvailable.setText(listing.getDesksAvailable() + " Desks Available");
        TextView listingPrice = (TextView) findViewById(R.id.detailedListingPrice);
        listingPrice.setText("€" + listing.getListingPrice() + " Per Desk Per Month");
        mapPrice = ("€" + listing.getListingPrice() + " Per Desk Per Month");

    }

    private void getListingInfo() {
        for(int i=0; i<allListingsList.size(); i++) {
            if(getListingID() == allListingsList.get(i).getListingID()) {
                int listingId = getListingID();
                int listingUserId = allListingsList.get(i).getListingUserID();
                int deskTypeId = allListingsList.get(i).getDeskTypeID();
                int desksAvailable = allListingsList.get(i).getDesksAvailable();
                int listingPrice = allListingsList.get(i).getListingPrice();
                String listingTitle = allListingsList.get(i).getListingTitle();
                String listingDescription = allListingsList.get(i).getListingDescription();

                listing = new Listing(listingId, listingUserId, deskTypeId, desksAvailable, listingPrice, listingTitle, listingDescription);
                break;
            }
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;



        LatLng ireland = new LatLng(53.344104, -6.2674937);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ireland, 12));

        //Code works but crashes is addressString is not valid.

        Geocoder gc = new Geocoder(this);
        List<android.location.Address> list = null;

        try {
            if(addressString != null) {
                list = gc.getFromLocationName(addressString, 1);


                if(list.size() > 0) {
                    android.location.Address add = list.get(0);
                    double lat = add.getLatitude();
                    double lng = add.getLongitude();
                    LatLng position = new LatLng(lat, lng);
                    Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat,lng)).title(mapPrice));
                    marker.showInfoWindow();
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getListingID() {
        Bundle extras = getIntent().getExtras();
        int listingID = extras.getInt("Listing ID");
        return listingID;
    }

    //Adding favourite
    private void addFavourite(){

        class AddFavourite extends AsyncTask<Void,Void,String> {



            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(DetailedListing.this,"Adding...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(DetailedListing.this, s, Toast.LENGTH_LONG).show();



            }

            @Override
            protected String doInBackground(Void... v) {
               HashMap<String,String> params = new HashMap<>();
                params.put("favouriteUserID",Integer.toString(UserID.getInstance().getLoggedInId()));
                params.put("favouriteListingID", Integer.toString(getListingID()));
                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.URL_ADD_FAVOURITE, params);
                return res;
            }
        }

        AddFavourite af = new AddFavourite();
        af.execute();

    }

    //Adding an user
    private void removeFavourite(){

        class RemoveFavourite extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(DetailedListing.this,"Removing...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(DetailedListing.this, s, Toast.LENGTH_LONG).show();


            }

            @Override
            protected String doInBackground(Void... v) {
                RequestHandler rh = new RequestHandler();
                String res = rh.sendGetRequest("http://johnrockfinalyearproject.com/removeFavourite.php?favouriteUserID=" + Integer.toString(UserID.getInstance().getLoggedInId()) + "&favouriteListingID="  + Integer.toString(getListingID()));



//
//                HashMap<String,String> params = new HashMap<>();
//                params.put("favouriteUserID",Integer.toString(UserID.getInstance().getLoggedInId()));
//                params.put("favouriteListingID", Integer.toString(getListingID()));
//                RequestHandler rh = new RequestHandler();
//                String res = rh.sendPostRequest(Config.URL_ADD_FAVOURITE, params);
//                return res;
                return res;
            }
        }

        RemoveFavourite rf = new RemoveFavourite();
        rf.execute();
    }


    class CheckIfFavourite extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            public SetFavouriteAsync delegate = null;

//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//                loading = ProgressDialog.show(DetailedListing.this,"Checking...","Wait...",false,false);
//            }

            @Override
            protected void onPostExecute(String s) {
//                super.onPostExecute(s);
//                loading.dismiss();
//                Toast.makeText(DetailedListing.this, s, Toast.LENGTH_LONG).show();
                delegate.processFinish(s);

            }

            @Override
            protected String doInBackground(Void... v) {
                RequestHandler rh = new RequestHandler();
                String res = rh.sendGetRequest("http://johnrockfinalyearproject.com/checkFavourite.php?favouriteUserID=" + Integer.toString(UserID.getInstance().getLoggedInId()) + "&favouriteListingID="  + Integer.toString(getListingID()));
                return res;
            }
        }



    @Override
    public void processFinish(String output) {


        Log.d("PutPut", output.toString().trim());
        if(!output.toString().contains("Not")) {
            isFavourite = true;
            Log.d("Is True?", isFavourite.toString());
            final Button favBtn = (Button) findViewById(R.id.btnFavDetailedListing);
            favBtn.setBackground(getResources().getDrawable(R.drawable.detailedfavred));
            favBtn.setTag(R.drawable.detailedfavred);
        }

    }

    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(DetailedListing.this, MainActivity.class);
        startActivity(setIntent);
    }
}
