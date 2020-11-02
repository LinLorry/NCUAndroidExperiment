package cn.edu.ncu.datatransmit

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCollector.addActivity(this)
        Log.d(this::class.simpleName, "On Create")
        Log.d(this::class.simpleName, "Task id is $taskId")
    }

    override fun onStart() {
        super.onStart()
        Log.d(this::class.simpleName, "On Start")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(this::class.simpleName, "On ReStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(this::class.simpleName, "On Resume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(this::class.simpleName, "On Pause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(this::class.simpleName, "On Stop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(this::class.simpleName, "Task id is $taskId")
        Log.d(this::class.simpleName, "On Destroy")
        ActivityCollector.removeActivity(this)
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