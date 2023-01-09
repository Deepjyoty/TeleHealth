package com.gnrc.telehealth;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.gnrc.telehealth.ui.InitAuthSDKActivity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class WebViewActivity extends AppCompatActivity {

    //  https://gnrctelehealth.com/tele-med/app_v2
    //  https://gnrctelehealth.com/developer/KKP/tele-med/app_v2
    // http://172.16.12.98/tele-med/app_v2
    private String baseUrl = "https://gnrctelehealth.com/developer/KKP/tele-med/app_v2  ";
    private WebView webView;
    boolean doubleBackToExitPressedOnce = false;


    private ValueCallback<Uri> mUploadMessage;
    private Uri mCapturedImageURI = null;
    private ValueCallback<Uri[]> mFilePathCallback;
    private String mCameraPhotoPath;
    private static final int INPUT_FILE_REQUEST_CODE = 1;
    private static final int FILECHOOSER_RESULTCODE = 1;



    final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 102;
    final int MY_PERMISSIONS_REQUEST_FILE_UPLOAD = 103;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);


        final ProgressDialog progressBar = new ProgressDialog(WebViewActivity.this);
        progressBar.setMessage("Loading Please wait");
        progressBar.setCancelable(false);
        progressBar.setCanceledOnTouchOutside(false);
        webView.loadUrl(baseUrl);
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
                        int PERM_WRITE_EXTERNAL_STORAGE = ContextCompat.checkSelfPermission(WebViewActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        int PERM_READ_EXTERNAL_STORAGE = ContextCompat.checkSelfPermission(WebViewActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
                        int PERM_CAMERA = ContextCompat.checkSelfPermission(WebViewActivity.this, Manifest.permission.CAMERA);
                        if (PERM_WRITE_EXTERNAL_STORAGE == PackageManager.PERMISSION_GRANTED && PERM_READ_EXTERNAL_STORAGE == PackageManager.PERMISSION_GRANTED && PERM_CAMERA == PackageManager.PERMISSION_GRANTED) {
                            webView.loadUrl(url);
                        } else {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_FILE_UPLOAD);
                        }
                    } else {
                        view.loadUrl(url);
                    }
                } else if (url.contains("tcpdf")) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if ((ContextCompat.checkSelfPermission(WebViewActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                        } else {
                            webView.loadUrl(url);
                        }
                    } else {
                        view.loadUrl(url);
                    }
                } else if (url.contains("androidCloseApp")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(WebViewActivity.this);
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

                } else if (url.contains("startMeeting")) {
                    Intent intent = new Intent(getBaseContext(), InitAuthSDKActivity.class);
                    Uri url1 = Uri.parse(url);
                    Set<String> paramNames = url1.getQueryParameterNames();
                    for (String key : paramNames) {
                        String value = url1.getQueryParameter(key);
                        Log.d("KKP", "shouldOverrideUrlLoading: " + key + "###" + value + "###" + url);
                        intent.putExtra(key, value);
                    }
                    startActivity(intent);
                }  else {
                    view.loadUrl(url);
                }

                return true;
            }
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.d("KKP", "onPageStarted: ");
                //    super.onPageStarted(view, url, favicon);
                if (!progressBar.isShowing()) {
                    webView.setVisibility(View.GONE);
                    progressBar.show();

                }
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d("KKP", "onPageFinished: " + url);
                if (progressBar.isShowing()) {
                    webView.setVisibility(View.VISIBLE);
                    progressBar.dismiss();

                }
            }
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.d("KKP", "onReceivedError: " + description);
                view.loadUrl("about:blank");
                AlertDialog alertDialog = new AlertDialog.Builder(WebViewActivity.this).create();
                alertDialog.setCancelable(false);
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.setTitle("Something went wrong");
                alertDialog.setMessage("Check your internet connection and try again.");
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Try Again", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        startActivity(getIntent());
                        webView.reload();
                    }
                });
                alertDialog.show();
                super.onReceivedError(webView, errorCode, description, failingUrl);
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

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                        takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                        //    Log.e(Common.TAG, "Unable to create Image File", ex);
                    }

                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(photoFile));
                    } else {
                        takePictureIntent = null;
                    }
                }

                Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                contentSelectionIntent.setType("image/*");

                Intent[] intentArray;
                if (takePictureIntent != null) {
                    intentArray = new Intent[]{takePictureIntent};
                } else {
                    intentArray = new Intent[0];
                }

                Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);

                startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE);

                return true;
            }
        });
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                Map<String, String> parameters = new HashMap<>();
                Uri url1 = Uri.parse(url);
                Set<String> paramNames = url1.getQueryParameterNames();
                for (String key : paramNames) {
                    String value = url1.getQueryParameter(key);
                    parameters.put(key, value);
                }
                String fileName = parameters.get("presc_no").toString();
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); //Notify client once download is completed!
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                dm.enqueue(request);
                Toast.makeText(getApplicationContext(), "Downloading File", Toast.LENGTH_LONG).show();
            }
        });
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if(data.getDataString() == null){
                Log.d("KKP","Null Intent Data: "+data.getDataString());
                data=null;
            }
        } catch(Exception ex){}

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Log.d("KKP","requestCode: "+requestCode);
            Log.d("KKP","resultCode: "+resultCode);
            Log.d("KKP","INPUT_FILE_REQUEST_CODE: "+INPUT_FILE_REQUEST_CODE);
            Log.d("KKP","mFilePathCallback: "+mFilePathCallback);

            if (requestCode != INPUT_FILE_REQUEST_CODE || mFilePathCallback == null) {
                super.onActivityResult(requestCode, resultCode, data);
                return;
            }
            Uri[] results = null;
            // Check that the response is a good one
            if (resultCode == WebViewActivity.RESULT_OK) {

                Log.d("KKP","WebViewActivity.RESULT_OK: "+WebViewActivity.RESULT_OK);
                Log.d("KKP","data: "+data);
                if (data == null) {

                    // If there is not data, then we may have taken a photo
                    if (mCameraPhotoPath != null) {
                        results = new Uri[]{Uri.parse(mCameraPhotoPath)};
                    }
                } else {
                    String dataString = data.getDataString();
                    if (dataString != null) {
                        results = new Uri[]{Uri.parse(dataString)};
                    }
                }
            }

            Log.d("kkp","onReceiveValue"+results);
            mFilePathCallback.onReceiveValue(results);
            mFilePathCallback = null;

        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
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
//                if (resultCode == 0) {
//                    result = null;
//                }
                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;

            }
        }


        return;
    }
    public static String intentToString(Intent intent) {
        if (intent == null)
            return "";

        StringBuilder stringBuilder = new StringBuilder("action: ")
                .append(intent.getAction())
                .append(" data: ")
                .append(intent.getDataString())
                .append(" extras: ")
                ;
        for (String key : intent.getExtras().keySet())
            stringBuilder.append(key).append("=").append(intent.getExtras().get(key)).append(" ");

        return stringBuilder.toString();

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this,ChoiceActivity.class);
        startActivity(i);
        finish();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean canUseExternalStorage = false;
        boolean isFileUploadAllowed = false;
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    canUseExternalStorage = true;
                }

                if (!canUseExternalStorage) {
                    Toast.makeText(WebViewActivity.this, "Cannot use this feature without requested permission", Toast.LENGTH_SHORT).show();
                } else {
                    // user now provided permission
                    // perform function for what you want to achieve

                }
            }

            case MY_PERMISSIONS_REQUEST_FILE_UPLOAD: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    isFileUploadAllowed = true;
                }

                if (!isFileUploadAllowed) {
                    Toast.makeText(WebViewActivity.this, "Cannot use this feature without requested permission", Toast.LENGTH_SHORT).show();
                } else {
                    // user now provided permission
                    // perform function for what you want to achieve
                }
            }
        }
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

}
