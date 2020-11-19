package cn.edu.ncu.concurrent.ui.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

        activity.let {
            if (it == null) {
                throw NullPointerException("List Fragment activity is null")
            }
            mainViewModel =
                ViewModelProvider(it).get(MainViewModel::class.java)
            mainViewModel.musics.value
        }?.let {
            musicAdapter = MusicAdapter(it, mainViewModel)
            musicRecyclerView.adapter = musicAdapter
        }

        val fab: FloatingActionButton = root.findViewById(R.id.fab)
        fab.setOnClickListener {
            musicRecyclerView.smoothScrollToPosition(0)
        }

        return root
    }
}