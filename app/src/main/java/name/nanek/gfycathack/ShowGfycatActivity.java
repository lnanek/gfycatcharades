package name.nanek.gfycathack;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

import name.nanek.gfycathack.network.GfycatServiceFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ShowGfycatActivity extends AppCompatActivity {

    GfycatService api = GfycatServiceFactory.get();

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_gfycat);
        textView = (TextView) findViewById(R.id.textView);

        showTrendingGfycat();
    }

    private void showTrendingGfycat() {
        Call<TrendingResponse> tags = api.trendingGfycats(10);
        tags.enqueue(new Callback<TrendingResponse>() {
            @Override
            public void onResponse(Call<TrendingResponse> call, Response<TrendingResponse> response) {
                showTrendingGfycatResponse(response.body());
            }

            @Override
            public void onFailure(Call<TrendingResponse> call, Throwable t) {
                textView.setText("Error: " + t);
            }
        });
    }

    private void showTrendingGfycatResponse(TrendingResponse response) {
        if (null == response) {
            textView.setText("no response");
        }

        if ( response.gfycats.isEmpty() ) {
            textView.setText("empty response");
        }

        textView.setText("gfycat: " + response.gfycats.get(0));
    }

    private void showTrendingTags() {
        Call<List<String>> tags = api.trendingTags(10);
        tags.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                textView.setText(response.body().toString());
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                textView.setText("Error: " + t);
            }
        });
    }
}
