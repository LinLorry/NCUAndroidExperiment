package cn.edu.ncu.activitiesandcomponents

import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_second.*
import kotlinx.android.synthetic.main.activity_second.exit
import kotlinx.android.synthetic.main.activity_second.jumpToFirst
import kotlinx.android.synthetic.main.activity_second.jumpToMain
import kotlinx.android.synthetic.main.activity_second.jumpToSecond

class SecondActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCollector.addActivity(this)
        setContentView(R.layout.activity_second)
//        returnTOMain.setOnClickListener { finish() }
        jumpToMain.setOnClickListener {
            startActivity(Intent(this, MainActivityKotlin::class.java))
        }
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