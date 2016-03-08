package com.example.makar.simplebrowser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends Activity {
    private TextView textView;
    private ProgressBar progress;
    private DownloadWebPage asyncDownloading;
    private TextView urlText;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.codeText);
        textView.setMovementMethod(new ScrollingMovementMethod());
        progress = (ProgressBar) findViewById(R.id.progressBar);
        progress.setVisibility(View.GONE);
        urlText = (TextView) findViewById(R.id.uriText);

        //adding uri textView listener
        urlText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // loading page
                    progress.setVisibility(View.VISIBLE);
                    asyncDownloading = new DownloadWebPage();
                    asyncDownloading.execute(urlText.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }


    private class DownloadWebPage extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            for (String url : urls) {
                DefaultHttpClient client = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(url);
                try {
                    HttpResponse execute = client.execute(httpGet);
                    InputStream content = execute.getEntity().getContent();
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String str;
                    while ((str = buffer.readLine()) != null) {
                        result += str;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            progress.setVisibility(View.GONE);
            textView.setText(result);
        }
    }
}
