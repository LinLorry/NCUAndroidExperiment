package cn.edu.ncu.musicplayer.data

import android.graphics.Bitmap
import java.io.Serializable

data class Music(
        val title: String, val album: String?,
        val mime: String?, val artist: String?,
        val date: String?, val imgBitMap: Bitmap
) : Serializable