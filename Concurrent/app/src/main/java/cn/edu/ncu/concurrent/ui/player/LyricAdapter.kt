package cn.edu.ncu.concurrent.ui.player

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.edu.ncu.concurrent.PlayerService
import cn.edu.ncu.concurrent.R
import cn.edu.ncu.concurrent.data.Music

class LyricAdapter(var music: Music, private val service: PlayerService.PlayerBinder)
    : RecyclerView.Adapter<LyricAdapter.ViewHolder>() {

    open inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val lyricTextView: TextView = view.findViewById(R.id.lyricTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LyricAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.lyric_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: LyricAdapter.ViewHolder, position: Int) {
        val lyric = music.lyric
        val paint = holder.lyricTextView.paint

        lyric?.let {
            if (position < 3) {
                lyric.getByPosition(0)?.let {
                    holder.lyricTextView.text = when (it.second.size) {
                        1 -> "\n"
                        2 -> "\n\n"
                        else -> ""
                    }
                }
                paint.isFakeBoldText = false
                return
            }


            lyric.getByPosition(position - 3)?.let {
                val line = it.second
                val strBuilder = StringBuilder()

                for (l in line) {
                    strBuilder.append(l)
                    strBuilder.append("\n")
                }

                holder.lyricTextView.text = strBuilder.toString()
                service.currentPosition.value?.let { cp ->
                    val p = lyric.getPositionByTimeRange(cp)
                    if (p == position - 3) {
                        paint.isFakeBoldText = true
//                        paint.color = Color.RED
                        holder.lyricTextView.setTextColor(Color.BLACK)
                    } else {
//                        paint.color = Color.GRAY
                        paint.isFakeBoldText = false
                        holder.lyricTextView.setTextColor(Color.GRAY)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        val lyric = music.lyric ?: return 0
        return lyric.size + 3
    }
}