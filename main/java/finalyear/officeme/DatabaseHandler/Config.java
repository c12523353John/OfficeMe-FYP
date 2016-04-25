package finalyear.officeme.DatabaseHandler;

/**
 * Created by College on 09/02/2016.
 */
public class Config {

    //Address of our scripts of the User CRUD
    public static final String URL_ADD_USER="http://www.johnrockfinalyearproject.com/addUser.php";
    public static final String URL_GET_ALL_USERS = "http://www.johnrockfinalyearproject.com/getAllUsers.php";
    public static final String URL_GET_USER = "http://www.johnrockfinalyearproject.com/getUser.php?id=";
    public static final String URL_UPDATE_USER = "http://www.johnrockfinalyearproject.com/updateUser.php";
    public static final String URL_DELETE_USER = "http://www.johnrockfinalyearproject.com/deleteUser.php?id=";

    public static final String URL_ADD_LISTING = "http://www.johnrockfinalyearproject.com/addListing.php";
    public static final String URL__GET_ALL_LISTINGS = "http://www.johnrockfinalyearproject.com/getAllListings.php";
    public static final String URL_GET_LISTING = "http://www.johnrockfinalyearproject.com/getListing.php?id=";
    public static final String URL__GET_ALL_LISTINGS_IMAGES = "http://www.johnrockfinalyearproject.com/getAllListingImages.php?id="; //pass in listingID
    public static final String URL_UPDATE_LISTING = "http://www.johnrockfinalyearproject.com/updateListing.php";
    public static final String URL_DELETE_LISTING = "http://www.johnrockfinalyearproject.com/deleteListing.php?id=";

    public static final String URL_ADD_PICTURE = "http://www.johnrockfinalyearproject.com/addPicture.php";
    public static final String URL_GET_ALL_PICTURES = "http://www.johnrockfinalyearproject.com/getAllPictures.php";
    public static final String URL_GET_PICTURE = "http://www.johnrockfinalyearproject.com/getPicture.php?id="; //pass in picture id
    public static final String URL_UPDATE_PICTURE = "http://www.johnrockfinalyearproject.com/updatePicture.php";
    public static final String URL_DELETE_PICTURE = "http://www.johnrockfinalyearproject.com/deletePicture.php?id=";

    public static final String URL_ADD_ADDRESS = "http://www.johnrockfinalyearproject.com/addAddress.php";
    public static final String URL_GET_ALL_ADDRESSES = "http://www.johnrockfinalyearproject.com/getAllAddresses.php";
    public static final String URL_GET_ADDRESS = "http://www.johnrockfinalyearproject.com/getPicture.php?id=";
    public static final String URL_UPDATE_ADDRESS = "http://www.johnrockfinalyearproject.com/updateAddress.php";
    public static final String URL_DELETE_ADDRESS = "http://www.johnrockfinalyearproject.com/deleteAddress.php?id=";

    public static final String URL_GET_ALL_DESK_TYPES = "http://www.johnrockfinalyearproject.com/getAllDeskTypes.php";

    public static final String URL_ADD_FAVOURITE = "http://johnrockfinalyearproject.com/addFavourite.php";
    public static final String URL_DELETE_FAVOURITE = "http://www.johnrockfinalyearproject.com/deleteUser.php?favouriteUserID=";
    public static final String URL_GET_ALL_FAVOURITES = "http://johnrockfinalyearproject.com/getUserFavourites.php?favouriteUserID=";

    public static final String URL_ADD_VIEWING_REQUEST = "http://johnrockfinalyearproject.com/addViewingRequest.php";
    public static final String URL_ADD_BOOKING_CONFIRMATION = "http://johnrockfinalyearproject.com/addBookingConfirmation.php";


    //Keys that will be used to send the request to php scripts
    public static final String KEY_USER_ID = "id";
    public static final String KEY_USER_NAME = "name";
    public static final String KEY_USER_EMAIL = "email";
    public static final String KEY_USER_PASSWORD = "password";
    public static final String KEY_USER_PHONE_NUMBER = "phoneNumber";


    public static final String KEY_LISTING_ID = "listingID";
    public static final String KEY_LISTING_USER_ID = "userID";
    public static final String KEY_LISTING_DESK_TYPE_ID = "deskTypeID";
    public static final String KEY_LISTING_DESKS_AVAILABLE = "desksAvailable";
    public static final String KEY_LISTING_LISTING_PRICE = "listingPrice";
    public static final String KEY_LISTING_LISTING_TITLE = "listingTitle";
    public static final String KEY_LISTING_LISTING_DESCRIPTION  = "listingDescription";

