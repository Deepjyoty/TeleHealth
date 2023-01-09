package com.gnrc.telehealth;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends Activity
{
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);


        final ProgressDialog progressBar = new ProgressDialog(MainActivity.this);
        progressBar.setMessage("Please wait...");

        // live http://110.235.202.18/tele-med/app/

        // local : http://172.16.12.98:9000/tele-med/app/

        //String webViewUrl = "http://172.16.12.98:9000/payumoney/PHP_Bolt-master/index.php";
        webView.loadUrl("http://172.16.12.98:9000/tele-med/app/");
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(url.contains("zoom.us"))
                {
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(i);
                }
                else {
                    view.loadUrl(url);

                }

                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (!progressBar.isShowing()) {
                    progressBar.show();
                }
            }

            public void onPageFinished(WebView view, String url) {
                if (progressBar.isShowing()) {
                    progressBar.dismiss();
                }
            }

//            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//                if (progressBar.isShowing()) {
//                    progressBar.dismiss();
//                }
//            }

            @Override
            public void onReceivedError(WebView view, int errorCode,String description, String failingUrl) {

//                                if (progressBar.isShowing()) {
//                    progressBar.dismiss();
//                }

               // view.loadUrl("file:///android_asset/sample.html");
//                Toast.makeText(MainActivity.this, "Please Check Internet Connection!", Toast.LENGTH_SHORT).show();

                Toast.makeText(MainActivity.this, "Your Internet Connection May not be active Or " + description , Toast.LENGTH_LONG).show();
                super.onReceivedError(view, errorCode, description, failingUrl);
            }



        });

    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

//    @Override
//    public void onBackPressed()
//    {
//        if(webView.canGoBack()){
//            webView.goBack();
//        }else
//        {
//                    new AlertDialog.Builder(this)
//                            .setIcon(android.R.drawable.ic_dialog_alert)
//                            .setTitle("Exit!")
//                            .setMessage("Are you sure you want to close?")
//                            .setPositiveButton("Yes", new DialogInterface.OnClickListener()
//                            {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    finish();
//                                }
//
//                            })
//                            .setNegativeButton("No", null)
//                            .show();
//        }
//    }
}