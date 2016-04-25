package finalyear.officeme.parse;

/**
 * Created by College on 12/02/2016.
 */
public class Story {

    String title, url, image;

    public Story(String title, String url, String image) {
        super();
        this.title = title;
        this.url = url;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }



}
