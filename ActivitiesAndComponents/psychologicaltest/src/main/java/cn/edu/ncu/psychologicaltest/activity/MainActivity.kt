package cn.edu.ncu.psychologicaltest.activity

import android.os.Bundle
import cn.edu.ncu.psychologicaltest.R
import cn.edu.ncu.psychologicaltest.TestResult
import cn.edu.ncu.psychologicaltest.common.BaseActivity
import cn.edu.ncu.psychologicaltest.fragment.ContentFragment

class MainActivity : BaseActivity(), ContentFragment.ResultReceiver {

    private var fResult: TestResult = TestResult.NONE
    private var sResult: TestResult = TestResult.NONE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun getFResult(): TestResult {
        return fResult
    }

    fun getSResult(): TestResult {
        return sResult
    }

    override fun receiveFirstResult(firstResult: TestResult) {
        fResult = firstResult
    }

    override fun receiveSecondResult(secondResult: TestResult) {
        sResult = secondResult
    }
}