package finalyear.officeme.Singletons;

import java.util.ArrayList;

import finalyear.officeme.model.Address;

/**
 * Created by College on 12/04/2016.
 */
public class AddressSingleton {


    private static AddressSingleton instance = null;




    ArrayList<Address> addresses = new ArrayList<>();

    protected AddressSingleton() {

    }

    public static AddressSingleton getInstance() {
        if(instance == null) {
            instance = new AddressSingleton();
        }
        return instance;
    }

    public ArrayList<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(ArrayList<Address> addresses) {
        this.addresses = addresses;
    }
}
