package cn.edu.ncu.activitymanage

import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        jumpToFirst.setOnClickListener {
            startActivity(Intent(this, FirstActivity::class.java))
        }
        jumpToSecond.setOnClickListener {
            startActivity(Intent(this, SecondActivity::class.java))
        }
        exit.setOnClickListener {
            ActivityCollector.finishAll()
        }
    }
}