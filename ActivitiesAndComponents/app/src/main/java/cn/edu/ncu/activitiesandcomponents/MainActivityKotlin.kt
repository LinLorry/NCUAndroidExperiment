package cn.edu.ncu.activitiesandcomponents

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivityKotlin : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.configure -> Toast
                .makeText(this, "You click Configure", Toast.LENGTH_SHORT)
                .show()
            R.id.exit -> AlertDialog.Builder(this).apply {
                setMessage("你确定要退出应用吗？")
                setPositiveButton("确定") { _, _ ->
                    ActivityCollector.finishAll()
                }
                setNegativeButton("取消") { _, _ -> }
                show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        Log.d(this::class.simpleName, "onRestoreInstanceState")
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.d(this::class.simpleName, "onSaveInstanceState")
        super.onSaveInstanceState(outState)
    }
}