package setupandroidcomm.xun.com.setupandroidcomm.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import setupandroidcomm.xun.com.setupandroidcomm.R;
import setupandroidcomm.xun.com.setupandroidcomm.net.IRequestCallback;
import setupandroidcomm.xun.com.setupandroidcomm.net.IRequestManager;
import setupandroidcomm.xun.com.setupandroidcomm.net.RequestFactory;

/**
 * Created by xunwang on 17/5/31.
 */

public class NetActivity extends AppCompatActivity implements View.OnClickListener {
    private Button volleyBtn, okhttpBtn;
    private TextView resposeTv;
    private IRequestManager requestManager;
    private String url = "https://api.douban.com/v2/movie/top250";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net);

        initView();
    }

    private void initView() {
        volleyBtn = (Button) findViewById(R.id.request_volley_btn);
        volleyBtn.setOnClickListener(this);
        resposeTv = (TextView) findViewById(R.id.respose_tv);
        okhttpBtn = (Button) findViewById(R.id.request_okhttp_btn);
        okhttpBtn.setOnClickListener(this);
    }

    private void requestNetwork() {
//        CommThreadPool.poolExecute(new Runnable() {
//            @Override
//            public void run() {
                requestManager.get(url, new IRequestCallback() {
                    @Override
                    public void onSuccess(String response) {
                        resposeTv.setText(response);
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.request_volley_btn:
                requestManager = RequestFactory.getRequestManager(RequestFactory.RequestType.VOLLEY);
//                HTTPSTrustManager.allowAllSSL();
                requestNetwork();
                break;
            case R.id.request_okhttp_btn:
                requestManager = RequestFactory.getRequestManager(RequestFactory.RequestType.OKHTTP);
                requestNetwork();
                break;
            default:
                break;
        }
    }
}
