package finalyear.officeme;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import finalyear.officeme.model.User;

public class ServerRequests {
    ProgressDialog progressDialog;
    public static final String SERVER_ADDRESS = "http://johnrock.comlu.com";

    public ServerRequests(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing...");
        progressDialog.setMessage("Please wait...");
    }

    public void storeUserDataInBackground(User user, GetUserCallback userCallBack) {
        progressDialog.show();
        new StoreUserDataAsyncTask(user, userCallBack).execute();
    }

    public void fetchUserDataAsyncTask(User user, GetUserCallback userCallBack) {
        progressDialog.show();
        new fetchUserDataAsyncTask(user, userCallBack).execute();
    }

    /**
     * parameter sent to task upon execution progress published during
     * background computation result of the background computation
     */

    public class StoreUserDataAsyncTask extends AsyncTask<Void, Void, Void> {
        User user;
        GetUserCallback userCallBack;

        public StoreUserDataAsyncTask(User user, GetUserCallback userCallBack) {
            this.user = user;
            this.userCallBack = userCallBack;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Map<String, String> dataToSend = new HashMap<>();
            dataToSend.put("name", user.get_name());
            dataToSend.put("email", user.get_email());
            dataToSend.put("password", user.get_password());


            //Encoded String - need to encode string by our custom method
            String encodedStr = getEncodedData(dataToSend);

            //Connection Handling
            try {
                //Converting address String to URL
                URL url = new URL(SERVER_ADDRESS + "/RegisterUser.php");
                //Opening the connection (Not setting or using CONNECTION_TIMEOUT)
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                //Post Method
                con.setRequestMethod("POST");
                //To enable inputting values using POST method
                con.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
                //Writing dataToSend to outputstreamwriter
                writer.write(encodedStr);
                writer.flush();


            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            userCallBack.done(null);
        }

        private String getEncodedData(Map<String,String> data) {
            StringBuilder sb = new StringBuilder();
            for(String key : data.keySet()) {
                String value = null;
                try {
                    value = URLEncoder.encode(data.get(key), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                if(sb.length()>0)
                    sb.append("&");

                sb.append(key + "=" + value);
            }
            return sb.toString();
        }

    }

    public class fetchUserDataAsyncTask extends AsyncTask<Void, Void, User> {
        User user;
        GetUserCallback userCallBack;

        public fetchUserDataAsyncTask(User user, GetUserCallback userCallBack) {
            this.user = user;
            this.userCallBack = userCallBack;
        }

        @Override
        protected User doInBackground(Void... params) {
            //corrected
            User returnedUser = null;
            Map<String, String> dataToSend = new HashMap<>();
            dataToSend.put("email", user.get_email());
            dataToSend.put("password", user.get_password());


            String encodedStr = getEncodedData(dataToSend);

            //Used to read data
            BufferedReader reader = null;

            try{
            //Connection Handling
            //Converting address String to URL
            URL url = new URL(SERVER_ADDRESS + "FetchUserData.php");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            //Post Method
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
            writer.write(encodedStr);
            writer.flush();

            //Data Read Procedure
            StringBuilder sb = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String result;
                while ((result = reader.readLine()) != null) { //Read till there is something available
                    sb.append(result + "\n");     //Reading and saving line by line - not all at once
                }

                result = sb.toString();

                JSONObject jObject = new JSONObject(result);

                if (jObject.length() != 0){
                    Log.v("happened", "2");
                    String name = jObject.getString("name");

                    returnedUser = new User(name, user.get_email(),
                            user.get_password());
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(reader != null) {
                    try {
                        reader.close();     //Closing the
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return returnedUser;
        }


        private String getEncodedData(Map<String,String> data) {
            StringBuilder sb = new StringBuilder();
            for(String key : data.keySet()) {
                String value = null;
                try {
                    value = URLEncoder.encode(data.get(key), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                if(sb.length()>0)
                    sb.append("&");

                sb.append(key + "=" + value);
            }
            return sb.toString();
        }

        @Override
        protected void onPostExecute(User returnedUser) {
            super.onPostExecute(returnedUser);
            progressDialog.dismiss();
            userCallBack.done(returnedUser);
        }


    }
}
