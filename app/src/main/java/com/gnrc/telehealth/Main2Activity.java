package com.gnrc.telehealth;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.MediaStore;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.app.NotificationCompat;
//import android.support.v4.content.ContextCompat;
//import android.support.v4.content.FileProvider;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.MimeTypeMap;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import android.util.Log;

import com.gnrc.telehealth.ui.InitAuthSDKActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Main2Activity extends AppCompatActivity {

    private WebView webView;
    boolean doubleBackToExitPressedOnce = false;
    private static final int FILECHOOSER_RESULTCODE = 2888;
    private ValueCallback<Uri> mUploadMessage;
    private Uri mCapturedImageURI = null;
    private static final int INPUT_FILE_REQUEST_CODE = 1;
    private String mCameraPhotoPath;
    private ValueCallback<Uri[]> mFilePathCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        //  webView.getSettings().setBuiltInZoomControls(true);
        //  webView.clearHistory();
        //  webView.clearCache(true);
        //  webView.getSettings().setSupportZoom(true);


        final ProgressDialog progressBar = new ProgressDialog(Main2Activity.this);
        progressBar.setMessage("Please wait...");
        progressBar.setCancelable(false);
        progressBar.setCanceledOnTouchOutside(false);

        webView.loadUrl("https://gnrctelehealth.com/developer/KKP/tele-med/app");
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("zoom.us")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Zoom App Not Installed. Please Install Zoom", Toast.LENGTH_LONG).show();
                    }
                } else if (url.contains("file_upload")) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(Main2Activity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        } else {
                            ActivityCompat.requestPermissions(Main2Activity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 401);
                        }
                        view.loadUrl(url);
                    } else {
                        // if version is below m then write code here,
                        Log.d("checkPermission", "if version is below m then write code here");
                        view.loadUrl(url);
                    }
                } else if (url.contains("androidCloseApp")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
                    builder.setMessage("Are you sure you want to exit?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    finish();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();

                } else if(url.contains("startMeeting")){
                    Intent intent = new Intent(getBaseContext(), InitAuthSDKActivity.class);
                    Uri url1 = Uri.parse(url);
                    Set<String> paramNames = url1.getQueryParameterNames();
                    for (String key: paramNames) {
                        String value = url1.getQueryParameter(key);
                        Log.d("KKP", "shouldOverrideUrlLoading: "+key+"###"+value+"###"+url);
                        intent.putExtra(key, value);
                    }
                    startActivity(intent);
                }




                else {
                    view.loadUrl(url);

                }

                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.d("KKP", "onPageStarted: ");
                //    super.onPageStarted(view, url, favicon);
                if (!progressBar.isShowing()) {
                    progressBar.show();
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d("KKP", "onPageFinished: " + url);
                if (progressBar.isShowing()) {
                    progressBar.dismiss();
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.d("KKP", "onReceivedError: " + description);
                view.loadUrl("about:blank");
                AlertDialog alertDialog = new AlertDialog.Builder(Main2Activity.this).create();
                alertDialog.setCancelable(false);
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.setTitle("Something went wrong");
                alertDialog.setMessage("Check your internet connection and try again.");
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Try Again", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        startActivity(getIntent());
                        progressBar.show();
                        webView.reload();
                    }
                });
                alertDialog.show();
                super.onReceivedError(webView, errorCode, description, failingUrl);
                if (progressBar.isShowing()) {
                    //  progressBar.dismiss();
                    //  Intent i = new Intent(getApplicationContext(),ErrorActivity.class);
                    //  startActivity(i);
                    //  finish();

                }
//              //  webView.loadUrl("file:///android_asset/sample.html");


            }


        });
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
//              return super.onJsAlert(view, url, message, result);
                AlertDialog dialog = new AlertDialog.Builder(view.getContext()).
                        setTitle("Alert").
                        setMessage(message).
                        setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //do nothing
                            }
                        }).create();
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                result.confirm();
                return true;
            }

            public boolean onShowFileChooser(WebView view, ValueCallback<Uri[]> filePath, WebChromeClient.FileChooserParams fileChooserParams) {
                // Double check that we don't have any existing callbacks
                if (mFilePathCallback != null) {
                    mFilePathCallback.onReceiveValue(null);
                }
                mFilePathCallback = filePath;
                File imageStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "AndroidExampleFolder");

                if (!imageStorageDir.exists()) {
                    // Create AndroidExampleFolder at sdcard
                    imageStorageDir.mkdirs();
                }

                // Create camera captured image file path and name
                File file = new File(imageStorageDir + File.separator + "IMG_"+ String.valueOf(System.currentTimeMillis())+ ".jpg");
                mCapturedImageURI = Uri.fromFile(file);

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    //  takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                        //  Log.e(Common.TAG, "Unable to create Image File", ex);
                        Log.d("mCameraPhotoPath", " Error", ex);
                    }

                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                    } else {
                        //       takePictureIntent = null;
                        Log.d("mCameraPhotoPath", " Not Created");
                    }
                }


                //   takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
