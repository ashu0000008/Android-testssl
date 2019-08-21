package xyz.ashu.testssl;

import android.app.Application;
import android.content.Context;

/**
 * <pre>
 * author : zhouyang
 * time   : 2019/08/21
 * desc   :
 * version:
 * </pre>
 */
public class MyApplication extends Application {
    private static Context mContext = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext() {
        return mContext;
    }
}
