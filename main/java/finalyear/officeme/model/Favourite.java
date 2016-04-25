package finalyear.officeme.model;

/**
 * Created by College on 16/04/2016.
 */
public class Favourite {

    int favouriteUserId, favouriteListingId;

    public Favourite(int favouriteUserId, int favouriteListingId) {
        this.favouriteUserId = favouriteUserId;
        this.favouriteListingId = favouriteListingId;
    }

    public int getFavouriteUserId() {
        return favouriteUserId;
    }

    public void setFavouriteUserId(int favouriteUserId) {
        this.favouriteUserId = favouriteUserId;
    }

    public int getFavouriteListingId() {
        return favouriteListingId;
    }

    public void setFavouriteListingId(int favouriteListingId) {
        this.favouriteListingId = favouriteListingId;
    }
}
