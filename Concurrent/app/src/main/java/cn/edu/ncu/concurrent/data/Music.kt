package cn.edu.ncu.concurrent.data

import android.graphics.Bitmap
import java.io.Serializable

data class Music(
        val path: String,
        val title: String, val album: String?,
        val mime: String?, val artist: String?,
        val date: String?, val imgBitMap: Bitmap,
        val lyric: Lyric?
) : Serializable