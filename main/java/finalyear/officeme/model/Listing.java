package finalyear.officeme.model;

/**
 * Created by College on 10/04/2016.
 */
public class Listing {

    int listingID, listingUserID, deskTypeID, desksAvailable, listingPrice;
    String listingTitle, listingDescription;

    public Listing(int listingID, int listingUserID, int deskTypeID, int desksAvailable, int listingPrice, String listingTitle, String listingDescription) {
        this.listingID = listingID;
        this.listingUserID = listingUserID;
        this.deskTypeID = deskTypeID;
        this.desksAvailable = desksAvailable;
        this.listingPrice = listingPrice;
        this.listingTitle = listingTitle;
        this.listingDescription = listingDescription;
    }

    public int getListingID() {
        return listingID;
    }

    public void setListingID(int listingID) {
        this.listingID = listingID;
    }

    public int getListingUserID() {
        return listingUserID;
    }

    public void setListingUserID(int listingUserID) {
        this.listingUserID = listingUserID;
    }

    public int getDeskTypeID() {
        return deskTypeID;
    }

    public void setDeskTypeID(int deskTypeID) {
        this.deskTypeID = deskTypeID;
    }

    public int getDesksAvailable() {
        return desksAvailable;
    }

    public void setDesksAvailable(int desksAvailable) {
        this.desksAvailable = desksAvailable;
    }

    public String getListingTitle() {
        return listingTitle;
    }

    public void setListingTitle(String listingTitle) {
        this.listingTitle = listingTitle;
    }

    public String getListingDescription() {
        return listingDescription;
    }

    public void setListingDescription(String listingDescription) {
        this.listingDescription = listingDescription;
    }

    public int getListingPrice() {
        return listingPrice;
    }

    public void setListingPrice(int listingPrice) {
        this.listingPrice = listingPrice;
    }
}
