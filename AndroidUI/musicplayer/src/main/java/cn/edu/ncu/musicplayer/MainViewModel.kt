package cn.edu.ncu.musicplayer

import android.content.res.AssetManager
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cn.edu.ncu.musicplayer.data.Music

class MainViewModel : ViewModel() {

    val musics: LiveData<List<Music>>
        get() = _musics

    val playMusic: LiveData<Music>
        get() = _playMusic

    val play: LiveData<Boolean>
        get() = _play

    /*val fav: LiveData<Boolean>
    get() = */

    val lineSequence: LiveData<LineSequence>
        get() = _lineSequence

    private val _musicList = ArrayList<Music>()

    private val _playMusic: MutableLiveData<Music> = MutableLiveData()

    private val _musics: MutableLiveData<List<Music>> = MutableLiveData(_musicList)

    private val _play: MutableLiveData<Boolean> = MutableLiveData(false)

    private val _lineSequence: MutableLiveData<LineSequence> = MutableLiveData(LineSequence.ORDER)

    fun loadMusics(assetManager: AssetManager) {
        val musicNames = assetManager.list("musics")
        Log.d(this::class.simpleName, musicNames.toString())
        if (musicNames != null) {
            val mmr = MediaMetadataRetriever()
            for (musicName in musicNames) {
                try {
                    Log.d(this::class.simpleName, musicName)
                    val fd = assetManager.openFd("musics/$musicName")

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
                    _musicList.add(Music(title, album, mime, artist, date, imgBitMap))
                } catch (e: Exception) {
                    Log.d(this::class.simpleName, e.toString())
                }
            }
        }
    }

    fun setMusic(position: Int) {
        _playMusic.value = _musicList[position]
    }

    fun playAndPause() {
        _play.value = !_play.value!!
    }

    fun nextLineSequence() {
        when (_lineSequence.value) {
            LineSequence.ORDER -> {
                _lineSequence.value = LineSequence.SHUFFLE
            }
            LineSequence.SHUFFLE -> {
                _lineSequence.value = LineSequence.REPEAT
            }
            LineSequence.REPEAT -> {
                _lineSequence.value = LineSequence.ORDER
            }
        }
    }
}