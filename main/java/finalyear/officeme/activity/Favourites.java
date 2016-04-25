package finalyear.officeme.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;

import finalyear.officeme.DatabaseHandler.Config;
import finalyear.officeme.DatabaseHandler.RequestHandler;
import finalyear.officeme.R;
import finalyear.officeme.Singletons.AddressSingleton;
import finalyear.officeme.Singletons.DeskTypeSingleton;
import finalyear.officeme.Singletons.ListingsSingleton;
import finalyear.officeme.Singletons.PicturesSingleton;
import finalyear.officeme.Singletons.UserID;
import finalyear.officeme.UpdateFavouritesList;
import finalyear.officeme.model.Address;
import finalyear.officeme.model.DeskType;
import finalyear.officeme.model.Favourite;
import finalyear.officeme.model.Listing;
import finalyear.officeme.model.Picture;

public class Favourites extends AppCompatActivity implements UpdateFavouritesList {

    //PROBLEM IN EITHER ON POST EXECUTE (NOFIFY DATA CHANGE) OR THE INNER FOR LOOP SORTING OUT THE LISTS

    String ALL_FAVOURITES_JSON_STRING;
    ArrayList<Integer> favouritesIDs;
    ArrayList<Listing> favouriteListings;
    PopulateFavourites pf = new PopulateFavourites();
    ArrayList<Listing> allListingsList;
    ArrayList<Picture> allPicturesList;
    ArrayList<DeskType> allDeskTypes;
    ArrayList<Address> allAddresses;
    ListView favouriteListingsListView;
    ArrayAdapter favouriteListingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        favouritesIDs = new ArrayList<>();
        allListingsList = new ArrayList<>();
        allListingsList.addAll(ListingsSingleton.getInstance().getListings());
        favouriteListings = new ArrayList<>();
        allPicturesList = new ArrayList<>();
        allPicturesList.addAll(PicturesSingleton.getInstance().getPictures());
        allDeskTypes = new ArrayList<>();
        allDeskTypes.addAll(DeskTypeSingleton.getInstance().getDeskTypes());
        allAddresses = new ArrayList<>();
        allAddresses.addAll(AddressSingleton.getInstance().getAddresses());
        favouriteListingsListView = (ListView) findViewById(R.id.favouriteListView);
        pf.delegate = this;
        pf.execute();
//        fillFavouriteListings();
//        populateFavouriteListingsList();

        favouriteListingsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent n = new Intent(Favourites.this, DetailedListing.class);
                Listing listing = favouriteListings.get(position);
                n.putExtra("Listing ID", listing.getListingID());
                startActivity(n);
            }
        });
    }

    private void populateFavouriteListingsList() {
//        HashSet hs = new HashSet();
//        hs.addAll(favouriteListings);
//        favouriteListings.clear();
//        favouriteListings.addAll(hs);
        favouriteListingAdapter = new FavouriteListingsListAdapter();
        favouriteListingsListView.setAdapter(favouriteListingAdapter);

    }

    @Override
    public void notifyListChange() {
        fillFavouriteListings();
        populateFavouriteListingsList();
        Log.d("Gets to Here", "");
    }

    private class FavouriteListingsListAdapter extends ArrayAdapter<Listing> {
        public FavouriteListingsListAdapter() {
            super(Favourites.this, R.layout.listview_listings, favouriteListings);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if(view == null)
                view = getLayoutInflater().inflate(R.layout.listview_listings, parent, false);

            Listing currentListing= favouriteListings.get(position);

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

    private void fillFavouriteListings() {
        if(!favouriteListings.isEmpty()) {
            favouriteListings.clear();
        }
        for(int j=0; j<favouritesIDs.size(); j++) {
            for(int i=0; i<allListingsList.size(); i++) {
                if(allListingsList.get(i).getListingID() == favouritesIDs.get(j)) {
                    int listingID = allListingsList.get(i).getListingID();
                    int userID = allListingsList.get(i).getListingUserID();
                    int deskTypeID = allListingsList.get(i).getDeskTypeID();
                    int desksAvailable = allListingsList.get(i).getDesksAvailable();
                    int listingPrice = allListingsList.get(i).getDesksAvailable();
                    String listingTitle = allListingsList.get(i).getListingTitle();
                    String listingDescription = allListingsList.get(i).getListingDescription();

                    Listing listing = new Listing(listingID, userID, deskTypeID, desksAvailable, listingPrice, listingTitle, listingDescription);
                    favouriteListings.add(listing);
                }
            }
        }
    }

    class PopulateFavourites extends AsyncTask<Void,Void,String> {

        public UpdateFavouritesList delegate = null;

        @Override
        protected void onPostExecute(String s) {
            delegate.notifyListChange();
        }

        @Override
        protected String doInBackground(Void... params) {
            RequestHandler rh = new RequestHandler();
            String stringAllFavourites = rh.sendGetRequest(Config.URL_GET_ALL_FAVOURITES + Integer.toString(UserID.getInstance().getLoggedInId()));

            if(stringAllFavourites !=null) {
                ALL_FAVOURITES_JSON_STRING = stringAllFavourites;
                addToFavouritesList();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        //stuff that updates ui
                        delegate.notifyListChange();
                    }
                });


            }
            return null;
        }
    }

    private void addToFavouritesList() {
        JSONObject jsonObject = null;

        if(!favouritesIDs.isEmpty()) {
            favouritesIDs.clear();
        }

        try {
            jsonObject = new JSONObject(ALL_FAVOURITES_JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_FAVOURITE_ARRAY);

            for(int i=0; i<result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                int id = Integer.parseInt(jo.getString(Config.TAG_FAVOURITE_LISTING_ID));
                favouritesIDs.add(id);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("Favourites List Size: ", Integer.toString(favouritesIDs.size()));
        //This works fine
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
}
