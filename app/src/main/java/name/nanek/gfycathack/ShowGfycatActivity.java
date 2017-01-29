package name.nanek.gfycathack;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.List;

import name.nanek.gfycathack.databinding.ActivityShowGfycatBinding;
import name.nanek.gfycathack.models.ClientCredentialsRequest;
import name.nanek.gfycathack.models.ClientCredentialsResponse;
import name.nanek.gfycathack.models.GetGfyResponse;
import name.nanek.gfycathack.models.Gfycat;
import name.nanek.gfycathack.models.PrepareUploadResponse;
import name.nanek.gfycathack.models.RequestBodyUtil;
import name.nanek.gfycathack.models.TrendingResponse;
import name.nanek.gfycathack.models.UploadStatus;
import name.nanek.gfycathack.network.CuratedImageService;
import name.nanek.gfycathack.network.GfycatService;
import name.nanek.gfycathack.network.GfycatServiceFactory;
import name.nanek.gfycathack.network.UploadService;
import name.nanek.gfycathack.network.UploadServiceFactory;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

public class ShowGfycatActivity extends AppCompatActivity {

    static final int REQUEST_VIDEO_CAPTURE = 1;

    static final String TAG = ShowGfycatActivity.class.getSimpleName();

    static String strSDCardPathName =
            //        Environment.getExternalStorageDirectory() + "/";
            "/sdcard/";

    static String strFileName = "";

    GfycatService api = GfycatServiceFactory.get();

    GfycatService apiTest = GfycatServiceFactory.getTest();

    CuratedImageService curatedImageService = new CuratedImageService();

    ActivityShowGfycatBinding binding;

    //String nextGifCursor;

    ProgressDialog dialog;

    String authToken;

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");

        super.onCreate(savedInstanceState);

        handler = new Handler();

        binding = DataBindingUtil.setContentView(this, R.layout.activity_show_gfycat);

        /*
        setContentView(R.layout.activity_show_gfycat);
        textView = (TextView) findViewById(R.id.textView);
        imageView = (ImageView) findViewById(R.id.imageView);
        */

        showTrendingGfycat();

        binding.newGifButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTrendingGfycat();
            }
        });

        binding.startRecordingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCameraRecording();
            }
        });

        login();
    }

    private void login() {
        Log.d(TAG, "login");

        showLoadingDialog("Logging in...");

        Call<ClientCredentialsResponse> credentialsCall =
                api.getCredentials(new ClientCredentialsRequest());
        credentialsCall.enqueue(new Callback<ClientCredentialsResponse>() {
            @Override
            public void onResponse(Call<ClientCredentialsResponse> call, Response<ClientCredentialsResponse> response) {
                Log.d(TAG, "credentials response: " + response.body());

                authToken = "Bearer " + response.body().accessToken;

                hideLoadingDialog();
            }

            @Override
            public void onFailure(Call<ClientCredentialsResponse> call, Throwable t) {
                Log.d(TAG, "credentials error: ", t);
                hideLoadingDialog();
                showErrorDialog("Error logging in.\n" + t);
            }
        });
    }

    private void showLoadingDialog(String message) {
        Log.d(TAG, "showLoadingDialog");

        hideLoadingDialog();

        dialog = new ProgressDialog(this); // this = YourActivity
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage(message);
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void hideLoadingDialog() {
        Log.d(TAG, "hideLoadingDialog");
        if ( null != dialog ) {
            dialog.hide();
            dialog = null;
        }
    }

    private void showErrorDialog(String error) {
        Log.d(TAG, "showErrorDialog error = " + error);

        new AlertDialog.Builder(this).setTitle(error)
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        finish();
                    }
                })
                .show();
    }

    private void showTrendingGfycat() {
        Log.d(TAG, "showTrendingGfycat");

        showLoadingDialog("Getting popular Gfycat...");

        curatedImageService.getGfy(api, apiTest, authToken, new Callback<Gfycat>() {
            @Override
            public void onResponse(Call<Gfycat> call, Response<Gfycat> response) {
                hideLoadingDialog();
                showGfycat(response.body());
            }

            @Override
            public void onFailure(Call<Gfycat> call, Throwable t) {
                hideLoadingDialog();

                showErrorDialog("Error Getting popular Gfycat.\n" + t);
                binding.textView.setText("Error: " + t);
            }
        });
        /*
        Call<TrendingResponse> tags = api.trendingGfycats(authToken, 10, nextGifCursor);
        tags.enqueue(new Callback<TrendingResponse>() {
            @Override
            public void onResponse(Call<TrendingResponse> call, Response<TrendingResponse> response) {
                hideLoadingDialog();
                showTrendingGfycatResponse(response.body());
            }

            @Override
            public void onFailure(Call<TrendingResponse> call, Throwable t) {
                hideLoadingDialog();

                showErrorDialog("Error Getting popular Gfycat.\n" + t);
                binding.textView.setText("Error: " + t);
            }
        });
        */
    }

    private void showTrendingGfycatResponse(TrendingResponse response) {
        Log.d(TAG, "showTrendingGfycatResponse");

        if (null == response) {
            binding.textView.setText("no response");
            showErrorDialog("Error Getting popular Gfycat.");
        }

        //nextGifCursor = response.cursor;

        if (response.gfycats.isEmpty()) {
            binding.textView.setText("empty response");
            showErrorDialog("Error Getting popular Gfycat.");
        }

        binding.textView.setText("gfycat: " + response.gfycats.get(0));

        showGfycat(response.gfycats.get(0));
    }

    private void showGfycat(Gfycat gfycat) {
        Log.d(TAG, "showGfycat gfycat = " + gfycat);

        final String url = gfycat.max2mbGif;

        Glide.with(this)
                .load(url)
                .into(new GlideDrawableImageViewTarget(binding.imageView));
    }

    private void showRecordingGfycat(Gfycat gfycat) {
        Log.d(TAG, "showRecordingGfycat gfycat = " + gfycat);

        binding.recordingView.setVisibility(View.VISIBLE);

        final String url = gfycat.max2mbGif;

        Glide.with(this)
                .load(url)
                .into(new GlideDrawableImageViewTarget(binding.recordingView));
    }

    private void showTrendingTags() {
        Log.d(TAG, "showTrendingTags");

        Call<List<String>> tags = api.trendingTags(authToken, 10);
        tags.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                binding.textView.setText(response.body().toString());
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                binding.textView.setText("Error: " + t);
            }
        });
    }

    private static final int VIDEO_QUALITY_HIGH = 1;

    private static final int VIDEO_QUALITY_LOW = 0;

    private void startCameraRecording() {
        Log.d(TAG, "startCameraRecording");

        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        Uri fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
        takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // Use low quality for faster uploads
        takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, VIDEO_QUALITY_LOW);
