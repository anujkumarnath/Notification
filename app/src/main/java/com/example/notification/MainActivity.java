package com.example.notification;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scrape();
    }

    public void scrape() {
        String url = "http://www.tezu.ernet.in/feed.html";
        (new NetworkTask()).execute(url);
    }

    public class NetworkTask extends AsyncTask<String, Void, Document> {

        @Override
        protected Document doInBackground(String... strings) {
            String url = strings[0];
            Log.d("TAG", url);
            Document document = null;
            try {
                document = Jsoup.connect(url).get();
            } catch (IOException e) {
                Log.e("TAG", "IOException has occured");
            }

            return document;
        }

        @Override
        protected void onPostExecute(Document document) {
            super.onPostExecute(document);
            if (document == null) {
                Log.e("TAG", "document == null");
            }

            Elements els = document.select("div[style]:has(a)");
            for (Element el : els) {
                String[] textSplitResult = el.select("a[href]").first().html().split("<br>");

                String header = Jsoup.parse(textSplitResult[0]).text();
                Log.d("Parse", header);

                String title = textSplitResult[1];
                Log.d("Parse", title);

                // TODO: Create URL object insted of String to avoid malformed URLs like spaces in between address
                String url = el.select("a[href]").attr("href");
                Log.d("Parse", url);

                String postDate = el.select("font.homepagedarkgry").first().text();
                Log.d("Parse", postDate);
            }
        }
    }
}
