package finalyear.officeme.Singletons;

/**
 * Created by College on 09/04/2016.
 */
public class UserID {
    private static UserID instance = null;

    private int loggedInId;

    protected UserID(){

    }

    public static UserID getInstance() {
        if(instance == null) {
            instance = new UserID();
        }

        return instance;
    }

    public void setLoggedInId(int i) {
        loggedInId = i;
    }

    public int getLoggedInId() {
        return loggedInId;
    }
}
