package finalyear.officeme.activity;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import finalyear.officeme.DatabaseHandler.Config;
import finalyear.officeme.DatabaseHandler.RequestHandler;
import finalyear.officeme.R;
import finalyear.officeme.Singletons.ListingsSingleton;
import finalyear.officeme.Singletons.UserList;
import finalyear.officeme.model.BookingRequest;

public class DetailedBookingRequest extends AppCompatActivity {

    BookingRequest bookingRequest;
    TextView etTitle, etDate, etTime, etCallUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_booking_request);
        setBookingDetails();
        final String title = getTitle(bookingRequest.getBookingRequestListingID());
        String requestedBy = getRequestedBy(bookingRequest.getBookingRequestVisitorID());

        etTitle = (TextView) findViewById(R.id.detailedBookingRequestTitle);
        etTitle.setText(title);
        etDate = (TextView) findViewById(R.id.detailedBookingRequestDate);
        etDate.setText("Date: " + bookingRequest.getBookingRequestDay() + "/" + bookingRequest.getBookingRequestMonth() + "/" + bookingRequest.getBookingRequestYear());
        String bookingMinute = Integer.toString(bookingRequest.getBookingRequestMinute());
        if(bookingRequest.getBookingRequestMinute() < 10) {
            bookingMinute = "0" + Integer.toString(bookingRequest.getBookingRequestMinute());
        }
        etTime = (TextView) findViewById(R.id.detailedBookingRequestTime);
        etTime.setText("Time: " + bookingRequest.getBookingRequestHour()+ ":" + bookingMinute);
        etCallUser = (TextView) findViewById(R.id.detailedBookingRequestCallContactTextView);
        etCallUser.setText("  Call " + requestedBy);


        final ImageButton callContactButton = (ImageButton) findViewById(R.id.detailedBookingRequestCallContactButton);
        final Button cancelRequestButton = (Button) findViewById(R.id.detailedBookingRequestCancelRequestButton);
        final Button confirmRequestButton = (Button) findViewById(R.id.detailedBookingRequestConfirmRequestButton);

        callContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + getRequestPhone(bookingRequest.getBookingRequestVisitorID())));
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for Activity#requestPermissions for more details.
                        return;
                    }
                }
                startActivity(callIntent);
            }
        });

        cancelRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RequestHandler rh = new RequestHandler();
                rh.sendGetRequest("http://johnrockfinalyearproject.com/deleteBookingRequest.php?viewingRequestID=" + Integer.toString(bookingRequest.getBookingRequestID()));

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + getRequestPhone(bookingRequest.getBookingRequestVisitorID())));
                intent.putExtra("exit_on_sent", true);
                intent.putExtra("sms_body", "Hi " + getRequestedBy(bookingRequest.getBookingRequestVisitorID()) + ", I won't be able to make the requested time for " + title);
                startActivityForResult(intent, 1);

            }
        });

        confirmRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmBookingRequest(bookingRequest);

                RequestHandler rh = new RequestHandler();
                rh.sendGetRequest("http://johnrockfinalyearproject.com/deleteBookingRequest.php?viewingRequestID=" + Integer.toString(bookingRequest.getBookingRequestID()));

                Intent i = new Intent(DetailedBookingRequest.this, ViewingInformation.class);
                startActivity(i);


            }
        });

    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
