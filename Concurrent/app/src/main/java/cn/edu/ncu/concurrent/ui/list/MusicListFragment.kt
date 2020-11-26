package cn.edu.ncu.concurrent.ui.list

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.edu.ncu.concurrent.MainActivity
import cn.edu.ncu.concurrent.MainViewModel
import cn.edu.ncu.concurrent.R
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MusicListFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel

    private lateinit var musicAdapter: MusicAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_music_list, container, false)

        val musicRecyclerView: RecyclerView = root.findViewById(R.id.musicListRecyclerView)
        musicRecyclerView.layoutManager = LinearLayoutManager(activity)

        val a = activity as MainActivity
        val service = a.playerBinder
        mainViewModel = ViewModelProvider(a).get(MainViewModel::class.java)

        mainViewModel.musics.value?.let { musics ->
            musicAdapter = MusicAdapter(musics)
            musicAdapter.setOnClickListener {
                val position = musicRecyclerView.getChildAdapterPosition(it)
                if (position != RecyclerView.NO_POSITION) {

                    if (position != service.position.value) {
                        service.setPlayMusicList(musics)
                        service.pause()

                        if (service.setPosition(position)) {
                            service.prepare()
                            service.start()
                        } else {
                            Log.d(this::class.simpleName, "Set service music failed")
                        }
                    }

                    it.findNavController().navigate(R.id.action_nav_music_list_to_playerFragment)
                }
            }
            musicRecyclerView.adapter = musicAdapter
        }

        val fab: FloatingActionButton = root.findViewById(R.id.fab)
        fab.setOnClickListener {
            musicRecyclerView.smoothScrollToPosition(0)
        }

        return root
    }
}