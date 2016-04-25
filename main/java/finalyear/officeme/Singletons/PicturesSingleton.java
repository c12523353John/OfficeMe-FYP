package finalyear.officeme.Singletons;

import java.util.ArrayList;

import finalyear.officeme.model.Picture;

/**
 * Created by College on 12/04/2016.
 */
public class PicturesSingleton {
    private static PicturesSingleton instance = null;



    ArrayList<Picture> pictures = new ArrayList<>();

    protected PicturesSingleton() {

    }

    public static PicturesSingleton getInstance() {
        if(instance == null) {
            instance = new PicturesSingleton();
        }
        return instance;
    }

    public ArrayList<Picture> getPictures() {
        return pictures;
    }

    public void setPictures(ArrayList<Picture> pictures) {
        this.pictures = pictures;
    }
}