//        startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);

        // 15 second limit
        takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 15);

        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Log.d(TAG, "onActivityResult");

        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Uri videoUri = intent.getData();
            Log.d(TAG, "videoUri = " + videoUri);
            //mVideoView.setVideoURI(videoUri);
            prepareToUploadFile();

        }
    }

    private void prepareToUploadFile() {
        Log.d(TAG, "prepareToUploadFile");


        showLoadingDialog("Preparing to upload video...");

        Call<PrepareUploadResponse> prepare = api.prepareUpload(authToken);
        prepare.enqueue(new Callback<PrepareUploadResponse>() {
            @Override
            public void onResponse(Call<PrepareUploadResponse> call, Response<PrepareUploadResponse> response) {
                Log.d(TAG, "prepare response: " + response.body());

                hideLoadingDialog();
                uploadFile(response.body().uploadType, response.body().gfyname);
            }

            @Override
            public void onFailure(Call<PrepareUploadResponse> call, Throwable t) {
                Log.e(TAG, "error calling prepare upload", t);
                hideLoadingDialog();
                showErrorDialog("Error Preparing to upload video.\n" + t);
            }
        });

    }

    private void uploadFile(String uploadType, final String gfyname) {
        Log.d(TAG, "uploadFile" +
                " uploadType = " + uploadType +
                ", gfyname = " + gfyname);


        showLoadingDialog("Uploading video...");

        try {
            InputStream in = new FileInputStream(new File(strSDCardPathName + strFileName));
            byte[] buf;
            buf = new byte[in.available()];
            while (in.read(buf) != -1) ;
            RequestBody requestBody = RequestBody
                    .create(MediaType.parse("video/mp4"), buf);

            UploadService service = UploadServiceFactory.create("https://" + uploadType + "/");
            Call<Void> call = service.uploadPhoto(/*authToken, */gfyname, requestBody);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    Log.d(TAG, "uploadFile success: " + response);
                    hideLoadingDialog();

                    checkStatus(gfyname);
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.d(TAG, "uploadFile onFailure", t);
                    showErrorDialog("Error Uploading");
                    hideLoadingDialog();

                    showErrorDialog("Error Uploading video.\n" + t);
                }
            });


        } catch (Throwable t) {
            Log.e(TAG, "error uploading file", t);
        }
    }

    private static Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {

        // Generate File Name
        java.util.Date date = new java.util.Date();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date.getTime());

        strFileName = "Clip_" + timeStamp + ".mp4";

        File mediaFile;

        if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(strSDCardPathName + strFileName);
        } else {
            return null;
        }

        Log.d(TAG, "getOutputMediaFile returning: " + mediaFile);
        return mediaFile;
    }

    private void checkStatus(final String gfyname) {
        Log.d(TAG, "checkStatus gfyname = " + gfyname);

        if (null == dialog) {
            showLoadingDialog("Checking Status...");
        }

        Call<UploadStatus> prepare = api.getStatus(authToken, gfyname);
        prepare.enqueue(new Callback<UploadStatus>() {
            @Override
            public void onResponse(Call<UploadStatus> call, Response<UploadStatus> response) {
                Log.d(TAG, "Checking Status response: " + response.body());

                hideLoadingDialog();

                final String status = response.body().task;
                if ("complete".equals(status)) {
                    getUploadedGif(gfyname);
                    return;
                }

                showLoadingDialog("Status: " + status + "...");

                // Check again in 5 seconds
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        checkStatus(gfyname);
                    }
                }, 5000);
            }

            @Override
            public void onFailure(Call<UploadStatus> call, Throwable t) {
                Log.e(TAG, "error Checking Status", t);
                hideLoadingDialog();
                showErrorDialog("Error Checking Status.\n" + t);
            }
        });
    }

    private void getUploadedGif(final String gfyname) {
        Log.d(TAG, "getUploadedGif gfyname = " + gfyname);

        showLoadingDialog("Getting uploaded gfycat...");

        Call<GetGfyResponse> prepare = apiTest.getGfy(authToken, gfyname);
        prepare.enqueue(new Callback<GetGfyResponse>() {
            @Override
            public void onResponse(Call<GetGfyResponse> call, Response<GetGfyResponse> response) {
                Log.d(TAG, "Getting uploaded gfycat response: " + response.body());

                hideLoadingDialog();

                binding.titleView.setText("You Uploaded:");

                showRecordingGfycat(response.body().gfyItem);
            }

            @Override
            public void onFailure(Call<GetGfyResponse> call, Throwable t) {
                Log.e(TAG, "error Getting uploaded gfycat", t);
                hideLoadingDialog();
                showErrorDialog("Error Getting uploaded gfycat.\n" + t);
            }
        });
    }



}
