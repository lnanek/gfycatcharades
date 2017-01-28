package name.nanek.gfycathack;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.util.List;

import name.nanek.gfycathack.databinding.ActivityShowGfycatBinding;
import name.nanek.gfycathack.models.TrendingResponse;
import name.nanek.gfycathack.network.GfycatService;
import name.nanek.gfycathack.network.GfycatServiceFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowGfycatActivity extends AppCompatActivity {

    static final int REQUEST_VIDEO_CAPTURE = 1;

    static final String TAG = ShowGfycatActivity.class.getSimpleName();

    GfycatService api = GfycatServiceFactory.get();

    ActivityShowGfycatBinding binding;

    String nextGifCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
    }

    private void showTrendingGfycat() {
        Call<TrendingResponse> tags = api.trendingGfycats(10, nextGifCursor);
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
        if (null == response) {
            binding.textView.setText("no response");
        }

        nextGifCursor = response.cursor;

        if ( response.gfycats.isEmpty() ) {
            binding.textView.setText("empty response");
        }

        binding.textView.setText("gfycat: " + response.gfycats.get(0));

        final String url = response.gfycats.get(0).max2mbGif;

        Glide.with(this)
                .load(url)
                .into(new GlideDrawableImageViewTarget(binding.imageView));
    }

    private void showTrendingTags() {
        Call<List<String>> tags = api.trendingTags(10);
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
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Uri videoUri = intent.getData();
            Log.d(TAG, "videoUri = " + videoUri);
            //mVideoView.setVideoURI(videoUri);
        }
    }

}
