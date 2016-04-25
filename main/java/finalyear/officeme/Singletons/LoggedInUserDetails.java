package finalyear.officeme.Singletons;

import finalyear.officeme.model.User;

/**
 * Created by College on 09/04/2016.
 */
public class LoggedInUserDetails {
    private static LoggedInUserDetails instance = null;

    private User user;

    protected LoggedInUserDetails(){

    }

    public static LoggedInUserDetails getInstance() {
        if(instance == null) {
            instance = new LoggedInUserDetails();
        }

        return instance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }




}

