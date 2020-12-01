package cn.edu.ncu.concurrent

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.edu.ncu.concurrent.data.Music


class MusicListDialog(context: Context, private val musicList: List<Music>) : Dialog(context, R.style.MusicListDialog) {

    private var itemClickListener: View.OnClickListener? = null

    private var removeClickListener: RemoveClickListener? = null

    fun interface RemoveClickListener {
        fun onClick(position: Int)
    }

    val musicListRecyclerView: RecyclerView by lazy {
        findViewById(R.id.musicListRecyclerView)
    }

    inner class Adapter(private val musics: List<Music>): RecyclerView.Adapter<Adapter.ViewHolder>() {
        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val titleTextView: TextView = view.findViewById(R.id.titleTextView)
            val removeImg: ImageView = view.findViewById(R.id.removeImg)

            fun setOnRemoveClickListener(l: RemoveClickListener) {
                removeImg.setOnClickListener {
                    l.onClick(adapterPosition)
                    notifyDataSetChanged()
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.dialog_music_item, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.titleTextView.text = musics[position].title
            itemClickListener?.let { holder.itemView.setOnClickListener(it) }
            removeClickListener?.let { holder.setOnRemoveClickListener(it) }
            if (itemCount == 1) {
                holder.removeImg.visibility = View.INVISIBLE
            }
        }

        override fun getItemCount(): Int = musics.size
    }


    fun setOnItemClickListener(l: View.OnClickListener) {
        itemClickListener = l
    }

    fun setOnRemoveClickListener(l: RemoveClickListener) {
        removeClickListener = l
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_music_list)
        musicListRecyclerView.layoutManager = LinearLayoutManager(context)
        musicListRecyclerView.adapter = Adapter(musicList)
    }

    override fun show() {
        super.show()
        window?.let {
            val layoutParams = it.attributes
            layoutParams.gravity = Gravity.BOTTOM
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT

            it.decorView.setPadding(0, 0, 0, 0)

            it.attributes = layoutParams
        }
    }
}