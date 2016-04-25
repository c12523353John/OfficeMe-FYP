package finalyear.officeme.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import finalyear.officeme.DatabaseHandler.Config;
import finalyear.officeme.DatabaseHandler.RequestHandler;
import finalyear.officeme.R;
import finalyear.officeme.Singletons.ListingsSingleton;
import finalyear.officeme.Singletons.UserList;
import finalyear.officeme.model.BookingRequest;

public class VisitorDetailedBookingRequest extends AppCompatActivity {

    BookingRequest bookingRequest;
    TextView etTitle, etDate, etTime, etCallHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_booking_request);
        setBookingDetails();
        final String title = getTitle(bookingRequest.getBookingRequestListingID());
        String hostName = getHostName(bookingRequest.getBookingRequestHostID());

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
        etCallHost = (TextView) findViewById(R.id.detailedBookingRequestCallContactTextView);
        etCallHost.setText("  Call the host: " + hostName);


        final Button confirmRequestButton = (Button) findViewById(R.id.detailedBookingRequestConfirmRequestButton);
        confirmRequestButton.setVisibility(View.INVISIBLE);
        final ImageButton callContactButton = (ImageButton) findViewById(R.id.detailedBookingRequestCallContactButton);

        final Button cancelRequestButton = (Button) findViewById(R.id.detailedBookingRequestCancelRequestButton);
        LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams)cancelRequestButton.getLayoutParams();
//        ll.gravity = Gravity.CENTER_HORIZONTAL;
        ll.leftMargin = 500;
        cancelRequestButton.setLayoutParams(ll);



        callContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + getHostPhone(bookingRequest.getBookingRequestHostID())));
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

                removeBooking(bookingRequest.getBookingRequestID());


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
            Intent i = new Intent(VisitorDetailedBookingRequest.this, VisitorBookingInformation.class);
            startActivity(i);
        }

    }

    private String getHostPhone(int userID) {
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


    private String getHostName(int bookingRequestHostID) {
        String user = null;
        for(int i=0; i< UserList.getInstance().getUserList().size(); i++) {
            if(bookingRequestHostID == UserList.getInstance().getUserList().get(i).getId()) {
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

    private void removeBooking(final int bookingRequestID) {


        class RemoveBookingAsync extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }

            @Override
            protected String doInBackground(Void... v) {
                RequestHandler rh = new RequestHandler();
                String res = rh.sendGetRequest("http://johnrockfinalyearproject.com/deleteBookingRequest.php?viewingRequestID=" + Integer.toString(bookingRequest.getBookingRequestID()));
                Log.d("Cancel Request: ", "http://johnrockfinalyearproject.com/deleteConfirmedBooking.php?confirmedViewingsID=" + Integer.toString(bookingRequestID));
                return res;
            }
        }

        RemoveBookingAsync rb = new RemoveBookingAsync();
        rb.execute();
    }

}
