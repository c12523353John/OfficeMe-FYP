package finalyear.officeme.parse;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import finalyear.officeme.R;

public class ParseActivity extends AppCompatActivity {


    public static ArrayList<Story> stories;
    Bitmap bitmap;
    ListView siliconListView;
    ArrayAdapter<Story> storyListAdapter;
    List<Story> storyList = new ArrayList<Story>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parse);
        stories = new ArrayList<>();
        bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.imageholder);


        ParseSilicon silicon = new ParseSilicon();
        try {
            stories.clear();
            stories.addAll(silicon.execute().get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Log.v("Main act size", Integer.toString(stories.size()));

        siliconListView = (ListView) findViewById(R.id.listView);
        addStoryAListToList();
        populateList(); //commented out as its causing it to crash

        storyListAdapter.notifyDataSetChanged();

        siliconListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent myIntent = new Intent(ParseActivity.this,WebViewPage.class);
                myIntent.putExtra("key",position);
                Story currentStory = storyList.get(position);
                String url = currentStory.getUrl().toString().trim();
                myIntent.putExtra("url", url);
                startActivity(myIntent);
            }
        });



    }

    public void addStoryAListToList() {
        for(int i=0; i<stories.size(); i++) {
            String title = stories.get(i).getTitle();
            String url = stories.get(i).getUrl();
            String img = stories.get(i).getImage();

            Story story = new Story(title, url, img);
            storyList.add(story);
        }

    }

    private void populateList() {
        storyListAdapter = new SiliconListAdapter();
        siliconListView.setAdapter(storyListAdapter); // this is the line that is causing it to crash

    }

    public class SiliconListAdapter extends ArrayAdapter<Story> {
        public SiliconListAdapter() { super(ParseActivity.this, R.layout.silicon_list_view,storyList);}


        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if(view == null)
                view = getLayoutInflater().inflate(R.layout.silicon_list_view, parent, false);

            Story currentStory = storyList.get(position);

            //           view.setTag(currentStory.getTitle());
//            view.setTag(position);


            TextView title = (TextView) view.findViewById(R.id.listSiliconTitle);
            title.setText(currentStory.getTitle().trim());


            try {
                ImageView circularImageView = (ImageView) view.findViewById(R.id.circleView);
                circularImageView.setImageBitmap(bitmap);
                GetImage img = new GetImage();
                bitmap = img.execute(currentStory.getImage().toString()).get();
                Bitmap circularBitmap = ImageConverter.getRoundedCornerBitmap(bitmap, 100);
                circularImageView.setImageBitmap(circularBitmap);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }


            return view;
        }


    }

    class GetImage extends AsyncTask<String , Void,  Bitmap> {

        @Override
        protected Bitmap doInBackground(String... address) {

            Bitmap d = null;
            try {
                InputStream is = (InputStream) this.fetch(address[0]);
                d = BitmapFactory.decodeStream(is);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return d;
        }

        private Object fetch(String address) throws MalformedURLException, IOException {

            URL url = new URL(address);
            Object content = url.getContent();
            return content;
        }

    }
}
