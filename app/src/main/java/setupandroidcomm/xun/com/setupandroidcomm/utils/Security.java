package setupandroidcomm.xun.com.setupandroidcomm.utils;

/**
 * Created by xunwang on 2017/7/17.
 */

public class Security {
    static { // 加载libsecurity.so，只要在方法调用前加载，放哪都行。
        System.loadLibrary("security");
    }
    public static native String getSecret();
}
