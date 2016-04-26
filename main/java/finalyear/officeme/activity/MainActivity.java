package finalyear.officeme.activity;




import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import finalyear.officeme.Login;
import finalyear.officeme.MarkerListing;
import finalyear.officeme.R;
import finalyear.officeme.Singletons.AddressSingleton;
import finalyear.officeme.Singletons.DeskTypeSingleton;
import finalyear.officeme.Singletons.ListingsSingleton;
import finalyear.officeme.Singletons.LoggedIn;
import finalyear.officeme.Singletons.LoggedInUserDetails;
import finalyear.officeme.Singletons.PicturesSingleton;
import finalyear.officeme.model.Address;
import finalyear.officeme.model.DeskType;
import finalyear.officeme.model.Favourite;
import finalyear.officeme.model.Listing;
import finalyear.officeme.model.Picture;
import finalyear.officeme.model.User;
import finalyear.officeme.parse.ParseActivity;
//extends AppCompatActivity

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    Button loginActivityButton;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    MenuItem menuItem;
    Menu nav;
    User loggedInUserDetails;


    ListView allListingsListView;
    ArrayAdapter allListingAdapter;
    RatingBar allListingAverageRating;
    Button allListingFavouriteButton;
    ArrayList<Listing> allListingsList;
    ArrayList<Picture> allPicturesList;
    ArrayList<DeskType> allDeskTypes;
    ArrayList<Address> allAddresses;
    ArrayList<String> citySpinnerList, deskSpinnerList;
    Set<String>  removeDuplicates;
    Spinner citySpinner, deskTypeSpinner;
    ArrayAdapter citySpinnerAdapter, deskSpinnerAdapter;
    SeekBar desksAvailableSeekBar, deskPriceSeekBar;
    TextView currentDesksAvailableValue, currentDeskPriceValue;

    //SearchItemsResults
    String deskTypeSelected, citySelected;
    int deskNumberSelected, deskPriceSelected;
    ArrayList<Listing> finalFilteredList;

    //Map Params
    SupportMapFragment mapFragment;
    private GoogleMap mMap;
    List<Marker> markers;
    List<MarkerListing> markerListings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialising toolbar and setting it as the the actionbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        actionBarSettings(myToolbar);

        //PopulateLists
        allListingsList = new ArrayList<>();
        allListingsList.addAll(ListingsSingleton.getInstance().getListings());
        allPicturesList = new ArrayList<>();
        allPicturesList.addAll(PicturesSingleton.getInstance().getPictures());
        allDeskTypes = new ArrayList<>();
        allDeskTypes.addAll(DeskTypeSingleton.getInstance().getDeskTypes());
        allAddresses = new ArrayList<>();
        allAddresses.addAll(AddressSingleton.getInstance().getAddresses());
        allListingsListView = (ListView) findViewById(R.id.allListingsListView);
        finalFilteredList = new ArrayList<>();
        markers = new ArrayList<>();
        markerListings = new ArrayList<>();


        //Initialise and populate city spinner in search tab
        citySpinnerList = new ArrayList<>();
        String allCities = "All Cities";
        citySpinnerList.add(allCities);
        for(int i=0; i<AddressSingleton.getInstance().getAddresses().size(); i++) {
            String city = AddressSingleton.getInstance().getAddresses().get(i).getAddressCity();
            citySpinnerList.add(city);
        }

        removeDuplicates = new HashSet<>();
        removeDuplicates.addAll(citySpinnerList);
        citySpinnerList.clear();
        citySpinnerList.addAll(removeDuplicates);

        citySpinner = (Spinner) findViewById(R.id.spinnerCity);
        citySpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, citySpinnerList);
        citySpinner.setAdapter(citySpinnerAdapter);

        //Initialise and populate desk type spinner in search tab
        deskSpinnerList = new ArrayList<>();
        String all = "All Desk Types";
        deskSpinnerList.add(all);
        for(int i=0; i<DeskTypeSingleton.getInstance().getDeskTypes().size(); i++) {
            int id = DeskTypeSingleton.getInstance().getDeskTypes().get(i).getDeskTypeID();
            String deskType = DeskTypeSingleton.getInstance().getDeskTypes().get(i).getDeskType().toString();
            deskSpinnerList.add(deskType);
        }

        deskTypeSpinner = (Spinner) findViewById(R.id.spinnerDeskType);
        deskSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, deskSpinnerList);
        deskTypeSpinner.setAdapter(deskSpinnerAdapter);

        //Initialise and update SeekBars in Search
        desksAvailableSeekBar = (SeekBar) findViewById(R.id.seekBarDesksAvailable);
        desksAvailableSeekBar.setMax(9);
        currentDesksAvailableValue = (TextView) findViewById(R.id.curentValueDesksAvailable);
        desksAvailableSeekBar.setOnSeekBarChangeListener(new desksAvailableListener());

        deskPriceSeekBar = (SeekBar) findViewById(R.id.seekBarDeskPrice);
        deskPriceSeekBar.setMax(400);
        currentDeskPriceValue = (TextView) findViewById(R.id.curentValueDeskPrice);
        deskPriceSeekBar.setOnSeekBarChangeListener(new deskPriceListener());

        //Initialise Search Button
        final Button searchButton = (Button) findViewById(R.id.btnSearch);
        final Button resetSearchButton = (Button) findViewById(R.id.btnResetSearch);

        //Init Map
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.searchMap);
        finalFilteredList.addAll(ListingsSingleton.getInstance().getListings());
        callMap();




        //Initialising tabs
        setUpTabs();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalFilteredList.clear();
                deskTypeSelected = deskTypeSpinner.getSelectedItem().toString();
                if(!currentDesksAvailableValue.equals("10+")) {
//                    deskNumberSelected = Integer.parseInt(currentDesksAvailableValue.getText().toString().trim());
                    deskNumberSelected = (desksAvailableSeekBar.getProgress()+1);
                } else deskNumberSelected = 11;
                if(!currentDeskPriceValue.equals("500+")) {
//                    deskPriceSelected = Integer.parseInt(currentDeskPriceValue.getText().toString().trim());
                    deskPriceSelected = (deskPriceSeekBar.getProgress()+100);
                } else deskPriceSelected = 501;
                citySelected = citySpinner.getSelectedItem().toString();

                Log.d("DEBUG", "Desk Type Selected is: "+deskTypeSelected);
                Log.d("DEBUG", "City Selected is: "+citySelected);
                Log.d("DEBUG", "Available Desks Selected is: "+ Integer.toString(deskNumberSelected));
                Log.d("DEBUG", "Desk Price Selected is: "+ Integer.toString(deskPriceSelected));
                
                filterList(deskTypeSelected, deskNumberSelected, deskPriceSelected, citySelected);

