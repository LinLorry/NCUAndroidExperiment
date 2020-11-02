package cn.edu.ncu.psychologicaltest.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import cn.edu.ncu.psychologicaltest.activity.ContentActivity
import cn.edu.ncu.psychologicaltest.Option
import cn.edu.ncu.psychologicaltest.R
import cn.edu.ncu.psychologicaltest.TestResult
import cn.edu.ncu.psychologicaltest.activity.MainActivity
import cn.edu.ncu.psychologicaltest.common.ActivityCollector
import kotlinx.android.synthetic.main.activity_content.*
import kotlinx.android.synthetic.main.fragment_option_list.*
import java.lang.ClassCastException


class OptionListFragment : Fragment() {

    private var isTwoPane = false

    private var resultReceiver: ContentFragment.ResultReceiver? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_option_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        isTwoPane = activity?.findViewById<View>(R.id.contentLayout) != null
        activity?.let { a ->
            if (isTwoPane) {
                val frag = contentFragment as ContentFragment

                firstTest.setOnClickListener { frag.refresh(Option.FIRST_TEST) }
                secondTest.setOnClickListener { frag.refresh(Option.SECOND_TEST) }
                about.setOnClickListener { frag.refresh(Option.ABOUT) }

                if (a is MainActivity) {
                    resultReceiver = a
                    result.setOnClickListener {
                        frag.refresh(a.getFResult(), a.getSResult())
                    }
                } else {
                    throw ClassCastException(
                        a::class.qualifiedName +
                                " must be Main Activity"
                    )
                }

            } else {
                firstTest.setOnClickListener {
                    startActivityForResult(
                        Intent(context, ContentActivity::class.java)
                            .apply {
                                putExtra("option", Option.FIRST_TEST.name)
                            }, 1
                    )
                }

                secondTest.setOnClickListener {
                    startActivityForResult(
                        Intent(context, ContentActivity::class.java)
                            .apply {
                                putExtra("option", Option.SECOND_TEST.name)
                            }, 2
                    )
                }

                about.setOnClickListener {
                    ContentActivity.actionStart(a, Option.ABOUT)
                }
                if (a is MainActivity) {
                    resultReceiver = a
                    result.setOnClickListener {
                        ContentActivity.actionStart(a, a.getFResult(), a.getSResult())
                    }
                } else {
                    throw ClassCastException(
                        a::class.qualifiedName +
                                " must be Main Activity"
                    )
                }
            }

            exit.setOnClickListener { ActivityCollector.finishAll() }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK) {
            when (requestCode) {
                1 -> {
                    data?.getStringExtra("firstResult")?.let {
                        resultReceiver!!.receiveFirstResult(TestResult.valueOf(it))
                    }
                }
                2 -> {
                    data?.getStringExtra("secondResult")?.let {
                        resultReceiver!!.receiveSecondResult(TestResult.valueOf(it))
                    }
                }
            }
        }
    }
}