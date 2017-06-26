package setupandroidcomm.xun.com.setupandroidcomm.log;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import setupandroidcomm.xun.com.setupandroidcomm.utils.CacheStoreUtil;

/**
 * Created by xunwang on 2017/6/23.
 */

public class CommLogHandler implements Thread.UncaughtExceptionHandler {
    //Log Tag
    public static final String TAG = "CommLogHandler";
    //异常信息
    public static final String EXCEPETION_INFOS_STRING = "excepetion_infos_string";
    //基本信息
    public static final String APP_INFOS_STRING = "app_infos_string";

    private static volatile CommLogHandler INSTANCE;
    private static Context mContext;
    private static Thread.UncaughtExceptionHandler mDefaultHandler;

    //用来存储设备信息和异常信息
    private ConcurrentHashMap<String, Object> infos = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, String> appInfoMap = new ConcurrentHashMap<>();
    private StringBuffer AppInfoSB = new StringBuffer();

    //用于格式化日期,作为日志文件名的一部分
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    private static String mDirPath = null;
    private String mExceptionInfos;
    //上传文件具体实现
    private CrashUploader mCrashUploader;

    private CommLogHandler() {
    }

    private static CommLogHandler getInstance() {
        if (null == INSTANCE) {
            synchronized (CommLogHandler.class) {
                if (INSTANCE == null) {
                    INSTANCE = new CommLogHandler();
                }
            }
        }
        return INSTANCE;
    }

    public static void init(Context context) {
        mContext = context.getApplicationContext();
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(getInstance());
        mDirPath = CacheStoreUtil.getCacheDir(mContext).toString();
        File mDirectory = new File(mDirPath);
        if (!mDirectory.exists()) {
            mDirectory.mkdirs();
        }
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (!catchCrashException(e) && mDefaultHandler != null) {
            //没有自定义的CrashHandler的时候就调用系统默认的异常处理方式
            mDefaultHandler.uncaughtException(t, e);
        } else {
            //退出应用
            killProcess();
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean catchCrashException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        //收集设备参数信息
        collectInfos(mContext, ex);
        //保存日志文件
        saveCrashInfo2File();
        //上传崩溃信息
        uploadCrashMessage(infos);

        return true;
    }

    /**
     * 将崩溃日志信息写入本地文件
     */
    private String saveCrashInfo2File() {
        String mTime = formatter.format(new Date());
        StringBuffer mStringBuffer = new StringBuffer();
        mStringBuffer.append("--start--------------").append(mTime).append("----------------").append("\r\n");
        mStringBuffer.append(AppInfoSB.toString());
        mStringBuffer.append(mExceptionInfos);
        mStringBuffer.append("--end--------------").append(mTime).append("----------------").append("\r\n");
        // 保存文件，设置文件名
        String mFileName = "CrashLog-" + mTime + ".log";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            try {
                File mDirectory = new File(mDirPath);
                Log.v(TAG, mDirectory.toString());
                if (!mDirectory.exists())
                    mDirectory.mkdirs();
                FileOutputStream mFileOutputStream = new FileOutputStream(mDirectory + File.separator + mFileName);
                mFileOutputStream.write(mStringBuffer.toString().getBytes());
                mFileOutputStream.close();
                return mFileName;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 上传崩溃信息到服务器
     */
    public void uploadCrashMessage(ConcurrentHashMap<String, Object> infos) {
        if (mCrashUploader != null) {
            mCrashUploader.uploadCrashMessage(infos);
        }
    }


    /**
     * 崩溃信息上传接口回调
     */
    public interface CrashUploader {
        void uploadCrashMessage(ConcurrentHashMap<String, Object> infos);
    }

    /**
     * 退出应用
     */
    public static void killProcess() {
        //结束应用
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, "哎呀，程序发生异常啦...", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }).start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        //退出程序
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    /**
     * 获取捕获异常的信息
     *
     * @param ex
     */
    private String collectExceptionInfos(Throwable ex) {
        Writer mWriter = new StringWriter();
        PrintWriter mPrintWriter = new PrintWriter(mWriter);
        ex.printStackTrace(mPrintWriter);
        ex.printStackTrace();
        Throwable mThrowable = ex.getCause();
        // 迭代栈队列把所有的异常信息写入writer中
        while (mThrowable != null) {
            mThrowable.printStackTrace(mPrintWriter);
            // 换行 每个个异常栈之间换行
            mPrintWriter.append("\r\n");
            mThrowable = mThrowable.getCause();
        }
        // 记得关闭
        mPrintWriter.close();
        return mWriter.toString();
    }

    /**
     * 获取应用包参数信息
     */
    private void collectPackageInfos(Context context) {
        try {
            // 获得包管理器
            PackageManager mPackageManager = context.getPackageManager();
            // 得到该应用的信息，即主Activity
            PackageInfo mPackageInfo = mPackageManager.getPackageInfo(context.getPackageName(), PackageManager
                    .GET_ACTIVITIES);
            if (mPackageInfo != null) {
                String versionName = mPackageInfo.versionName == null ? "null" : mPackageInfo.versionName;
                String versionCode = mPackageInfo.versionCode + "";
                AppInfoSB.append("VersionName: ").append(versionName).append("\n").append("VersionCode: ").append
                        (versionCode).append("\n");
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void dumpPhoneInfo() {
        //android版本号
        AppInfoSB.append("Android OS Version: ").append(Build.VERSION.RELEASE).append("_").append(Build.VERSION
                .SDK_INT).append("\n");

        //手机制造商
        AppInfoSB.append("Vendor: ").append(Build.MANUFACTURER).append("\n");

        //手机型号
        AppInfoSB.append("Model: ").append(Build.MODEL).append("\n");

        //cpu架构
        AppInfoSB.append("CPU ABI: ").append(Build.CPU_ABI).append("\n");
        appInfoMap.put(APP_INFOS_STRING, AppInfoSB.toString());
    }


    /**
     * 获取设备参数信息
     *
     * @param context
     */
    private void collectInfos(Context context, Throwable ex) {
        mExceptionInfos = collectExceptionInfos(ex);
        collectPackageInfos(context);
        dumpPhoneInfo();

        //将信息储存到一个总的Map中提供给上传动作回调
        infos.put(EXCEPETION_INFOS_STRING, mExceptionInfos);
        infos.put(APP_INFOS_STRING, appInfoMap);
    }

}
