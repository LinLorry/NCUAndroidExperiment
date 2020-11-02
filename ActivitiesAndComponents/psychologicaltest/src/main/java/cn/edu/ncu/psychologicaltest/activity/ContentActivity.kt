package cn.edu.ncu.psychologicaltest.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import cn.edu.ncu.psychologicaltest.fragment.ContentFragment
import cn.edu.ncu.psychologicaltest.Option
import cn.edu.ncu.psychologicaltest.R
import cn.edu.ncu.psychologicaltest.TestResult
import cn.edu.ncu.psychologicaltest.common.BaseActivity
import kotlinx.android.synthetic.main.activity_content.*

class ContentActivity : BaseActivity(), ContentFragment.ResultReceiver {

    private var fResult: TestResult = TestResult.NONE
    private var sResult: TestResult = TestResult.NONE

    companion object {
        @JvmStatic
        fun actionStart(context: Context, option: Option) {
            val intent = Intent(context, ContentActivity::class.java).apply {
                putExtra("option", option.name)
            }
            context.startActivity(intent)
        }

        @JvmStatic
        fun actionStart(context: Context, firstResult: TestResult, secondResult: TestResult) {
            val intent = Intent(context, ContentActivity::class.java).apply {
                putExtra("option", Option.RESULT.name)
                putExtra("firstResult", firstResult.name)
                putExtra("secondResult", secondResult.name)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content)

        when (val option = intent.getStringExtra("option")) {
            Option.RESULT.name -> {
                val firstResult = intent.getStringExtra("firstResult")
                val secondResult = intent.getStringExtra("secondResult")
                if (firstResult != null && secondResult != null) {
                    (contentFragment as ContentFragment).refresh(
                        TestResult.valueOf(firstResult),
                        TestResult.valueOf(secondResult)
                    )
                }
            }
            else -> {
                (contentFragment as ContentFragment).refresh(
                    Option.valueOf(
                        option ?: Option.ABOUT.name
                    )
                )
            }
        }
    }

    override fun finish() {
        setResult(RESULT_OK, Intent().apply {
            putExtra("firstResult", fResult.name)
            putExtra("secondResult", sResult.name)
        })
        super.finish()
    }

    override fun receiveFirstResult(firstResult: TestResult) {
        fResult = firstResult
    }

    override fun receiveSecondResult(secondResult: TestResult) {
        sResult = secondResult
    }
}