package setupandroidcomm.xun.com.setupandroidcomm.net;

/**
 * Created by xunwang on 17/5/31.
 */

public interface IRequestManager {

    void get(String url, IRequestCallback requestCallback);

    void post(String url, String requestBodyJson, IRequestCallback requestCallback);

    void put(String url, String requestBodyJson, IRequestCallback requestCallback);

    void delete(String url, String requestBodyJson, IRequestCallback requestCallback);
}
