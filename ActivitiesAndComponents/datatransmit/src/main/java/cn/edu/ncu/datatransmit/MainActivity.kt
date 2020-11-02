package cn.edu.ncu.datatransmit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDate

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        jumpToFirst.setOnClickListener {
            startActivityForResult(
                Intent(this, FirstActivity::class.java).apply {
                    putExtras(
                        Bundle().apply {
                            /*putSerializable(
                                "student",
                                StudentSerializable(
                                    "张三", LocalDate.of(1999, 2, 23)
                                ),
                            )*/
                            putParcelable(
                                "student",
                                StudentParcelable(
                                    "张三", LocalDate.of(1999, 2, 23)
                                ),
                            )
                        })
                }, 1
            )
        }
        jumpToSecond.setOnClickListener {
            startActivityForResult(Intent(this, SecondActivity::class.java), 1)
        }
        exit.setOnClickListener {
            ActivityCollector.finishAll()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1 -> {
                AlertDialog.Builder(this).apply {
                    setTitle("从第一个页面返回")
                    setPositiveButton("OK") { _, _ -> }
                    show()
                }
            }
            2 -> {
                AlertDialog.Builder(this).apply {
                    setTitle("从第二个页面返回")
                    setPositiveButton("OK") { _, _ -> }
                    show()
                }
            }
        }
    }
}