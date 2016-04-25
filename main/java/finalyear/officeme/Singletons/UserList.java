package finalyear.officeme.Singletons;

import java.util.ArrayList;

import finalyear.officeme.model.User;

/**
 * Created by College on 09/04/2016.
 */
public class UserList {
    private static UserList instance = null;



    ArrayList<User> userList = new ArrayList<>();

    protected UserList() {

    }

    public static UserList getInstance() {
        if(instance == null) {
            instance = new UserList();
        }
        return instance;
    }

    public ArrayList<User> getUserList() {
        return userList;
    }

    public void setUserList(ArrayList<User> userList) {
        this.userList = userList;
    }


}
