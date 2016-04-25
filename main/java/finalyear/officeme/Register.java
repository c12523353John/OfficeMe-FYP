package finalyear.officeme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

import finalyear.officeme.DatabaseHandler.Config;
import finalyear.officeme.DatabaseHandler.RequestHandler;
import finalyear.officeme.Singletons.UserList;
import finalyear.officeme.model.User;

public class Register extends AppCompatActivity implements View.OnClickListener{

    Button bRegister;
    EditText etName, etEmail, etPassword, etPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = (EditText) findViewById(R.id.etRegName);
        etEmail = (EditText) findViewById(R.id.etRegEmail);
        etPassword = (EditText) findViewById(R.id.etRegPassword);
        etPhoneNumber = (EditText) findViewById(R.id.etRegPhoneNumber);
        bRegister = (Button) findViewById(R.id.bRegister);


        bRegister.setOnClickListener(this);

    }


    //Adding an user
    private void addUser(){

        final String name = etName.getText().toString().trim();
        final String email = etEmail.getText().toString().trim();
        final String phoneNumber = etPhoneNumber.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();

        class AddUser extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Register.this,"Adding...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(Register.this, s, Toast.LENGTH_LONG).show();


            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                params.put(Config.KEY_USER_NAME,name);
                params.put(Config.KEY_USER_EMAIL,email);
                params.put(Config.KEY_USER_PASSWORD,password);
                params.put(Config.KEY_USER_PHONE_NUMBER,phoneNumber);
                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.URL_ADD_USER, params);
                return res;
            }
        }

        AddUser au = new AddUser();
        au.execute();

    }



    @Override
    public void onClick(View v) {
        if(v == bRegister){
            addUser();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    Intent i=new Intent(Register.this,Login.class);
                    startActivity(i);
                }
            }, 3000);


        }

    }
}
