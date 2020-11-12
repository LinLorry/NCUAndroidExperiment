package cn.edu.ncu.musicplayer.ui.player

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import cn.edu.ncu.musicplayer.LineSequence
import cn.edu.ncu.musicplayer.MainViewModel
import cn.edu.ncu.musicplayer.R

class PlayerFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_player, container, false)
        val musicImg: ImageView = root.findViewById(R.id.musicImg)
        val musicTitle: TextView = root.findViewById(R.id.musicTitle)
        val playImg: ImageView = root.findViewById(R.id.playImg)
        val lineSequenceImg: ImageView = root.findViewById(R.id.lineSequenceImg)

        val a = activity ?: throw NullPointerException("List Fragment activity is null")
        mainViewModel =
                ViewModelProvider(a).get(MainViewModel::class.java)


        val music = mainViewModel.playMusic.value ?: throw java.lang.NullPointerException("")

        musicImg.setImageBitmap(music.imgBitMap)
        musicTitle.text = music.title

        playImg.setOnClickListener {
            mainViewModel.playAndPause()
        }

        lineSequenceImg.setOnClickListener {
            mainViewModel.nextLineSequence()
        }

        mainViewModel.play.observe(viewLifecycleOwner) {
            if (it) {
                playImg.setImageResource(R.drawable.pause_line)
            } else {
                playImg.setImageResource(R.drawable.play_line)
            }
        }
        mainViewModel.lineSequence.observe(viewLifecycleOwner) {
            when (it) {
                LineSequence.ORDER -> {
                    lineSequenceImg.setImageResource(R.drawable.shuffle_line)
//                    Toast.makeText(activity, "shuffle", Toast.LENGTH_SHORT).show()
                }
                LineSequence.SHUFFLE -> {
                    lineSequenceImg.setImageResource(R.drawable.repeat_one_line)
//                    Toast.makeText(activity, "loop one", Toast.LENGTH_SHORT).show()
                }
                LineSequence.REPEAT -> {
                    lineSequenceImg.setImageResource(R.drawable.repeat_line)
//                    Toast.makeText(activity, "loop all", Toast.LENGTH_SHORT).show()
                }
                null -> {}
            }
        }

        /*favImg.setOnClickListener {
            if (fav) {
                favImg.setImageResource(R.drawable.collection_fill)
            } else {
                favImg.setImageResource(R.drawable.favorite)
            }
            fav = !fav
        }*/

        return root
    }
}