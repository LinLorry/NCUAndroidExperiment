package cn.edu.ncu.concurrent.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.edu.ncu.concurrent.R
import cn.edu.ncu.concurrent.data.Music

class MusicAdapter(private val musics: List<Music>) :
    RecyclerView.Adapter<MusicAdapter.ViewHolder>() {

    private var clickListener: View.OnClickListener? = null

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val musicImg: ImageView = view.findViewById(R.id.musicImgView)
        val musicTitle: TextView = view.findViewById(R.id.musicTitle)
        val singer: TextView = view.findViewById(R.id.musicSinger)
    }

    fun setOnClickListener(clickListener: View.OnClickListener) {
        this.clickListener = clickListener
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
        holder.itemView.setOnClickListener(clickListener)
    }

    override fun getItemCount() = musics.size
}