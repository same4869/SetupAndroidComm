package setupandroidcomm.xun.com.setupandroidcomm.net;

/**
 * Created by xunwang on 17/5/31.
 */

public interface IRequestCallback {
    void onSuccess(String response);

    void onFailure(Throwable throwable);
}
