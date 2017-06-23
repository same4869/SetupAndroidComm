package setupandroidcomm.xun.com.setupandroidcomm;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by xunwang on 17/5/31.
 */

public class MyApp extends Application {
    public static RequestQueue mQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        mQueue = Volley.newRequestQueue(this);
    }

}
