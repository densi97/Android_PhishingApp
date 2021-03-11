package saarland.cispa.phishingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebView MVW = findViewById(R.id.mainview);
        MVW.getSettings().setJavaScriptEnabled(true);
        MVW.getSettings().setDisplayZoomControls(false);

        Intent incoming = getIntent();
        if(incoming != null){
            Uri uri = incoming.getData();
            if(uri != null) {
                if(uri.toString().contains("densi97.pythonanywhere.com")) {
                    MVW.loadUrl("https://densi97.pythonanywhere.com/poc/");
                } else {
                    MVW.loadUrl("https://densi97.pythonanywhere.com");
                }
            } else {
                MVW.loadUrl("https://densi97.pythonanywhere.com");
            }
        } else {
            MVW.loadUrl("https://densi97.pythonanywhere.com");
        }
    }
}
