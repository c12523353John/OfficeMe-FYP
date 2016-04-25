package finalyear.officeme.model;

/**
 * Created by College on 10/04/2016.
 */
public class Address {

    int addressID, addressListingID;
    String addressStreet1, addressStreet2, addressCounty, addressCity, addressCountry;

    public Address(int addressID, int addressListingID, String addressStreet1, String addressStreet2, String addressCity,  String addressCounty, String addressCountry) {
        this.addressID = addressID;
        this.addressListingID = addressListingID;
        this.addressStreet1 = addressStreet1;
        this.addressStreet2 = addressStreet2;
        this.addressCity = addressCity;
        this.addressCounty = addressCounty;
        this.addressCountry = addressCountry;
    }

    public int getAddressID() {
        return addressID;
    }

    public void setAddressID(int addressID) {
        this.addressID = addressID;
    }

    public int getAddressListingID() {
        return addressListingID;
    }

    public void setAddressListingID(int addressListingID) {
        this.addressListingID = addressListingID;
    }

    public String getAddressStreet1() {
        return addressStreet1;
    }

    public void setAddressStreet1(String addressStreet1) {
        this.addressStreet1 = addressStreet1;
    }

    public String getAddressStreet2() {
        return addressStreet2;
    }

    public void setAddressStreet2(String addressStreet2) {
        this.addressStreet2 = addressStreet2;
    }

    public String getAddressCounty() {
        return addressCounty;
    }

    public void setAddressCounty(String addressCounty) {
        this.addressCounty = addressCounty;
    }

    public String getAddressCountry() {
        return addressCountry;
    }

    public void setAddressCountry(String addressCountry) {
        this.addressCountry = addressCountry;
    }

    public String getAddressCity() {
        return addressCity;
    }

    public void setAddressCity(String addressCity) {
        this.addressCity = addressCity;
    }
}
