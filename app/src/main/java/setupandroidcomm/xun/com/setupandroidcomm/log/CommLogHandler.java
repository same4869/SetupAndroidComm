package setupandroidcomm.xun.com.setupandroidcomm.log;

import android.content.Context;

/**
 * Created by xunwang on 2017/6/23.
 */

public class CommLogHandler implements Thread.UncaughtExceptionHandler {
    private static CommLogHandler handler;
    private static Context context;
    private static Thread.UncaughtExceptionHandler defaultHandler;

    private CommLogHandler() {
    }

    private static CommLogHandler getInstance() {
        if (null == handler) {
            synchronized (CommLogHandler.class) {
                if (handler == null) {
                    handler = new CommLogHandler();
                }
            }
        }
        return handler;
    }

    public static void init(Context context) {
        context = context.getApplicationContext();
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(getInstance());
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {

    }
}