//        if (requestCode==1) Toast.makeText(this, "Message Sent", Toast.LENGTH_SHORT).show();
//

    }

    @Override
    public  void onActivityResult(int reqCode, int resCode, Intent data) {

        if(reqCode == 1) {

            Toast.makeText(this, "Message Sent", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(DetailedBookingRequest.this, ViewingInformation.class);
            startActivity(i);
        }

    }

    private String getRequestPhone(int userID) {
        String phone = null;
        for(int i=0; i < UserList.getInstance().getUserList().size(); i++) {
            if(userID == UserList.getInstance().getUserList().get(i).getId()) {
                phone = UserList.getInstance().getUserList().get(i).getPhoneNumber();
                break;

            }
        }
        return phone;
    }

    public void setBookingDetails() {
        Bundle extras = getIntent().getExtras();
        int bookingRequestID = extras.getInt("bookingRequestID");
        int bookingRequestDay = extras.getInt("bookingRequestDay");
        int bookingRequestMonth = extras.getInt("bookingRequestMonth");
        int bookingRequestYear = extras.getInt("bookingRequestYear");
        int bookingRequestHour = extras.getInt("bookingRequestHour");
        int bookingRequestMinute = extras.getInt("bookingRequestMinute");
        int bookingRequestVisitorID = extras.getInt("bookingRequestVisitorID");
        int bookingRequestHostID = extras.getInt("bookingRequestHostID");
        int bookingRequestListingID = extras.getInt("bookingRequestListingID");

        bookingRequest = new BookingRequest(bookingRequestID, bookingRequestDay, bookingRequestMonth, bookingRequestYear, bookingRequestHour, bookingRequestMinute, bookingRequestVisitorID, bookingRequestHostID, bookingRequestListingID);
    }

    private String getRequestedBy(int bookingRequestVisitorID) {
        String user = null;
        for(int i=0; i< UserList.getInstance().getUserList().size(); i++) {
            if(bookingRequestVisitorID == UserList.getInstance().getUserList().get(i).getId()) {
                user = UserList.getInstance().getUserList().get(i).get_name();
            }
        }

        return user;

    }

    private String getTitle(int bookingRequestListingID) {
        String title = null;

        for(int i=0; i< ListingsSingleton.getInstance().getListings().size(); i++) {
            if(bookingRequestListingID == ListingsSingleton.getInstance().getListings().get(i).getListingID()) {
                title = ListingsSingleton.getInstance().getListings().get(i).getListingTitle();
            }
        }
        return title;
    }

    private void confirmBookingRequest(BookingRequest br) {
        final int id = br.getBookingRequestID();
        final int day = br.getBookingRequestDay();
        final int month = br.getBookingRequestMonth();
        final int year = br.getBookingRequestYear();
        final int hour = br.getBookingRequestHour();
        final int minute = br.getBookingRequestMinute();
        final int hostID = br.getBookingRequestHostID();
        final int requestUserID = br.getBookingRequestVisitorID();
        final int listingID = br.getBookingRequestListingID();

        class ConfirmBooking extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Toast.makeText(DetailedBookingRequest.this, s, Toast.LENGTH_LONG).show();

                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                // your code here
                                Intent i = new Intent(DetailedBookingRequest.this, ViewingInformation.class);
                                    startActivity(i);
                            }
                        },
                        750
                );
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                params.put(Config.KEY_VIEWING_REQUEST_DAY, Integer.toString(day));
                params.put(Config.KEY_VIEWING_REQUEST_MONTH, Integer.toString(month));
                params.put(Config.KEY_VIEWING_REQUEST_YEAR, Integer.toString(year));
                params.put(Config.KEY_VIEWING_REQUEST_HOUR, Integer.toString(hour));
                params.put(Config.KEY_VIEWING_REQUEST_MINUTE, Integer.toString(minute));
                params.put(Config.KEY_VIEWING_REQUEST_HOST_ID, Integer.toString(hostID));
                params.put(Config.KEY_VIEWING_REQUEST_REQUEST_USER_ID, Integer.toString(requestUserID));
                params.put(Config.KEY_VIEWING_REQUEST_LISTING_ID, Integer.toString(listingID));
                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.URL_ADD_BOOKING_CONFIRMATION, params);

                rh.sendGetRequest(rh.sendGetRequest("http://johnrockfinalyearproject.com/deleteBookingRequest.php?viewingRequestID=" + Integer.toString(id)));
                return res;
            }
        }

        ConfirmBooking au = new ConfirmBooking();
        au.execute();

    }

}
