package name.nanek.gfycathack;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.util.List;

import name.nanek.gfycathack.databinding.ActivityShowGfycatBinding;
import name.nanek.gfycathack.models.ClientCredentialsRequest;
import name.nanek.gfycathack.models.ClientCredentialsResponse;
import name.nanek.gfycathack.models.PrepareUploadResponse;
import name.nanek.gfycathack.models.TrendingResponse;
import name.nanek.gfycathack.network.GfycatService;
import name.nanek.gfycathack.network.GfycatServiceFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowGfycatActivity extends AppCompatActivity {

    static final int REQUEST_VIDEO_CAPTURE = 1;

    static final String TAG = ShowGfycatActivity.class.getSimpleName();

    static final String DEBUG_SKIP_RECORD_URL =
            "content://media/external/video/media/9250";

    GfycatService api = GfycatServiceFactory.get();

    ActivityShowGfycatBinding binding;

    String nextGifCursor;

    ProgressDialog dialog;

    String authToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");

        super.onCreate(savedInstanceState);

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
                dispatchTakeVideoIntent();
            }
        });

        login();
    }

    private void login() {

        showLoadingDialog();

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
                showErrorDialog("Error logging in.");
            }
        });
    }

    private void showLoadingDialog() {
        dialog = new ProgressDialog(this); // this = YourActivity
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Loading. Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void hideLoadingDialog() {
        dialog.hide();
        dialog = null;
    }

    private void showErrorDialog(String error) {
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

        Call<TrendingResponse> tags = api.trendingGfycats(authToken, 10, nextGifCursor);
        tags.enqueue(new Callback<TrendingResponse>() {
            @Override
            public void onResponse(Call<TrendingResponse> call, Response<TrendingResponse> response) {
                showTrendingGfycatResponse(response.body());
            }

            @Override
            public void onFailure(Call<TrendingResponse> call, Throwable t) {
                binding.textView.setText("Error: " + t);
            }
        });
    }

    private void showTrendingGfycatResponse(TrendingResponse response) {
        Log.d(TAG, "showTrendingGfycatResponse");

        if (null == response) {
            binding.textView.setText("no response");
        }

        nextGifCursor = response.cursor;

        if (response.gfycats.isEmpty()) {
            binding.textView.setText("empty response");
        }

        binding.textView.setText("gfycat: " + response.gfycats.get(0));

        final String url = response.gfycats.get(0).max2mbGif;

        Glide.with(this)
                .load(url)
                .into(new GlideDrawableImageViewTarget(binding.imageView));
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

    private void dispatchTakeVideoIntent() {
        Log.d(TAG, "dispatchTakeVideoIntent");

        if (null != DEBUG_SKIP_RECORD_URL) {
            Uri uri = Uri.parse(DEBUG_SKIP_RECORD_URL);
            uploadVideo(uri);
            return;
        }

        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
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
        }
    }

    private void uploadVideo(Uri uri) {
        Log.d(TAG, "uploadVideo uri = " + uri);

        Call<PrepareUploadResponse> prepare = api.prepareUpload(authToken);
        prepare.enqueue(new Callback<PrepareUploadResponse>() {
            @Override
            public void onResponse(Call<PrepareUploadResponse> call, Response<PrepareUploadResponse> response) {
                Log.d(TAG, "prepare response: " + response.body());
            }

            @Override
            public void onFailure(Call<PrepareUploadResponse> call, Throwable t) {
                Log.e(TAG, "error calling prepare upload", t);
            }
        });

    }

}
