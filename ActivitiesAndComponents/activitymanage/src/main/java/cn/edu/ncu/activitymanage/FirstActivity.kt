package cn.edu.ncu.activitymanage

import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_first.exit
import kotlinx.android.synthetic.main.activity_first.jumpToMain

class FirstActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)
//        returnTOMain.setOnClickListener { finish() }
        jumpToMain.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        exit.setOnClickListener {
            ActivityCollector.finishAll()
        }
    }
}