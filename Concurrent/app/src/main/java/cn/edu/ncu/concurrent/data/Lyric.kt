package cn.edu.ncu.concurrent.data

import java.util.*
import kotlin.collections.ArrayList

class Lyric {

    private val map: TreeMap<Int, Int> = TreeMap()

    private val list: ArrayList<Pair<Int, ArrayList<String>>> = ArrayList()

    val size: Int
        get() = list.size

    fun add(time: Int, line: String): List<String> {
        val position = map[time]

        return if (position != null) {
            return list[position].second.apply {
                this.add(line)
            }
        } else {
            ArrayList<String>().apply {
                add(line)
                list.add(time to this)
                map[time] = list.size - 1
            }
        }
    }

    fun getPositionByTimeRange(time: Int): Int? = map.lowerEntry(time)?.value

    fun getByPosition(position: Int): Pair<Int, List<String>>? {
        return if (position < size) {
            list[position]
        } else {
            null
        }
    }
}