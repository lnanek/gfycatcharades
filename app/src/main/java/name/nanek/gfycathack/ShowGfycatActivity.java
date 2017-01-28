package name.nanek.gfycathack;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ShowGfycatActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_gfycat);
        textView = (TextView) findViewById(R.id.textView);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.gfycat.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GfycatService service = retrofit.create(GfycatService.class);

        Call<List<String>> tags = service.trending(10);
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
