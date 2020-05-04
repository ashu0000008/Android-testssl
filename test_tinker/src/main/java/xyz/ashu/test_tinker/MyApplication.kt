package xyz.ashu.test_tinker

import android.app.Application
import com.tencent.bugly.Bugly

/**
 * <pre>
 * author : zhouyang
 * time   : 2020/05/04
 * desc   :
 * version:
 * </pre>
 */
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Bugly.init(this, "825e26ede6", true)
    }
}