package finalyear.officeme.model;

import android.graphics.Bitmap;

/**
 * Created by College on 10/04/2016.
 */
public class Picture {

    int pictureID, pictureListingID;
    Bitmap listingImage;

    public Picture(int pictureID, int pictureListingID, Bitmap listingImage) {
        this.pictureID = pictureID;
        this.pictureListingID = pictureListingID;
        this.listingImage = listingImage;
    }

    public int getPictureID() {
        return pictureID;
    }

    public void setPictureID(int pictureID) {
        this.pictureID = pictureID;
    }

    public int getPictureListingID() {
        return pictureListingID;
    }

    public void setPictureListingID(int pictureListingID) {
        this.pictureListingID = pictureListingID;
    }

    public Bitmap getListingImage() {
        return listingImage;
    }

    public void setListingImage(Bitmap listingImage) {
        this.listingImage = listingImage;
    }
}
