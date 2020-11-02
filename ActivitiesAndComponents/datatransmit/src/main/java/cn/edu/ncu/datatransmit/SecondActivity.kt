package cn.edu.ncu.datatransmit

import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_second.exit
import kotlinx.android.synthetic.main.activity_second.jumpToMain

class SecondActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCollector.addActivity(this)
        setContentView(R.layout.activity_second)
//        returnTOMain.setOnClickListener { finish() }
        jumpToMain.setOnClickListener {
            setResult(RESULT_OK)
            finish()
        }
        exit.setOnClickListener {
            ActivityCollector.finishAll()
        }
    }
}