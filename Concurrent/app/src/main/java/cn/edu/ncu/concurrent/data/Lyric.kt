package cn.edu.ncu.concurrent.data

import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class Lyric {

    private val map: HashMap<Int, Int> = HashMap()

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

    fun getByTime(time: Int): List<String>? {
        val position = map[time]
        return if (position != null) {
            list[position].second
        } else {
            null
        }
    }

    fun getByPosition(position: Int): Pair<Int, List<String>>? {
        return if (position < size) {
            list[position]
        } else {
            null
        }
    }
}