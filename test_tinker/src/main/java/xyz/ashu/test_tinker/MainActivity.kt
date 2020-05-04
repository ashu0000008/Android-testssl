package xyz.ashu.test_tinker

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.tencent.bugly.crashreport.CrashReport
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv_hello.setOnClickListener { tv_hello.text = "原始信息" }
        tv_crash.setOnClickListener { CrashReport.testJavaCrash() }
    }
}
