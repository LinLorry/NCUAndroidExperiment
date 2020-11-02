package cn.edu.ncu.callback

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
                    finish()
                }
                setNegativeButton("取消") { _, _ -> }
                show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}