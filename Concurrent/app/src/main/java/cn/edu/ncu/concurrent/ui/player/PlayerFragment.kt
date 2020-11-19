package cn.edu.ncu.concurrent.ui.player

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
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

        val a = activity as MainActivity
        val service = a.playerBinder
        val music = service.playMusic.value ?: throw java.lang.NullPointerException("")

        setMusic(music, root)

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
            setMusic(it, root)
        }

        service.play.observe(viewLifecycleOwner) {
            if (it) {
                playImg.setImageResource(R.drawable.pause_line)
            } else {
                playImg.setImageResource(R.drawable.play_line)
            }
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

    private fun setMusic(music: Music, root: View) {
        val musicImg: ImageView = root.findViewById(R.id.musicImg)
        val musicTitle: TextView = root.findViewById(R.id.musicTitle)

        musicImg.setImageBitmap(music.imgBitMap)
        musicTitle.text = music.title
    }
}