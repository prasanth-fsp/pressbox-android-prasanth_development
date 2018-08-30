package com.usepressbox.pressbox.ui.activity.order;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebBackForwardList;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;
import com.usepressbox.pressbox.LandingScreen;
import com.usepressbox.pressbox.R;
import com.usepressbox.pressbox.ui.MyAcccount;
import com.usepressbox.pressbox.ui.activity.register.Config;
import com.usepressbox.pressbox.ui.fragment.IntroWelcomeFragment;
import com.usepressbox.pressbox.utils.Constants;
import com.usepressbox.pressbox.utils.SessionManager;


/**
 * Created by fssd101 on 11/7/2016.
 * This class is used to play the intro video
 * T
 */
public class Video extends Activity {
    private static final int RECOVERY_REQUEST = 1;
    private YouTubePlayerView youTubeView;


    private WebView webviewIns;
    private Toolbar toolbar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        setToolbarTitle();
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        webviewIns=(WebView)findViewById(R.id.webview);
        webviewIns.setWebViewClient(new MyBrowser());
        webviewIns.getSettings().setLoadsImagesAutomatically(true);
        webviewIns.getSettings().setJavaScriptEnabled(true);
        webviewIns.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webviewIns.loadUrl("http://www.usepressbox.com/how-it-works/");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
    }
    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
    public void setToolbarTitle(){
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setNavigationIcon(R.drawable.back);

        TextView toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setVisibility(View.GONE);

        TextView toolbar_left = (TextView) toolbar.findViewById(R.id.toolbar_left);
        toolbar_left.setVisibility(View.GONE);

        TextView toolbar_right = (TextView) toolbar.findViewById(R.id.toolbar_right);
        toolbar_right.setVisibility(View.GONE);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        webviewIns.clearCache(true);
        webviewIns.clearHistory();
        webviewIns.destroy();
    }
}

