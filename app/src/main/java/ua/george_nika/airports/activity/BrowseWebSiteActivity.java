package ua.george_nika.airports.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import ua.george_nika.airports.R;

public class BrowseWebSiteActivity extends Activity {
    public static final String EXTRA_WEB_SITE = "EXTRA_WEB_SITE";
    private static final String TAG = BrowseWebSiteActivity.class.getSimpleName();

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_web_site);

        Intent intent = getIntent();
        if (intent==null) {
            Log.e(TAG," No intent");
            return;
        }
        if (!intent.hasExtra(EXTRA_WEB_SITE)) {
            Log.e(TAG," Intent without "+EXTRA_WEB_SITE);
            return ;
        }
        String webSite = intent.getStringExtra(EXTRA_WEB_SITE);

        webView= (WebView) findViewById(R.id.web_view);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled (true);

        webSettings.setDefaultTextEncodingName("utf-8");
        // устанавливаем строку User Agent
        String ua = "Mozilla/5.0 (Linux; Android 4.1.1; HTC One X Build/JRO03C) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.58 Mobile Safari/537.31";
        webSettings.setUserAgentString(ua);

        // другие настройки
        webSettings.setUseWideViewPort(true);
        webSettings.setSupportZoom(true);

        webView.setInitialScale(1);
        webView.setWebViewClient(new InnerViewClient());
        webView.loadUrl(inspectWebUrl(webSite));
        Log.i(TAG," Show url: "+webSite);
    }

    @Override
    public void onBackPressed() {
        if(webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    private String inspectWebUrl(String webUrl){
        String resultUrl = webUrl;
        if (!resultUrl.contains("http")){
            resultUrl= "http://".concat(webUrl);
        }
        return resultUrl;
    }


    //  ****************   Class  *************


    private class InnerViewClient extends WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            view.loadUrl(url);
            return true;
        }
    }
}
