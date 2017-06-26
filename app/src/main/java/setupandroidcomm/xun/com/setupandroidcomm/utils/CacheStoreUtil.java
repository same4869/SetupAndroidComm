package setupandroidcomm.xun.com.setupandroidcomm.utils;

import android.content.Context;

import java.io.File;

import commlib.xun.com.commlib.utils.BaseStoreUtil;
import setupandroidcomm.xun.com.setupandroidcomm.config.Constants;

/**
 * Created by xunwang on 2017/6/26.
 */

public class CacheStoreUtil extends BaseStoreUtil{

    public static File getCacheDir(Context ctx) {
        return getRootDir(ctx, Constants.COMM_CACHE, false);
    }

    public static File getImagesDir(Context ctx) {
        return getRootDir(ctx, Constants.COMM_IMAGES, false);
    }

    public static File getImagesDir(Context ctx, String fileDir) {
        return getRootDir(ctx, Constants.COMM_IMAGES + "/" + fileDir, false);
    }

    public static File getLogsDir(Context ctx) {
        return getRootDir(ctx, Constants.COMM_LOGS, false);
    }

    public static File getUserEventDir(Context ctx) {
        return getRootDir(ctx, Constants.COMM_EVENTS, false);
    }
}
