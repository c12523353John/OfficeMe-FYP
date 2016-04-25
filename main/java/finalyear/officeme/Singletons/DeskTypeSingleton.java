package finalyear.officeme.Singletons;

import java.util.ArrayList;

import finalyear.officeme.model.DeskType;

/**
 * Created by College on 11/04/2016.
 */
public class DeskTypeSingleton {
    private static DeskTypeSingleton instance = null;



    ArrayList<DeskType> deskTypes = new ArrayList<>();

    protected DeskTypeSingleton() {

    }

    public static DeskTypeSingleton getInstance() {
        if(instance == null) {
            instance = new DeskTypeSingleton();
        }
        return instance;
    }

    public ArrayList<DeskType> getDeskTypes() {
        return deskTypes;
    }

    public void setDeskTypes(ArrayList<DeskType> deskTypes) {
        this.deskTypes = deskTypes;
    }

}
