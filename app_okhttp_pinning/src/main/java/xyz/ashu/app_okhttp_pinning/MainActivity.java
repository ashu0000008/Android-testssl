package xyz.ashu.app_okhttp_pinning;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import xyz.ashu.app_okhttp_pinning.network.Api;


public class MainActivity extends AppCompatActivity {

    private TextView mTvHello;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTvHello = findViewById(R.id.tv_hello);
        mTvHello.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSomething();
            }
        });
    }

    private void getSomething() {
        new Api().getCryptoPercent(new Api.OnResponseListener() {
            @Override
            public void onResponse(String response) {
                refreshView(response);
            }
        });
    }

    private void refreshView(final String response) {
        mTvHello.post(new Runnable() {
            @Override
            public void run() {
                mTvHello.setText(response);
            }
        });
    }
}
