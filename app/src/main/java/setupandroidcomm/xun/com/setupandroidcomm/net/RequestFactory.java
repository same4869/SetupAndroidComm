package setupandroidcomm.xun.com.setupandroidcomm.net;

/**
 * Created by xunwang on 17/5/31.
 */

public class RequestFactory {
    public enum RequestType{
        VOLLEY, OKHTTP
    }

    public static IRequestManager getRequestManager(RequestType requestType){
        switch (requestType){
            case VOLLEY:
                return VolleyRequestManager.getInstance();
            case OKHTTP:
                return OkHttpRequestManager.getInstance();
            default:
                return VolleyRequestManager.getInstance();
        }
    }
}
