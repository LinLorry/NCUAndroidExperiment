package cn.edu.ncu.musicplayer.ui.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.edu.ncu.musicplayer.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ListFragment : Fragment() {

    private lateinit var listViewModel: ListViewModel

    private lateinit var musicAdapter: MusicAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_list, container, false)

        val musicRecyclerView: RecyclerView = root.findViewById(R.id.musicRecyclerView)
        musicRecyclerView.layoutManager = LinearLayoutManager(activity)



        listViewModel =
            ViewModelProvider(this).get(ListViewModel::class.java)

        activity?.assets?.let {
            Log.d(this::class.simpleName, "load musics")
            listViewModel.loadMusics(it)
            listViewModel.getMusics().value?.let { v ->
                Log.d(this::class.simpleName, "musics size : ${v.size}")
                musicAdapter = MusicAdapter(v)
                musicRecyclerView.adapter = musicAdapter
            }
        }

        val fab: FloatingActionButton = root.findViewById(R.id.fab)
        fab.setOnClickListener {
            musicRecyclerView.smoothScrollToPosition(0)
        }

        return root
    }
}