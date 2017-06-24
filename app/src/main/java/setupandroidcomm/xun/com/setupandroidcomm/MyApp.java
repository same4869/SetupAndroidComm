package setupandroidcomm.xun.com.setupandroidcomm;

import android.app.Application;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.concurrent.ConcurrentHashMap;

import setupandroidcomm.xun.com.setupandroidcomm.log.CommLogHandler;

/**
 * Created by xunwang on 17/5/31.
 */

public class MyApp extends Application implements CommLogHandler.CrashUploader {
    public static RequestQueue mQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        mQueue = Volley.newRequestQueue(this);
        CommLogHandler.init(this);
    }

    @Override
    public void uploadCrashMessage(ConcurrentHashMap<String, Object> infos) {
        Log.d("kkkkkkkk", "上传日志````");
    }
}
