package finalyear.officeme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import finalyear.officeme.Singletons.LoggedIn;
import finalyear.officeme.Singletons.LoggedInUserDetails;
import finalyear.officeme.Singletons.UserID;
import finalyear.officeme.Singletons.UserList;
import finalyear.officeme.activity.MainActivity;
import finalyear.officeme.model.User;
import finalyear.officeme.parse.PopulateSingletonLists;

public class Login extends AppCompatActivity implements View.OnClickListener {

    Button bLogin;
    EditText etEmail, etPassword;
    TextView logRegisterLinkButton;
    UserLocalStore userLocalStore;
    boolean exist = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Remove the Populate Singletons List
        PopulateSingletonLists ps = new PopulateSingletonLists();
        ps.asyncPopulate();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acivity_login);

        etEmail = (EditText) findViewById(R.id.etLogEmail);
        etPassword = (EditText) findViewById(R.id.etLogPassword);
        bLogin = (Button) findViewById(R.id.bLogin);
        logRegisterLinkButton = (TextView) findViewById(R.id.logRegisterLinkToRegister);


        bLogin.setOnClickListener(this);
        logRegisterLinkButton.setOnClickListener(this);

        userLocalStore = new UserLocalStore(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bLogin:
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                User user = new User(email, password);
                Log.d("LoginUsersSize: ", Integer.toString(UserList.getInstance().getUserList().size()));
                authenticate(user);
                break;

            case R.id.logRegisterLinkToRegister:
                startActivity(new Intent(this, Register.class));
                break;
        }
    }

    public void authenticate(User user) {

        for(int i=0; i< UserList.getInstance().getUserList().size(); i++) {
            if(UserList.getInstance().getUserList().get(i).get_email().equalsIgnoreCase(user.get_email()) && UserList.getInstance().getUserList().get(i).get_password().equalsIgnoreCase(user.get_password())) {
                exist = true;
                UserID.getInstance().setLoggedInId(UserList.getInstance().getUserList().get(i).getId());
                LoggedInUserDetails.getInstance().setUser(new User(UserList.getInstance().getUserList().get(i).getId(), UserList.getInstance().getUserList().get(i).get_name(),
                        UserList.getInstance().getUserList().get(i).get_email(), UserList.getInstance().getUserList().get(i).get_password(), UserList.getInstance().getUserList().get(i).getPhoneNumber()));
                Log.d("Logged in Login", Integer.toString(UserID.getInstance().getLoggedInId()));

                break;
            }
        }

        if(exist) {
            Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
            LoggedIn.getInstance().setLoggedIn(true);
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "User not found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(Login.this, MainActivity.class);
        startActivity(setIntent);
    }
}
