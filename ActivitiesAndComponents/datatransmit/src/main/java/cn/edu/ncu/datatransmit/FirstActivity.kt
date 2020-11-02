package cn.edu.ncu.datatransmit

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_first.*

class FirstActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)
//        val student = intent.getSerializableExtra("student") as StudentSerializable
        val student = intent.getParcelableExtra<StudentParcelable>("student")
        studentText.text = "学生的姓名为： ${student.name}, 学生的生日为： ${student.birthday}"
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