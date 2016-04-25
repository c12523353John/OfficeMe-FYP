package finalyear.officeme.parse;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import finalyear.officeme.R;

public class WebViewPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().requestFeature(Window.FEATURE_PROGRESS);
        getWindow().setFeatureInt(Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_page);

        WebView wbView = (WebView) findViewById(R.id.WebView);
        wbView.getSettings().setJavaScriptEnabled(true);

        final Activity activity = this;

        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);

        wbView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                progressDialog.show();
                progressDialog.setProgress(0);
                activity.setProgress(progress * 1000);

                progressDialog.incrementProgressBy(progress);

                if (progress > 95 && progressDialog.isShowing()) //&& progressDialog.isShowing()
                    progressDialog.dismiss();
            }
        });

        String url = getIntent().getStringExtra("url");
        wbView.loadUrl(url);



    }




}
