package cn.edu.ncu.concurrent.ui.player

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import cn.edu.ncu.concurrent.LineSequence
import cn.edu.ncu.concurrent.MainActivity
import cn.edu.ncu.concurrent.R
import cn.edu.ncu.concurrent.data.Music


class PlayerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_player, container, false)

        val playImg: ImageView = root.findViewById(R.id.playImg)
        val skipBackImg: ImageView = root.findViewById(R.id.skipBackImg)
        val skipForwardImg: ImageView = root.findViewById(R.id.skipForwardImg)
        val lineSequenceImg: ImageView = root.findViewById(R.id.lineSequenceImg)
        val seekBar: SeekBar = root.findViewById(R.id.musicSeekBar)
        val currentPositionTextView: TextView = root.findViewById(R.id.currentPositionTextView)
        val durationTextView: TextView = root.findViewById(R.id.durationTextView)

        val a = requireActivity() as MainActivity
        val service = a.playerBinder
        val music = service.playMusic.value ?: throw java.lang.NullPointerException("")
        setMusic(music, root, a.supportActionBar)

        service.duration.value?.let { seekBar.max = it }
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    service.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) { }

            override fun onStopTrackingTouch(seekBar: SeekBar?) { }
        })

        playImg.setOnClickListener {
            if (service.isPlaying()) {
                service.pause()
            } else {
                service.start()
            }
        }

        skipBackImg.setOnClickListener {
            service.skipBack()
        }

        skipForwardImg.setOnClickListener {
            service.skipForward()
        }

        lineSequenceImg.setOnClickListener {
            service.nextLineSequence()
        }

        service.playMusic.observe(viewLifecycleOwner) {
            setMusic(it, root, a.supportActionBar)
        }

        service.duration.observe(viewLifecycleOwner) {
            seekBar.max = it
            val minute = it / 60000
            val second = (it / 1000) % 60
            val text =  String.format("%02d:%02d", minute, second)
            durationTextView.text = text
        }

        service.play.observe(viewLifecycleOwner) {
            if (it) {
                playImg.setImageResource(R.drawable.pause_line)
            } else {
                playImg.setImageResource(R.drawable.play_line)
            }
        }

        service.currentPosition.observe(viewLifecycleOwner) {
            seekBar.progress = it
            val minute = it / 60000
            val second = (it / 1000) % 60
            val text =  String.format("%02d:%02d", minute, second)
            currentPositionTextView.text = text
        }

        service.lineSequence.observe(viewLifecycleOwner) {
            when (it) {
                LineSequence.ORDER -> {
                    lineSequenceImg.setImageResource(R.drawable.repeat_line)
//                    Toast.makeText(activity, "shuffle", Toast.LENGTH_SHORT).show()
                }
                LineSequence.SHUFFLE -> {
                    lineSequenceImg.setImageResource(R.drawable.shuffle_line)
//                    Toast.makeText(activity, "loop one", Toast.LENGTH_SHORT).show()
                }
                LineSequence.REPEAT -> {
                    lineSequenceImg.setImageResource(R.drawable.repeat_one_line)
//                    Toast.makeText(activity, "loop all", Toast.LENGTH_SHORT).show()
                }
                null -> {}
            }
        }

        return root
    }

    private fun setMusic(music: Music, root: View, supportActionBar: ActionBar?) {
        val musicImg: ImageView = root.findViewById(R.id.musicImg)

        musicImg.setImageBitmap(music.imgBitMap)
        supportActionBar?.title = music.title
    }
}