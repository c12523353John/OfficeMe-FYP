package finalyear.officeme.Singletons;

import java.util.ArrayList;

import finalyear.officeme.model.Listing;

/**
 * Created by College on 11/04/2016.
 */
public class ListingsSingleton {

    private static ListingsSingleton instance = null;




    ArrayList<Listing> listings = new ArrayList<>();

    protected ListingsSingleton() {

    }

    public static ListingsSingleton getInstance() {
        if(instance == null) {
            instance = new ListingsSingleton();
        }
        return instance;
    }

    public ArrayList<Listing> getListings() {
        return listings;
    }

    public void setListings(ArrayList<Listing> listings) {
        this.listings = listings;
    }



}
