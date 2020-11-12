package cn.edu.ncu.musicplayer.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.edu.ncu.musicplayer.MainViewModel
import cn.edu.ncu.musicplayer.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ListFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel

    private lateinit var musicAdapter: MusicAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_list, container, false)

        val musicRecyclerView: RecyclerView = root.findViewById(R.id.musicRecyclerView)
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