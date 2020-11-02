package cn.edu.ncu.activitiesandcomponents

import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_first.*
import kotlinx.android.synthetic.main.activity_first.exit
import kotlinx.android.synthetic.main.activity_first.jumpToFirst
import kotlinx.android.synthetic.main.activity_first.jumpToMain
import kotlinx.android.synthetic.main.activity_first.jumpToSecond
import kotlinx.android.synthetic.main.activity_main.*

class FirstActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)
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