//                for(int i=0; i<finalFilteredList.size(); i++) {
//                    Log.d("DEBUG", finalFilteredList.get(i).getListingTitle());
//                }

                Log.d("Filtered List Size: ", Integer.toString(finalFilteredList.size()));

                mMap.clear();
                callMap();



                TabHost host = (TabHost) findViewById(R.id.tabHost);
                host.setCurrentTab(2);
            }
        });

        resetSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalFilteredList.clear();
                mMap.clear();
                finalFilteredList.addAll(ListingsSingleton.getInstance().getListings());
                callMap();
                desksAvailableSeekBar.setProgress(0);
                deskPriceSeekBar.setProgress(0);

            }
        });

        //Initialising NavigationView
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        nav = navigationView.getMenu();
        menuItem = nav.findItem(R.id.LogInSignUp);






        if(LoggedIn.getInstance().getLoggedIn() == true) {
            menuItem.setTitle("Log Out");
        }


        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {


                //Checking if the item is in checked state or not, if not make it in checked state
                if(menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(false);


                //Closing drawer on item click
                drawerLayout.closeDrawers();

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()){

                    case R.id.latestNews:
                        Intent in = new Intent(MainActivity.this, ParseActivity.class);
                        startActivity(in);
                        return true;
                    case R.id.idSwitchToHosting:
                        if(LoggedIn.getInstance().getLoggedIn() == true){
                            Intent hostingIntent = new Intent(MainActivity.this, HostingActivity.class);
                            startActivity(hostingIntent);
                            return true;
                        } else {
                            Toast.makeText(getApplicationContext(), "Please Login to View Hosting", Toast.LENGTH_SHORT).show();
                            Intent logInRegisterIntent = new Intent(MainActivity.this, Login.class);
                            startActivity(logInRegisterIntent);
                            return true;
                        }

                    case R.id.LogInSignUp:
                        if(LoggedIn.getInstance().getLoggedIn() == true) {
                            Toast.makeText(getApplicationContext(), "Successfully Logged Out", Toast.LENGTH_SHORT).show();
                            LoggedIn.getInstance().setLoggedIn(false);
                            Intent i = new Intent(MainActivity.this, SplashActivity.class);
                            startActivity(i);
                            return true;
                        } else {
                            Intent logInRegisterIntent = new Intent(MainActivity.this, Login.class);
                            startActivity(logInRegisterIntent);
                            return true;
                        }

                    case R.id.savedAds:
                        if(LoggedIn.getInstance().getLoggedIn() == true) {
                            Intent fav = new Intent(MainActivity.this, Favourites.class);
                            startActivity(fav);
                            return true;
                        } else {
                            Toast.makeText(getApplicationContext(), "You must login to view favourites", Toast.LENGTH_SHORT).show();
                            Intent i4 = new Intent(MainActivity.this, Login.class);
                            startActivity(i4);
                            return true;
                        }


                    case R.id.howItWorks:


                    case R.id.bookings:
                        if(LoggedIn.getInstance().getLoggedIn() == true) {
                            Intent bI = new Intent(MainActivity.this, VisitorBookingInformation.class);
                            startActivity(bI);
                            return true;
                        } else {
                            Toast.makeText(getApplicationContext(), "You must login to view bookings", Toast.LENGTH_SHORT).show();
                            return true;
                        }

                    default:
                        Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                        return true;


                }
            }
        });



        //Initialising drawer layout
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        populateAlListingsList();

        allListingsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent n = new Intent(MainActivity.this, DetailedListing.class);
                Listing listing = allListingsList.get(position);
                n.putExtra("Listing ID", listing.getListingID());
                startActivity(n);
            }
        });


    }

    private void filterList(String deskTypeSelected, int deskNumberSelected, int deskPriceSelected, String citySelected) {
        ArrayList<Listing> allListings = new ArrayList<>();
        allListings.addAll(ListingsSingleton.getInstance().getListings());
        ArrayList<Listing> filterListing = new ArrayList<>();

        Log.d("DEBUG", "Desk Type Selected is: "+deskTypeSelected);
        Log.d("DEBUG", "City Selected is: "+citySelected);
        Log.d("DEBUG", "Available Desks Selected is: "+ Integer.toString(deskNumberSelected));
        Log.d("DEBUG", "Desk Price Selected is: " + Integer.toString(deskPriceSelected));

        Log.d("A AllListing: ", Integer.toString(allListings.size()));


        for(int i=0; i<allListings.size(); i++) {
            if(allListings.get(i).getListingPrice() <= deskPriceSelected &&  allListings.get(i).getDesksAvailable() >= deskNumberSelected) {
                filterListing.add(allListings.get(i));
            }
        }



        Log.d("A FilterListing: ", Integer.toString(filterListing.size()));

        allListings.clear();
        allListings.addAll(filterListing);
        filterListing.clear();

        Log.d("B AllListing: ", Integer.toString(allListings.size()));

        if(!deskTypeSelected.equalsIgnoreCase("All Desk Types")) {
            for(int i=0; i<allListings.size(); i++) {
                if(deskTypeSelected.equalsIgnoreCase(getDeskType(allListings.get(i).getDeskTypeID()))) {
                    filterListing.add(allListings.get(i));
                }
            }

            allListings.clear();
            allListings.addAll(filterListing);
            filterListing.clear();
        }

        Log.d("C AllListing: ", Integer.toString(allListings.size()));

        if(!citySelected.equalsIgnoreCase("All Cities")) {
            for(int i=0; i<allListings.size(); i++) {
                if(citySelected.equalsIgnoreCase(getCity(allListings.get(i).getListingID()))) {
                    filterListing.add(allListings.get(i));
                }
            }
            allListings.clear();
            allListings.addAll(filterListing);
            filterListing.clear();
        }

        finalFilteredList.clear();
//        callMap();
        finalFilteredList.addAll(allListings);
    }

    private void populateAlListingsList() {
        allListingAdapter = new AllListingsListAdapter();
        allListingsListView.setAdapter(allListingAdapter);
    }



    private class AllListingsListAdapter extends ArrayAdapter<Listing> {
        public AllListingsListAdapter() {
            super(MainActivity.this, R.layout.listview_listings, allListingsList);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if(view == null)
                view = getLayoutInflater().inflate(R.layout.listview_listings, parent, false);

            Listing currentListing= allListingsList.get(position);

            TextView myListingListingPrice = (TextView) view.findViewById(R.id.listImgPrice);
            myListingListingPrice.setText("€" + Integer.toString(currentListing.getListingPrice()) + " Per Desk Per Month");
            TextView myListingListingTitle = (TextView) view.findViewById(R.id.listImgTitle);
            myListingListingTitle.setText(currentListing.getListingTitle());
            TextView myListingDesksAvailable = (TextView) view.findViewById(R.id.listImgDesksAvailable);
            myListingDesksAvailable.setText(Integer.toString(currentListing.getDesksAvailable()) + " Desks Available");




            //GetMainImage
            Bitmap bitmap = getImage(currentListing.getListingID());
            ImageView myListingsImageView = (ImageView) view.findViewById(R.id.listImgImgMain);
            myListingsImageView.setImageBitmap(bitmap);
            String bitmapcase;
            if(bitmap == null) {
                bitmapcase = "bitmap null";
            } else bitmapcase = "bitmap not null";
            Log.d("Is bitmap null?", bitmapcase);

            String office = getDeskType(currentListing.getDeskTypeID());
            TextView myListingOfficeType = (TextView) view.findViewById(R.id.listImgOfficeType);
            myListingOfficeType.setText(office);

            String city = getCity(currentListing.getListingID());
            TextView myListingCity = (TextView) view.findViewById(R.id.listImgCity);
            myListingCity.setText(city);


            return view;
        }
    }


    private void actionBarSettings(Toolbar myToolbar) {
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#FFFFFF'>Office Me </font>"));
    }



    public void setUpTabs() {
        TabHost host = (TabHost) findViewById(R.id.tabHost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Home");
        spec.setContent(R.id.tab1);
        spec.setIndicator("", getResources().getDrawable(R.drawable.ic_home_white_24dp));
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Search");
        spec.setContent(R.id.tab2);
        spec.setIndicator("", getResources().getDrawable(R.drawable.ic_search_white_24dp));
        host.addTab(spec);

        //Tab 3
        spec = host.newTabSpec("Nearby");
        spec.setContent(R.id.tab3);
        spec.setIndicator("", getResources().getDrawable(R.drawable.ic_place_white_24dp));
        host.addTab(spec);

//        //Tab 4
//        spec = host.newTabSpec("Messages");
//        spec.setContent(R.id.tab4);
//        spec.setIndicator("", getResources().getDrawable(R.drawable.ic_mail_outline_white_24dp));
//        host.addTab(spec);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.drawerToggleIc:
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);
                drawer.openDrawer(Gravity.RIGHT);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.appbar, menu);
        return true;
    }

    private String getCity(int listingID) {
        String city = null;

        for(int i=0; i<allAddresses.size(); i++) {
            if(listingID == allAddresses.get(i).getAddressListingID()) {
                city = allAddresses.get(i).getAddressCity();
                break;
            }
        }
        return city;
    }


    private Bitmap getImage(int listingID) {
        Log.d("ListingID", Integer.toString(listingID));
        Bitmap currentImage = null;
        for(int i=0; i<allPicturesList.size(); i++) {
            if(allPicturesList.get(i).getPictureListingID() == listingID) {
                currentImage = allPicturesList.get(i).getListingImage();
                break;
            }
        }
        return currentImage;
    }

    private String getDeskType(int deskTypeID) {
        String officeType = null;
        for(int i=0; i<allDeskTypes.size(); i++) {
            if(allDeskTypes.get(i).getDeskTypeID() == deskTypeID) {
                officeType = allDeskTypes.get(i).getDeskType();
                break;
            }
        }
        return officeType;
    }

    private class deskPriceListener implements SeekBar.OnSeekBarChangeListener {

        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            // Log the progress
            Log.d("DEBUG", "Progress is: "+progress);
            //set textView's text
//            currentDesksAvailableValue.setText(""+progress);
            if(progress < 400) {
                currentDeskPriceValue.setText("€"+(progress+100));
            } else currentDeskPriceValue.setText("€500+");
        }

        public void onStartTrackingTouch(SeekBar seekBar) {}

        public void onStopTrackingTouch(SeekBar seekBar) {}

    }

    private class desksAvailableListener implements SeekBar.OnSeekBarChangeListener {

        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            // Log the progress
            Log.d("DEBUG", "Progress is: "+progress);
            //set textView's text
            if(progress < 9) {
                currentDesksAvailableValue.setText(""+(progress+1));
            } else currentDesksAvailableValue.setText("10+");

        }

        public void onStartTrackingTouch(SeekBar seekBar) {}

        public void onStopTrackingTouch(SeekBar seekBar) {}

    }

    private void callMap() {
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if(markerListings != null) {
            markerListings.clear();
        }
        if(markers != null) {
            markers.clear();
        }


        mMap = googleMap;


        LatLng ireland = new LatLng(53.344104, -6.2674937);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ireland, 14));

        Geocoder gc = new Geocoder(this);
        List<android.location.Address> list = null;
        for (int i = 0; i < finalFilteredList.size(); i++) {
            String address = getAddressString(finalFilteredList.get(i).getListingID());
            try {
                list = gc.getFromLocationName(address, 1);
                android.location.Address add = list.get(0);
                double lat = add.getLatitude();
                double lng = add.getLongitude();
                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(lat, lng))
                        .title(this.finalFilteredList.get(i).getListingTitle().toString())
                        .snippet("Price: €" + Integer.toString(this.finalFilteredList.get(i).getListingPrice())
                                + "\n" + "Desks Available: " + Integer.toString(this.finalFilteredList.get(i).getDesksAvailable())
                                + "\n" + "Desk Type: " + getDeskType(this.finalFilteredList.get(i).getDeskTypeID())));

                MarkerListing ml = new MarkerListing(marker.getId(), finalFilteredList.get(i).getListingID());
                markerListings.add(ml);
                markers.add(marker);

            } catch (IOException e) {
                e.printStackTrace();
            }

            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                Marker lastClicked;
                @Override
                public boolean onMarkerClick(Marker marker) {
                    if (lastClicked != null && lastClicked.equals(marker)) {
                        lastClicked = null;
                        marker.hideInfoWindow();
                        return true;
                    } else {
                        lastClicked = marker;
                        return false;
                    }
                }
            });

            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                @Override
                public View getInfoWindow(Marker arg0) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {

                    LinearLayout info = new LinearLayout(getApplicationContext());
                    info.setOrientation(LinearLayout.VERTICAL);

                    TextView title = new TextView(getApplicationContext());
                    title.setTextColor(Color.BLACK);
                    title.setGravity(Gravity.CENTER);
                    title.setTypeface(null, Typeface.BOLD);
                    title.setText(marker.getTitle());

                    TextView snippet = new TextView(getApplicationContext());
                    snippet.setTextColor(Color.GRAY);
                    snippet.setText(marker.getSnippet());

                    info.addView(title);
                    info.addView(snippet);

                    mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker marker) {
                            Intent n = new Intent(MainActivity.this, DetailedListing.class);
                            int listingID = -1;
                            for (int i = 0; i < markerListings.size(); i++) {
                                if (marker.getId().equalsIgnoreCase(markerListings.get(i).getMarkerID())) {
                                    listingID = markerListings.get(i).getListingID();
                                }
                            }
                            n.putExtra("Listing ID", listingID);
                            if (listingID != -1) {
                                startActivity(n);
                            }

                        }
                    });

                    return info;
                }
            });


        }

    }

    private String getAddressString(int listingID) {
        String address = null;

        for(int i=0; i<allAddresses.size(); i++) {
            if(listingID == allAddresses.get(i).getAddressListingID()) {
                String street1 = allAddresses.get(i).getAddressStreet1();
                String street2 = allAddresses.get(i).getAddressStreet2();
                String city = allAddresses.get(i).getAddressCity();
                String county = allAddresses.get(i).getAddressCounty();
                String country = allAddresses.get(i).getAddressCountry();

                address = (street1 + ", " + street2 + ", " + city + ", " + county + ", " + country);
            }
        }

        return address;
    }


}