//                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//                    // Create the File where the photo should go
//                    File photoFile = null;
//                    try {
//                        photoFile = createImageFile();
//                        takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
//                    } catch (IOException ex) {
//                        // Error occurred while creating the File
//                      //  Log.e(Common.TAG, "Unable to create Image File", ex);
//                    }
//
//                    // Continue only if the File was successfully created
//                    if (photoFile != null) {
//                        mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
//                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
//                                Uri.fromFile(photoFile));
//                    } else {
//                        takePictureIntent = null;
//                    }
//                }


                Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                contentSelectionIntent.setType("image/*");

//                Intent[] intentArray;
//                if (takePictureIntent != null) {
//                    intentArray = new Intent[]{takePictureIntent};
//                } else {
//                    intentArray = new Intent[0];
//                }
                Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Parcelable[]{takePictureIntent});
                startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE);

                return true;

            }

            private File createImageFile() throws IOException {
                // Create an image file name
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "_";
                File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                Log.d("storageDir", String.valueOf(storageDir));

                try {
                    File imageFile = File.createTempFile(imageFileName,  /* prefix */".jpg",         /* suffix */storageDir      /* directory */);
                    Log.d("imageFile", String.valueOf(imageFile));
                    return imageFile;
                } catch (Exception ex) {
                    Log.d("imageFile", "error", ex);
                    return null;
                }


            }

        });
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent,String contentDisposition, String mimetype,long contentLength) {
  ///              webView.loadUrl(url);
//                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
//                request.allowScanningByMediaScanner();
//                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); //Notify client once download is completed!
//                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Name of your downloadble file goes here, example: Mathematics II ");
//                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
//                dm.enqueue(request);
//                Toast.makeText(getApplicationContext(), "Downloading File", //To notify the Client that the file is being downloaded
//                        Toast.LENGTH_LONG).show();
                Log.d("KKP", "ulr: "+url);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.gnrctelehealth.com/developer/KKP/tele-med/app/test.php?q="+url));
                startActivity(browserIntent);

            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("KKP", requestCode + "##" + resultCode + "##" + data);
        Log.d("KKP","step1");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Log.d("KKP","step2");
            if (requestCode != INPUT_FILE_REQUEST_CODE || mFilePathCallback == null) {
                Log.d("KKP","step3");
                super.onActivityResult(requestCode, resultCode, data);
                return;
            }

            Uri[] results = null;

            // Check that the response is a good one
            if (resultCode == Activity.RESULT_OK) {
                Log.d("KKP","step4");
                if (data == null) {
                    Log.d("KKP","step5");
                    // If there is not data, then we may have taken a photo
                    if (mCameraPhotoPath != null) {
                        results = new Uri[]{Uri.parse(mCameraPhotoPath)};
                        ExifInterface ei = null;
                        try {
                            ei = new ExifInterface(mCameraPhotoPath);
                            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_UNDEFINED);
                            Log.d("KKP","step6-TRY"+orientation);
                        } catch (IOException e) {
                            Log.d("KKP",e.getMessage());
                            e.printStackTrace();
                        }
                    }
                } else {
                    Log.d("KKP","step7");
                    String dataString = data.getDataString();
                    if (dataString != null) {
                        Log.d("KKP","step8");
                        results = new Uri[]{Uri.parse(dataString)};
                    }
                }
            } else {
                Log.d("KKP","step9");
                results = new Uri[]{Uri.parse(String.valueOf(mCapturedImageURI))};
            }
            if (resultCode == 0) {
                Log.d("KKP","step10");
                results = null;
            }

            mFilePathCallback.onReceiveValue(results);
            Log.d("KKP","step11"+Environment.getExternalStorageDirectory());
            mFilePathCallback = null;

        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            Log.d("activity1", "activity2");
            if (requestCode != FILECHOOSER_RESULTCODE || mUploadMessage == null) {
                super.onActivityResult(requestCode, resultCode, data);
                return;
            }

            if (requestCode == FILECHOOSER_RESULTCODE) {
                if (null == this.mUploadMessage) {
                    return;
                }
                Uri result = null;

                try {
                    if (resultCode != RESULT_OK) {

                        result = null;

                    } else {

                        // retrieve from the private variable if the intent is null
                        result = data == null ? mCapturedImageURI : data.getData();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "activity :" + e,
                            Toast.LENGTH_LONG).show();
                }
                if (resultCode == 0) {
                    result = null;
                }

                Log.d("KKP", "mUploadMessage"+mUploadMessage);
                Log.d("KKP", "result"+result);
                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;

            }
        }


        return;
    }

    @Override
    public void onBackPressed() {
//        if (doubleBackToExitPressedOnce) {
//            super.onBackPressed();
//            return;
//        }
//
//        this.doubleBackToExitPressedOnce = true;
//        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                doubleBackToExitPressedOnce=false;
//            }
//        }, 2000);
    }

}




