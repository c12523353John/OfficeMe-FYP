package finalyear.officeme.parse;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by College on 12/02/2016.
 */
public class ParseSilicon extends AsyncTask<Void, Void, ArrayList<Story>> {

    ArrayList<Story> stories = new ArrayList<>();

    protected ArrayList<Story> doInBackground(Void... params) {

        Document doc = null;
        try {
            doc = Jsoup.connect("https://www.siliconrepublic.com/start-ups").get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Elements links = null;
        try {
            links = doc.select("div.large-3.small-3.columns.nopadding>a[href]");
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Element link : links) {
            String title = link.childNode(1).attr("alt").trim();
            String url = link.attr("href").trim();
            String img = link.childNode(1).attr("src").trim();

            Story story = new Story((title+"."), url, img);
            stories.add(story);
        }

        Log.v("Background story size:", Integer.toString(stories.size()));

        return stories;

    }

}

