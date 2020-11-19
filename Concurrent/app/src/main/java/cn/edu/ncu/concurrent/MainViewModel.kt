package cn.edu.ncu.concurrent

import android.content.res.AssetManager
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cn.edu.ncu.concurrent.data.Music

class MainViewModel : ViewModel() {

    val musics: LiveData<List<Music>>
        get() = _musics

    private val _musicList = ArrayList<Music>()

    private val _musics: MutableLiveData<List<Music>> = MutableLiveData(_musicList)

    fun loadMusics(assetManager: AssetManager) {
        val musicNames = assetManager.list("musics")
        if (musicNames != null) {
            val mmr = MediaMetadataRetriever()
            for (musicName in musicNames) {
                try {
                    val path = "musics/$musicName"
                    val fd = assetManager.openFd(path)
                    mmr.setDataSource(
                            fd.fileDescriptor, fd.startOffset, fd.length
                    )

                    val title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
                    val album = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)
                    val mime = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE)
                    val artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
                    val date = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DATE)
                    val bits = mmr.embeddedPicture
                    val imgBitMap = BitmapFactory.decodeByteArray(bits, 0, bits.size)

                    _musicList.add(Music(path, title, album, mime, artist, date, imgBitMap))
                } catch (e: Exception) {
                    Log.d(this::class.simpleName, e.toString())
                }
            }
        }
    }
}