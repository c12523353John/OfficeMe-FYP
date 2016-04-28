package finalyear.officeme.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import finalyear.officeme.Login;
import finalyear.officeme.R;
import finalyear.officeme.Singletons.AddressSingleton;
import finalyear.officeme.Singletons.DeskTypeSingleton;
import finalyear.officeme.Singletons.ListingsSingleton;
import finalyear.officeme.Singletons.LoggedIn;
import finalyear.officeme.Singletons.LoggedInUserDetails;
import finalyear.officeme.Singletons.PicturesSingleton;
import finalyear.officeme.Singletons.UserID;
import finalyear.officeme.model.Address;
import finalyear.officeme.model.DeskType;
import finalyear.officeme.model.Listing;
import finalyear.officeme.model.Picture;
import finalyear.officeme.model.User;
import finalyear.officeme.parse.ParseActivity;

public class HostingActivity extends AppCompatActivity implements View.OnClickListener {

    Button loginActivityButton;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    //My Listings Variables
    ListView myListingsListView;
    ArrayAdapter myListingAdapter;
    RatingBar myListingAverageRating;
    Button myListingFavouriteButton;
    ArrayList<Listing> myListingsList;
    ArrayList<Listing> allListingList;
    ArrayList<Picture> allPicturesList;
    ArrayList<DeskType> allDeskTypes;
    ArrayList<Address> allAddresses;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hosting);

        //Initialising toolbar and setting it as the the actionbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setBackgroundColor(getResources().getColor(R.color.hostingPrimary));
        actionBarSettings(myToolbar);

        //PopulateLists
        allListingList = new ArrayList<>();
        allListingList.addAll(ListingsSingleton.getInstance().getListings());
        myListingsList = new ArrayList<>();
        allPicturesList = new ArrayList<>();
        allPicturesList.addAll(PicturesSingleton.getInstance().getPictures());
        allDeskTypes = new ArrayList<>();
        allDeskTypes.addAll(DeskTypeSingleton.getInstance().getDeskTypes());
        allAddresses = new ArrayList<>();
        allAddresses.addAll(AddressSingleton.getInstance().getAddresses());

        Log.d("All Pic Single size", Integer.toString(PicturesSingleton.getInstance().getPictures().size()));
        Log.d("All pic size", Integer.toString(allPicturesList.size()));
        myListingsListView = (ListView) findViewById(R.id.myListingsListView);

        //Initialising tabs
        setUpTabs();


        //Initialising NavigationView
        navigationView = (NavigationView) findViewById(R.id.navigation_host_view);


        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {


                //Checking if the item is in checked state or not, if not make it in checked state
                if(menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(false);

                //Closing drawer on item click
                //drawerLayout.closeDrawers();

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()){

                    case R.id.idSwitchToVisitor:
                        //drawerLayout.closeDrawers();
                        Intent in = new Intent(HostingActivity.this, MainActivity.class);
                        startActivity(in);
                        return true;
                    case R.id.listSpace:
                        Intent i = new Intent(HostingActivity.this, ListSpace.class);
                        startActivity(i);
                        return true;
                    case R.id.idHostingLogOut:
                        Toast.makeText(getApplicationContext(), "Successfully Logged Out", Toast.LENGTH_SHORT).show();
                        LoggedIn.getInstance().setLoggedIn(false);
                        Intent j = new Intent(HostingActivity.this, SplashActivity.class);
                        startActivity(j);
                        return true;
                    case R.id.spaceViewings:
                        Intent viewings = new Intent(HostingActivity.this, ViewingInformation.class);
                        startActivity(viewings);
                        return true;
//                    case R.id.trash:
//                        Toast.makeText(getApplicationContext(),"Trash Selected",Toast.LENGTH_SHORT).show();
//                        return true;
//                    case R.id.spam:
//                        Toast.makeText(getApplicationContext(),"Spam Selected",Toast.LENGTH_SHORT).show();
//                        return true;
                    default:
                        Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                        return true;

                }
            }
        });


