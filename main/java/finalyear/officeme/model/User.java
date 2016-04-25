package finalyear.officeme.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by College on 25/01/2016.
 */

public class User {

    String name, email, password;
    int id;


    String phoneNumber;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User() {

    }

    public User(int id, String name, String email, String password, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    public User(String email, String password) {
        this("", email, password);
    }

    public String get_name() {
        return name;
    }

    public void set_name(String _name) {
        this.name = _name;
    }

    public String get_email() {
        return email;
    }

    public void set_email(String _email) {
        this.email = _email;
    }

    public String get_password() {
        return password;
    }

    public void set_password(String _password) {
        this.password = _password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
