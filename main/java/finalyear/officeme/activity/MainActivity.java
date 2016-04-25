package finalyear.officeme.activity;




import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import finalyear.officeme.Login;
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

public class MainActivity extends AppCompatActivity  {

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



        //Initialising tabs
        setUpTabs();

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

        //Tab 4
        spec = host.newTabSpec("Messages");
        spec.setContent(R.id.tab4);
        spec.setIndicator("", getResources().getDrawable(R.drawable.ic_mail_outline_white_24dp));
        host.addTab(spec);

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

}


