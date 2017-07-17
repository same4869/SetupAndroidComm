package setupandroidcomm.xun.com.setupandroidcomm.ui;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import setupandroidcomm.xun.com.setupandroidcomm.R;
import setupandroidcomm.xun.com.setupandroidcomm.utils.Security;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button networkBtn, crashBtn, ndkBtn;
    private TextView ndkTv;

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
        ndkBtn = (Button) findViewById(R.id.ndk_btn);
        ndkBtn.setOnClickListener(this);
        ndkTv = (TextView) findViewById(R.id.ndk_tv);
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
            case R.id.ndk_btn:
                Log.d("kkkkkkkk", "getSign() --> " + getSign());
                ndkTv.setText(Security.getSecret());
                break;
            default:
                break;
        }
    }

    /**
     * 展示了如何用Java代码获取签名
     */
    private String getSign() {
        try {
            // 下面几行代码展示如何任意获取Context对象，在jni中也可以使用这种方式
//            Class<?> activityThreadClz = Class.forName("android.app.ActivityThread");
//            Method currentApplication = activityThreadClz.getMethod("currentApplication");
//            Application application = (Application) currentApplication.invoke(null);
//            PackageManager pm = application.getPackageManager();
//            PackageInfo pi = pm.getPackageInfo(application.getPackageName(), PackageManager.GET_SIGNATURES);

            PackageManager pm = getPackageManager();
            PackageInfo pi = pm.getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            Signature[] signatures = pi.signatures;
            Signature signature0 = signatures[0];
            return signature0.toCharsString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
