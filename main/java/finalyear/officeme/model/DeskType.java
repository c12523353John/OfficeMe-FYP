package finalyear.officeme.model;

/**
 * Created by College on 10/04/2016.
 */
public class DeskType {

    int deskTypeID;
    String deskType;

    public DeskType(int deskTypeID, String deskType) {
        this.deskTypeID = deskTypeID;
        this.deskType = deskType;
    }

    public int getDeskTypeID() {
        return deskTypeID;
    }

    public void setDeskTypeID(int deskTypeID) {
        this.deskTypeID = deskTypeID;
    }

    public String getDeskType() {
        return deskType;
    }

    public void setDeskType(String deskType) {
        this.deskType = deskType;
    }
}
