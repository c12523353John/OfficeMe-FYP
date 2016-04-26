package finalyear.officeme;

/**
 * Created by College on 26/04/2016.
 */
public class MarkerListing {

    String markerID;
    int listingID;

    public MarkerListing(String markerID, int listingID) {
        this.markerID = markerID;
        this.listingID = listingID;
    }

    public String getMarkerID() {
        return markerID;
    }

    public void setMarkerID(String markerID) {
        this.markerID = markerID;
    }

    public int getListingID() {
        return listingID;
    }

    public void setListingID(int listingID) {
        this.listingID = listingID;
    }
}