    public static final String KEY_PICTURE_ID = "pictureID";
    public static final String KEY_PICTURE_LISTING_ID = "listingID";
    public static final String KEY_PICTURE_LISTING_IMAGE = "listingImage";

    public static final String KEY_ADDRESS_ID = "addressID";
    public static final String KEY_ADDRESS_LISTING_ID = "listingID";
    public static final String KEY_ADDRESS_STREET1 = "street1";
    public static final String KEY_ADDRESS_STREET2 = "street2";
    public static final String KEY_ADDRESS_CITY = "city";
    public static final String KEY_ADDRESS_COUNTY = "county";
    public static final String KEY_ADDRESS_COUNTRY = "country";

    public static final String KEY_DESK_TYPE_ID = "deskType";
    public static final String KEY_DESK_TYPE_DESK_TYPE = "deskType";

    public static final String KEY_VIEWING_REQUEST_DAY = "day";
    public static final String KEY_VIEWING_REQUEST_MONTH = "month";
    public static final String KEY_VIEWING_REQUEST_YEAR = "year";
    public static final String KEY_VIEWING_REQUEST_HOUR = "hour";
    public static final String KEY_VIEWING_REQUEST_MINUTE = "minute";
    public static final String KEY_VIEWING_REQUEST_HOST_ID = "hostID";
    public static final String KEY_VIEWING_REQUEST_REQUEST_USER_ID = "requestUserID";
    public static final String KEY_VIEWING_REQUEST_LISTING_ID = "listingID";


    //JSON Tags
    public static final String TAG_JSON_ARRAY="result";
    public static final String TAG_ID = "user_id";
    public static final String TAG_NAME = "name";
    public static final String TAG_EMAIL = "email";
    public static final String TAG_PASSWORD = "password";
    public static final String TAG_PHONE_NUMBER = "phoneNumber";

    public static final String TAG_JSON_LISTING_ARRAY="result";
    public static final String TAG_LISTING_ID = "listingID";
    public static final String TAG_LISTING_USER_ID = "userID";
    public static final String TAG_LISTING_DESK_TYPE_ID = "deskTypeID";
    public static final String TAG_LISTING_DESKS_AVAILABLE = "desksAvailable";
    public static final String TAG_LISTING_PRICE = "listingPrice";
    public static final String TAG_LISTING_TITLE = "listingTitle";
    public static final String TAG_LISTING_DESCRIPTION = "listingDescription";

    public static final String TAG_JSON_PICTURE_ARRAY="result";
    public static final String TAG_PICTURE_ID = "pictureID";
    public static final String TAG_PICTURE_LISTING_ID = "listingID";
    public static final String TAG_PICTURE_LISTING_PATH= "path";


    public static final String TAG_JSON_ADDRESS_ARRAY="result";
    public static final String TAG_ADDRESS_ID = "addressID";
    public static final String TAG_ADDRESS_LISTING_ID = "listingID";
    public static final String TAG_ADDRESS_STREET1 = "street1";
    public static final String TAG_ADDRESS_STREET2 = "street2";
    public static final String TAG_ADDRESS_CITY = "city";
    public static final String TAG_ADDRESS_COUNTY = "county";
    public static final String TAG_ADDRESS_COUNTRY = "country";

    public static final String TAG_JSON_DESK_TYPE_ARRAY="result";
    public static final String TAG_DESK_TYPE_ID = "deskTypeID";
    public static final String TAG_DESK_TYPE_DESK_TYPE = "deskType";

    public static final String TAG_JSON_FAVOURITE_ARRAY="result";
    public static final String TAG_FAVOURITE_ID = "favouriteID";
    public static final String TAG_FAVOURITE_USER_ID = "favouriteUserID";
    public static final String TAG_FAVOURITE_LISTING_ID = "favouriteListingID";

    public static final String TAG_JSON_VIEWING_REQUEST_ARRAY="result";
    public static final String TAG_VIEWING_REQUEST_DAY = "day";
    public static final String TAG_VIEWING_REQUEST_MONTH = "month";
    public static final String TAG_VIEWING_REQUEST_YEAR = "year";
    public static final String TAG_VIEWING_REQUEST_HOUR = "hour";
    public static final String TAG_VIEWING_REQUEST_MINUTE = "minute";
    public static final String TAG_VIEWING_REQUEST_HOST_ID = "hostID";
    public static final String TAG_VIEWING_REQUEST_REQUEST_USER_ID = "requestUserID";
    public static final String TAG_VIEWING_REQUEST_LISTING_ID = "listingID";

}