package cn.edu.ncu.psychologicaltest.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.edu.ncu.psychologicaltest.Option
import cn.edu.ncu.psychologicaltest.TestResult
import cn.edu.ncu.psychologicaltest.R
import kotlinx.android.synthetic.main.fragment_content.*
import java.lang.ClassCastException


class ContentFragment : Fragment() {

    private var resultReceiver: ResultReceiver? = null

    interface ResultReceiver {
        fun receiveFirstResult(firstResult: TestResult)

        fun receiveSecondResult(secondResult: TestResult)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_content, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ResultReceiver) {
            resultReceiver = context
        } else {
            throw ClassCastException(context::class.qualifiedName +
                    " must implement ResultReceiver")
        }
    }

    private fun invisibleAllLayout() {
        aboutText.visibility = View.INVISIBLE
        resultLayout.visibility = View.INVISIBLE
        testLayout.visibility = View.INVISIBLE
        firstTestRadioGroup.visibility = View.INVISIBLE
        secondTestRadioGroup.visibility = View.INVISIBLE
        finishHint.visibility = View.INVISIBLE
    }

    private fun calculateAndSendFirstResult(map: HashMap<Int, Int>) {
        map[firstAnswerA.id]?.let {
            when {
                it > 5 -> {
                    resultReceiver!!.receiveFirstResult(TestResult.THIRD)
                }
                it > 3 -> {
                    resultReceiver!!.receiveFirstResult(TestResult.SECOND)
                }
                else -> {
                    resultReceiver!!.receiveFirstResult(TestResult.FIRST)
                }
            }
        }
    }

    private fun calculateAndSendSecondResult(map: HashMap<Int, Int>) {
        map[secondAnswerA.id]?.let {
            when {
                it > 5 -> {
                    resultReceiver!!.receiveSecondResult(TestResult.THIRD)
                }
                it > 3 -> {
                    resultReceiver!!.receiveSecondResult(TestResult.SECOND)
                }
                else -> {
                    resultReceiver!!.receiveSecondResult(TestResult.FIRST)
                }
            }
        }
    }

    private fun finishTest() {
        invisibleAllLayout()
        finishHint.visibility = View.VISIBLE
    }

    private fun refreshFirstTest() {
        testLayout.visibility = View.VISIBLE
        firstTestRadioGroup.visibility = View.VISIBLE
        var num = 0
        val firstTestContent = resources.getStringArray(R.array.psychology_test1)
        val resultMap = hashMapOf(
            firstAnswerA.id to 0,
            firstAnswerB.id to 0,
            firstAnswerC.id to 0,
            firstAnswerD.id to 0,
        )

        testText.text = firstTestContent[num]

        firstTestRadioGroup.setOnCheckedChangeListener { group, id ->
            if (id == -1) return@setOnCheckedChangeListener

            resultMap.computeIfPresent(id) { _, value ->
                if (++num < firstTestContent.size) {
                    testText.text = firstTestContent[num]
                }
                value + 1
            }
            group.clearCheck()

            if (num == firstTestContent.size) {
                calculateAndSendFirstResult(resultMap)
                finishTest()
            }
        }
    }

    private fun refreshSecondTest() {
        testLayout.visibility = View.VISIBLE
        secondTestRadioGroup.visibility = View.VISIBLE
        var num = 0
        val secondTestContent = resources.getStringArray(R.array.psychology_test2)
        val resultMap = hashMapOf(
            secondAnswerA.id to 0,
            secondAnswerB.id to 0,
            secondAnswerC.id to 0,
        )

        testText.text = secondTestContent[num]
        secondTestRadioGroup.setOnCheckedChangeListener { group, id ->
            if (id == -1) return@setOnCheckedChangeListener

            resultMap.computeIfPresent(id) { _, value ->
                if (++num < secondTestContent.size) {
                    testText.text = secondTestContent[num]
                }
                value + 1
            }
            group.clearCheck()

            if (num == secondTestContent.size) {
                calculateAndSendSecondResult(resultMap)
                finishTest()
            }
        }
    }

    fun refresh(option: Option) {
        invisibleAllLayout()

        when (option) {
            Option.ABOUT -> aboutText.visibility = View.VISIBLE
            Option.FIRST_TEST -> refreshFirstTest()
            Option.SECOND_TEST -> refreshSecondTest()
            Option.RESULT -> resultLayout.visibility = View.VISIBLE
        }
    }

    fun refresh(firstResult: TestResult?, secondResult: TestResult?) {
        invisibleAllLayout()
        resultLayout.visibility = View.VISIBLE

        when (firstResult) {
            TestResult.FIRST -> {
                firstResultText.text = getString(R.string.psychology_test1_result_1)
            }
            TestResult.SECOND -> {
                firstResultText.text = getString(R.string.psychology_test1_result_2)
            }
            TestResult.THIRD -> {
                firstResultText.text = getString(R.string.psychology_test1_result_3)
            }
            else -> {
                firstResultText.text = getString(R.string.un_test_hint)
            }
        }

        when (secondResult) {
            TestResult.FIRST -> {
                secondResultText.text = getString(R.string.psychology_test2_result_1)
            }
            TestResult.SECOND -> {
                secondResultText.text = getString(R.string.psychology_test2_result_2)
            }
            TestResult.THIRD -> {
                secondResultText.text = getString(R.string.psychology_test2_result_3)
            }
            else -> {
                secondResultText.text = getString(R.string.un_test_hint)
            }
        }
    }
}