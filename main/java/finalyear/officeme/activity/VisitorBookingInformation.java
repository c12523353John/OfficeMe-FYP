package finalyear.officeme.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import finalyear.officeme.DatabaseHandler.RequestHandler;
import finalyear.officeme.R;
import finalyear.officeme.Singletons.ListingsSingleton;
import finalyear.officeme.Singletons.UserID;
import finalyear.officeme.Singletons.UserList;
import finalyear.officeme.model.BookingRequest;

public class VisitorBookingInformation extends AppCompatActivity implements FillBookingRequestListAsync, FillConfirmedBookingsListAsync  {
    String ALL_REQUESTS_JSON_STRING, ALL_CONFIRMATIONS_JSON_STRING;
    ArrayList<BookingRequest> allRequests, allConfirmations;
    GetBookingRequests gb = new GetBookingRequests();
    GetConfirmedBookings cb = new GetConfirmedBookings();
    ListView bookingRequestsListView, confirmedBookingsListView;
    ArrayAdapter bookingRequestsAdapter, confirmedBookingsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor_booking_information);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        setUpTabs();
        allRequests = new ArrayList<>();
        allConfirmations = new ArrayList<>();

        gb.delegate = this;
        gb.execute();
        cb.delegate = this;
        cb.execute();

        //Implement list and text views
        bookingRequestsListView = (ListView) findViewById(R.id.myVisitorViewingRequests);
        confirmedBookingsListView = (ListView) findViewById(R.id.myVisitorConfirmedViewings);

        bookingRequestsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent n = new Intent(VisitorBookingInformation.this, VisitorDetailedBookingRequest.class);
                BookingRequest bookingRequest = allRequests.get(position);
                n.putExtra("bookingRequestID", bookingRequest.getBookingRequestID());
                n.putExtra("bookingRequestDay", bookingRequest.getBookingRequestDay());
                n.putExtra("bookingRequestMonth", bookingRequest.getBookingRequestMonth());
                n.putExtra("bookingRequestYear", bookingRequest.getBookingRequestYear());
                n.putExtra("bookingRequestHour", bookingRequest.getBookingRequestHour());
                n.putExtra("bookingRequestMinute", bookingRequest.getBookingRequestMinute());
                n.putExtra("bookingRequestVisitorID", bookingRequest.getBookingRequestVisitorID());
                n.putExtra("bookingRequestHostID", bookingRequest.getBookingRequestHostID());
                n.putExtra("bookingRequestListingID", bookingRequest.getBookingRequestListingID());
                startActivity(n);

            }
        });

        confirmedBookingsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent bc = new Intent(VisitorBookingInformation.this, VisitorDetailedBookingConfirmation.class);
                BookingRequest bookingConfirmation = allConfirmations.get(position);
                bc.putExtra("bookingRequestID", bookingConfirmation.getBookingRequestID());
                bc.putExtra("bookingRequestDay", bookingConfirmation.getBookingRequestDay());
                bc.putExtra("bookingRequestMonth", bookingConfirmation.getBookingRequestMonth());
                bc.putExtra("bookingRequestYear", bookingConfirmation.getBookingRequestYear());
                bc.putExtra("bookingRequestHour", bookingConfirmation.getBookingRequestHour());
                bc.putExtra("bookingRequestMinute", bookingConfirmation.getBookingRequestMinute());
                bc.putExtra("bookingRequestVisitorID", bookingConfirmation.getBookingRequestVisitorID());
                bc.putExtra("bookingRequestHostID", bookingConfirmation.getBookingRequestHostID());
                bc.putExtra("bookingRequestListingID", bookingConfirmation.getBookingRequestListingID());
                startActivity(bc);
            }
        });






    }

    @Override
    public void onResume() {
        super.onResume();
        populateList();
        populateConfirmedList();

    }

    public void setUpTabs() {
        TabHost host = (TabHost) findViewById(R.id.tabVisitorViewings);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Booking Requests");
        spec.setContent(R.id.tab1VisitorViewing);
        spec.setIndicator("", getResources().getDrawable(R.drawable.ic_book_white_24dp));
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Confirmed Bookings");
        spec.setContent(R.id.tab2VisitorViewing);
        spec.setIndicator("", getResources().getDrawable(R.drawable.ic_featured_play_list_white_24dp));
        host.addTab(spec);


    }




    class GetBookingRequests extends AsyncTask<Void,Void,String> {

        //        ProgressDialog loading;
        public FillBookingRequestListAsync delegate = null;

//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//                loading = ProgressDialog.show(DetailedListing.this,"Checking...","Wait...",false,false);
//            }

        @Override
        protected void onPostExecute(String s) {
//                super.onPostExecute(s);
//                loading.dismiss();
//                Toast.makeText(DetailedListing.this, s, Toast.LENGTH_LONG).show();
            delegate.populateList();

        }

        @Override
        protected String doInBackground(Void... v) {
            RequestHandler rh = new RequestHandler();
//            String res = rh.sendGetRequest("http://johnrockfinalyearproject.com/checkFavourite.php?favouriteUserID=" + Integer.toString(UserID.getInstance().getLoggedInId()) + "&favouriteListingID="  + Integer.toString(getListingID()));
            String res = rh.sendGetRequest("http://johnrockfinalyearproject.com/getViewingRequestsByVisitorID.php?requestUserID=" + Integer.toString(UserID.getInstance().getLoggedInId()));
            Log.d("LoggedInID: ", Integer.toString(UserID.getInstance().getLoggedInId()));
            Log.d("Res: ", res);

            if(res!=null){
                ALL_REQUESTS_JSON_STRING = res;
                Log.d("JSONSTR:", ALL_REQUESTS_JSON_STRING);
                addRequestsToList();
//                UserList.getInstance().setUserList(userList);
//                Log.d("UserList Size: ", Integer.toString(UserList.getInstance().getUserList().size()));
            }

            return res;
        }
    }

    private void addRequestsToList() {
        JSONObject jsonObject = null;

        if(!allRequests.isEmpty()) {
            allRequests.clear();
        }

        try {
            jsonObject = new JSONObject(ALL_REQUESTS_JSON_STRING);
            JSONArray result = jsonObject.getJSONArray("result");

            for(int i=0; i<result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                int viewingRequestID = Integer.parseInt(jo.getString("viewingRequestID"));
                int day = Integer.parseInt(jo.getString("day"));
                int month = Integer.parseInt(jo.getString("month"));
                int year = Integer.parseInt(jo.getString("year"));
                int hour = Integer.parseInt(jo.getString("hour"));
                int minute = Integer.parseInt(jo.getString("minute"));
                int hostID = Integer.parseInt(jo.getString("hostID"));
                int requestUserID = Integer.parseInt(jo.getString("requestUserID"));
                int listingID = Integer.parseInt(jo.getString("listingID"));

                BookingRequest br = new BookingRequest(viewingRequestID, day, month, year, hour, minute, hostID, requestUserID, listingID);
                allRequests.add(br);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    class GetConfirmedBookings extends AsyncTask<Void,Void,String> {

        //        ProgressDialog loading;
        public FillConfirmedBookingsListAsync delegate = null;

//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//                loading = ProgressDialog.show(DetailedListing.this,"Checking...","Wait...",false,false);
//            }

        @Override
        protected void onPostExecute(String s) {
//                super.onPostExecute(s);
//                loading.dismiss();
//                Toast.makeText(DetailedListing.this, s, Toast.LENGTH_LONG).show();
            delegate.populateConfirmedList();

        }

        @Override
        protected String doInBackground(Void... v) {
            RequestHandler rh = new RequestHandler();
//            String res = rh.sendGetRequest("http://johnrockfinalyearproject.com/checkFavourite.php?favouriteUserID=" + Integer.toString(UserID.getInstance().getLoggedInId()) + "&favouriteListingID="  + Integer.toString(getListingID()));
            String res = rh.sendGetRequest("http://johnrockfinalyearproject.com/getConfirmedBookingsByVisitorID.php?requestUserID=" + Integer.toString(UserID.getInstance().getLoggedInId()));
            Log.d("LoggedInID: ",Integer.toString(UserID.getInstance().getLoggedInId()));
            Log.d("Res: ", res);

            if(res!=null){
                ALL_CONFIRMATIONS_JSON_STRING = res;
                Log.d("JSONSTR:", ALL_CONFIRMATIONS_JSON_STRING);
                addConfirmationsToList();
//                UserList.getInstance().setUserList(userList);
//                Log.d("UserList Size: ", Integer.toString(UserList.getInstance().getUserList().size()));
            }

            return res;
        }
    }

    private void addConfirmationsToList() {

        JSONObject jsonObject = null;

        if(!allConfirmations.isEmpty()) {
            allConfirmations.clear();
        }

        try {
            jsonObject = new JSONObject(ALL_CONFIRMATIONS_JSON_STRING);
            JSONArray result = jsonObject.getJSONArray("result");

            for(int i=0; i<result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                int viewingRequestID = Integer.parseInt(jo.getString("confirmedViewingsID"));
                int day = Integer.parseInt(jo.getString("day"));
                int month = Integer.parseInt(jo.getString("month"));
                int year = Integer.parseInt(jo.getString("year"));
                int hour = Integer.parseInt(jo.getString("hour"));
                int minute = Integer.parseInt(jo.getString("minute"));
                int hostID = Integer.parseInt(jo.getString("hostID"));
                int requestUserID = Integer.parseInt(jo.getString("requestUserID"));
                int listingID = Integer.parseInt(jo.getString("listingID"));

                BookingRequest br = new BookingRequest(viewingRequestID, day, month, year, hour, minute, hostID, requestUserID, listingID);
                allConfirmations.add(br);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void populateList() {
        bookingRequestsAdapter = new BookingRequestListAdapter();
        bookingRequestsListView.setAdapter(bookingRequestsAdapter);
    }

    @Override
    public void populateConfirmedList() {
        confirmedBookingsAdapter = new ConfirmedBookingsListAdapter();
        confirmedBookingsListView.setAdapter(confirmedBookingsAdapter);

    }

    private class ConfirmedBookingsListAdapter extends ArrayAdapter<BookingRequest> {
        public ConfirmedBookingsListAdapter() {
            super(VisitorBookingInformation.this, R.layout.listview_host_booking_requests, allConfirmations);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if(view == null)
                view = getLayoutInflater().inflate(R.layout.listview_host_booking_requests, parent, false);

            BookingRequest currentRequest = allConfirmations.get(position);
            TextView myRequestTitle = (TextView) view.findViewById(R.id.listviewBookingRequestTitle);
            TextView myRequestDate = (TextView) view.findViewById(R.id.listviewBookingRequestDate);
            TextView myRequestTime = (TextView) view.findViewById(R.id.listviewBookingRequestTime);
            TextView myRequestBy = (TextView) view.findViewById(R.id.listviewBookingRequestBy);

            String title = getTitle(currentRequest.getBookingRequestListingID());
            myRequestTitle.setText("Space: " + title);

            myRequestDate.setText("Date Requested: " + currentRequest.getBookingRequestDay() + "/" + currentRequest.getBookingRequestMonth() + "/" + currentRequest.getBookingRequestYear());
            String bookMinute = Integer.toString(currentRequest.getBookingRequestMinute());
            if(currentRequest.getBookingRequestMinute() < 10) {
                bookMinute = "0" + Integer.toString(currentRequest.getBookingRequestMinute());
            }
            myRequestTime.setText("Time Requested: " + currentRequest.getBookingRequestHour()+  ":" + bookMinute);
            String hostName = getHostName(currentRequest.getBookingRequestHostID());
            myRequestBy.setText("Viewing requested by: " + hostName);

            return view;
        }
    }



    private class BookingRequestListAdapter extends ArrayAdapter<BookingRequest> {
        public BookingRequestListAdapter() {
            super(VisitorBookingInformation.this, R.layout.listview_host_booking_requests, allRequests);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if(view == null)
                view = getLayoutInflater().inflate(R.layout.listview_host_booking_requests, parent, false);

            BookingRequest currentRequest = allRequests.get(position);
            TextView myRequestTitle = (TextView) view.findViewById(R.id.listviewBookingRequestTitle);
            TextView myRequestDate = (TextView) view.findViewById(R.id.listviewBookingRequestDate);
            TextView myRequestTime = (TextView) view.findViewById(R.id.listviewBookingRequestTime);
            TextView myRequestBy = (TextView) view.findViewById(R.id.listviewBookingRequestBy);

            String title = getTitle(currentRequest.getBookingRequestListingID());
            myRequestTitle.setText("Space: " + title);

            myRequestDate.setText("Date Requested: " + currentRequest.getBookingRequestDay() + "/" + currentRequest.getBookingRequestMonth() + "/" + currentRequest.getBookingRequestYear());
            String bookMinute = Integer.toString(currentRequest.getBookingRequestMinute());
            if(currentRequest.getBookingRequestMinute() < 10) {
                bookMinute = "0" + Integer.toString(currentRequest.getBookingRequestMinute());
            }
            myRequestTime.setText("Time Requested: " + currentRequest.getBookingRequestHour()+  ":" + bookMinute);
            String hostName = getHostName(currentRequest.getBookingRequestHostID());
            myRequestBy.setText("Viewing requested by: " + hostName);

            return view;
        }
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

    //Notice any change


}