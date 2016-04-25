package finalyear.officeme.Singletons;

/**
 * Created by College on 09/04/2016.
 */
public class LoggedIn {
    private static LoggedIn instance = null;

    private boolean loggedIn = false;

    protected LoggedIn(){

    }

    public static LoggedIn getInstance() {
        if(instance == null) {
            instance = new LoggedIn();
        }

        return instance;
    }

    public boolean getLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }



}