//        myListingDesksAvailable = (TextView) findViewById(R.id.listImgDesksAvailable);
//        myListingCity = (TextView) findViewById(R.id.listImgCity);
        myListingAverageRating = (RatingBar) findViewById(R.id.listImgAverageRating);


        if(!allListingList.isEmpty()) {
            for(int i=0; i<allListingList.size(); i++){
                if(allListingList.get(i).getListingUserID() == UserID.getInstance().getLoggedInId()) {
                    int listingID = allListingList.get(i).getListingID();
                    int listingUserID = allListingList.get(i).getListingUserID();
                    int deskTypeID = allListingList.get(i).getDeskTypeID();
                    int desksAvailable = allListingList.get(i).getDesksAvailable();
                    int listingPrice = allListingList.get(i).getListingPrice();
                    String listingTitle = allListingList.get(i).getListingTitle();
                    String listingDescription = allListingList.get(i).getListingDescription();

                    Listing l = new Listing(listingID, listingUserID, deskTypeID, desksAvailable, listingPrice, listingTitle, listingDescription);
                    myListingsList.add(l);

                }
            }

            populateMyListingsList();
        }

    }

    private void populateMyListingsList() {
        myListingAdapter = new MyListingsListAdapter();
        myListingsListView.setAdapter(myListingAdapter);
    }

    private class MyListingsListAdapter extends ArrayAdapter<Listing> {
        public MyListingsListAdapter() {
            super(HostingActivity.this, R.layout.listview_listings, myListingsList);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if(view == null)
                view = getLayoutInflater().inflate(R.layout.listview_listings, parent, false);

            Listing currentListing= myListingsList.get(position);

            TextView myListingListingPrice = (TextView) view.findViewById(R.id.listImgPrice);
            myListingListingPrice.setText("€" + Integer.toString(currentListing.getListingPrice()) + " Per Desk Per Month");
            TextView myListingListingTitle = (TextView) view.findViewById(R.id.listImgTitle);
            myListingListingTitle.setText(currentListing.getListingTitle());
            TextView myListingDesksAvailable = (TextView) view.findViewById(R.id.listImgDesksAvailable);
            myListingDesksAvailable.setText(Integer.toString(currentListing.getDesksAvailable()) + " Desks Available");

//            //GetMainImage
//            Bitmap bitmap = getImage(currentListing.getListingID());
//            ImageView myListingsImageView = (ImageView) view.findViewById(R.id.listImgImgMain);
//            myListingsImageView.setImageBitmap(bitmap);
//            String bitmapcase;
//            if(bitmap == null) {
//                bitmapcase = "bitmap null";
//            } else bitmapcase = "bitmap not null";
//            Log.d("Is bitmap null?", bitmapcase);
            String url = "http://www.johnrockfinalyearproject.com/images/listingimage" + Integer.toString(currentListing.getListingID()) + "1";
            ImageView myListingsImageView = (ImageView) view.findViewById(R.id.listImgImgMain);
            UrlImageViewHelper.setUrlDrawable(myListingsImageView, url);

            String office = getDeskType(currentListing.getDeskTypeID());
            TextView myListingOfficeType = (TextView) view.findViewById(R.id.listImgOfficeType);
            myListingOfficeType.setText(office);
            
            String city = getCity(currentListing.getListingID());
            TextView myListingCity = (TextView) view.findViewById(R.id.listImgCity);
            myListingCity.setText(city);



            return view;
        }
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
        //change this to async that grabs one image as opposed to all of them
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



    private void actionBarSettings(Toolbar myToolbar) {
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#FFFFFF'>Office Me </font>"));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case  R.id.bLogin:
                startActivity(new Intent(this, Login.class));
                break;

        }
    }

    public void setUpTabs() {
        TabHost host = (TabHost) findViewById(R.id.tabHostHosting);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("My Listings");
        spec.setContent(R.id.tab1Hosting);
        spec.setIndicator("", getResources().getDrawable(R.drawable.ic_book_white_24dp));
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("My Listings");
        spec.setContent(R.id.tab2Hosting);
        spec.setIndicator("",getResources().getDrawable(R.drawable.ic_featured_play_list_white_24dp));
        host.addTab(spec);

        //Tab 3
        spec = host.newTabSpec("Bookings");
        spec.setContent(R.id.tab3Hosting);
        spec.setIndicator("", getResources().getDrawable(R.drawable.ic_date_range_white_24dp));
        host.addTab(spec);

//        //Tab 4
//        spec = host.newTabSpec("Messages");
//        spec.setContent(R.id.tab4Hosting);
//        spec.setIndicator("",getResources().getDrawable(R.drawable.ic_mail_outline_white_24dp));
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

    @Override
    public void onResume() {  // After a pause OR at startup
        super.onResume();
//        PopulateFavourites pf = new PopulateFavourites();
//        pf.delegate = this;
//        pf.execute();
//        if(favouriteListingAdapter !=null)
//            favouriteListingAdapter.notifyDataSetChanged();
//        allListingsListView.setAdapter(new CustomListAdapter(this, allListingsList));

        populateMyListingsList();

    }

}
