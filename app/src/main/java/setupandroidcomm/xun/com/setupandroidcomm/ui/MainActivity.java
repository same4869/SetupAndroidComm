package setupandroidcomm.xun.com.setupandroidcomm.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import setupandroidcomm.xun.com.setupandroidcomm.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button networkBtn,crashBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        networkBtn = (Button) findViewById(R.id.networkBtn);
        networkBtn.setOnClickListener(this);
        crashBtn = (Button) findViewById(R.id.crashBtn);
        crashBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.networkBtn:
                Intent intent = new Intent(this, NetActivity.class);
                startActivity(intent);
                break;
            case R.id.crashBtn:
                crashBtn = null;
                crashBtn.setText("");
                break;
            default:
                break;
        }
    }
}
