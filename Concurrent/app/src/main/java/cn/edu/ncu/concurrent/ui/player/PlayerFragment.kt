package cn.edu.ncu.concurrent.ui.player

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.edu.ncu.concurrent.LineSequence
import cn.edu.ncu.concurrent.MainActivity
import cn.edu.ncu.concurrent.MusicListDialog
import cn.edu.ncu.concurrent.R
import cn.edu.ncu.concurrent.data.Music


class PlayerFragment : Fragment() {

    private lateinit var playerViewModel: PlayerViewModel

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_player, container, false)
        val a = requireActivity() as MainActivity
        val service = a.playerBinder
        val music = service.playMusic.value ?: throw java.lang.NullPointerException("")
        playerViewModel = ViewModelProvider(this).get(PlayerViewModel::class.java)

        val musicImgAndLyricConstraintLayout: ConstraintLayout =
                root.findViewById(R.id.musicImgAndLyricConstraintLayout)
        val musicImg: ImageView = root.findViewById(R.id.musicImg)
        val playImg: ImageView = root.findViewById(R.id.playImg)
        val skipBackImg: ImageView = root.findViewById(R.id.skipBackImg)
        val skipForwardImg: ImageView = root.findViewById(R.id.skipForwardImg)
        val lineSequenceImg: ImageView = root.findViewById(R.id.lineSequenceImg)
        val menuImg: ImageView = root.findViewById(R.id.menuImg)
        val seekBar: SeekBar = root.findViewById(R.id.musicSeekBar)
        val currentPositionTextView: TextView = root.findViewById(R.id.currentPositionTextView)
        val durationTextView: TextView = root.findViewById(R.id.durationTextView)
        val musicLyricRecyclerView: RecyclerView = root.findViewById(R.id.musicLyricRecyclerView)
        val adapter = LyricAdapter(music, service)
        val musicListDialog: MusicListDialog? =  service.musicList.value?.let { musicList ->
            val dialog = MusicListDialog(a, musicList)
            dialog.setOnItemClickListener {
                val position = dialog.musicListRecyclerView.getChildAdapterPosition(it)
                if (service.setPosition(position)) {
                    service.prepare()
                    service.start()
                }
            }
            dialog.setOnRemoveClickListener {
                service.removeByPosition(it)
            }
            return@let dialog
        }


        val gestureDetector = GestureDetector(a,
                object : GestureDetector.SimpleOnGestureListener() {

                    override fun onDoubleTap(e: MotionEvent?): Boolean = changImgOrLyric()


                    fun changImgOrLyric(): Boolean {
                        playerViewModel.changeShow()
                        return true
                    }
                })

        musicLyricRecyclerView.layoutManager = object : LinearLayoutManager(activity) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }

        musicLyricRecyclerView.adapter = adapter

        setMusic(music, root, a.supportActionBar)

        service.duration.value?.let { seekBar.max = it }

        musicImgAndLyricConstraintLayout.setOnTouchListener { _, event -> gestureDetector.onTouchEvent(event) }

        musicLyricRecyclerView.setOnTouchListener { _, event -> gestureDetector.onTouchEvent(event) }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    service.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        playImg.setOnClickListener {
            if (service.isPlaying()) {
                service.pause()
            } else {
                service.start()
            }
        }

        skipBackImg.setOnClickListener { service.skipBack() }

        skipForwardImg.setOnClickListener { service.skipForward() }

        lineSequenceImg.setOnClickListener { service.nextLineSequence() }

        menuImg.setOnClickListener {
            musicListDialog?.show()
        }

        playerViewModel.showImgOrLyric.observe(viewLifecycleOwner) {
            if (it) {
                musicImg.visibility = View.INVISIBLE
                musicLyricRecyclerView.visibility = View.VISIBLE
            } else {
                musicImg.visibility = View.VISIBLE
                musicLyricRecyclerView.visibility = View.INVISIBLE
            }
        }

        service.playMusic.observe(viewLifecycleOwner) {
            setMusic(it, root, a.supportActionBar)
            adapter.music = it
            adapter.notifyDataSetChanged()
        }

        service.duration.observe(viewLifecycleOwner) {
            seekBar.max = it
            val minute = it / 60000
            val second = (it / 1000) % 60
            val text = String.format("%02d:%02d", minute, second)
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
            val text = String.format("%02d:%02d", minute, second)
            currentPositionTextView.text = text

            service.playMusic.value?.lyric?.getPositionByTimeRange(it)?.let { position ->
                (musicLyricRecyclerView.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(position, 0)
                adapter.notifyDataSetChanged()
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

        return root
    }

    private fun setMusic(music: Music, root: View, supportActionBar: ActionBar?) {
        val musicImg: ImageView = root.findViewById(R.id.musicImg)

        musicImg.setImageBitmap(music.imgBitMap)
        supportActionBar?.title = music.title
    }
}