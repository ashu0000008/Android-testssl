package xyz.ashu.testssl;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import xyz.ashu.demo11.R;
import xyz.ashu.testssl.network.Api;

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

    private void refreshView(String response) {
        mTvHello.post(new Runnable() {
            @Override
            public void run() {
                mTvHello.setText(response);
            }
        });
    }
}
