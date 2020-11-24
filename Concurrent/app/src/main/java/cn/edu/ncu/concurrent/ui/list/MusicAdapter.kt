package cn.edu.ncu.concurrent.ui.list

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import cn.edu.ncu.concurrent.PlayerService
import cn.edu.ncu.concurrent.R
import cn.edu.ncu.concurrent.data.Music

class MusicAdapter(private val musics: List<Music>, private val service: PlayerService.PlayerBinder) :
    RecyclerView.Adapter<MusicAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val musicImg: ImageView = view.findViewById(R.id.musicImgView)
        val musicTitle: TextView = view.findViewById(R.id.musicTitle)
        val singer: TextView = view.findViewById(R.id.musicSinger)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
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
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.music_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val music = musics[position]
        holder.musicTitle.text = music.title
        holder.singer.text = music.artist
        holder.musicImg.setImageBitmap(music.imgBitMap)
    }

    override fun getItemCount() = musics.size
}