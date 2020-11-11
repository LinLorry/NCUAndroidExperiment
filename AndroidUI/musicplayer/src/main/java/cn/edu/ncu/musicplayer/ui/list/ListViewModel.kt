package cn.edu.ncu.musicplayer.ui.list

import android.content.res.AssetManager
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.media.MediaMetadataRetriever.*
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cn.edu.ncu.musicplayer.data.Music

class ListViewModel : ViewModel() {

    private val musicList = ArrayList<Music>()

    private val musics: MutableLiveData<List<Music>> by lazy {
        MutableLiveData<List<Music>>().also {
            it.value = musicList
        }
    }

    fun getMusics(): LiveData<List<Music>> {
        return musics
    }

    fun loadMusics(assetManager: AssetManager) {
        val musicNames = assetManager.list("musics")
        Log.d(this::class.simpleName, musicNames.toString())
        if (musicNames != null) {


            Log.d(this::class.simpleName, "is not null")
            Log.d(this::class.simpleName, "path size: ${musicNames.size}")

            val mmr = MediaMetadataRetriever()
            for (musicName in musicNames) {
                try {
                    Log.d(this::class.simpleName, musicName)
                    val fd = assetManager.openFd("musics/$musicName")
                    /*Log.d(this::class.simpleName, fd.toString())
                    Log.d(this::class.simpleName, fd.fileDescriptor.toString())*/

                    mmr.setDataSource(
                            fd.fileDescriptor, fd.startOffset, fd.length
                    )

                    val title = mmr.extractMetadata(METADATA_KEY_TITLE)
                    val album = mmr.extractMetadata(METADATA_KEY_ALBUM)
                    val mime = mmr.extractMetadata(METADATA_KEY_MIMETYPE)
                    val artist = mmr.extractMetadata(METADATA_KEY_ARTIST)
                    val date = mmr.extractMetadata(METADATA_KEY_DATE)

                    val bits = mmr.embeddedPicture
                    val imgBitMap = BitmapFactory.decodeByteArray(bits, 0, bits.size)
                    musicList.add(Music(title, album, mime, artist, date, imgBitMap))
                } catch (e: Exception) {
                    Log.d(this::class.simpleName, e.toString())
                }
            }
        }
    }
}