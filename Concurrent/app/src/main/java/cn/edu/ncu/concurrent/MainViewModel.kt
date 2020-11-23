package cn.edu.ncu.concurrent

import android.content.res.AssetManager
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cn.edu.ncu.concurrent.data.Lyric
import cn.edu.ncu.concurrent.data.Music
import org.json.JSONObject
import java.nio.charset.Charset
import java.util.regex.Pattern
import kotlin.collections.ArrayList

class MainViewModel : ViewModel() {

    val musics: LiveData<List<Music>>
        get() = _musics

    private val _musicList = ArrayList<Music>()

    private val _musics: MutableLiveData<List<Music>> = MutableLiveData(_musicList)

    private companion object {
        val pattern: Pattern = Pattern.compile("\\[([0-9])*?:([0-9]*?)\\.([0-9]*?)](.*)")
    }

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
                    val lyric = getMusicLyric("lyric/${musicName.split(".")[0]}", assetManager)

                    _musicList.add(Music(path, title, album, mime, artist, date, imgBitMap, lyric))
                } catch (e: Exception) {
                    Log.d(this::class.simpleName, e.toString())
                }
            }
        }
    }

    private fun getMusicLyric(path: String, assetManager: AssetManager): Lyric? {

        try {
            val reader = assetManager.open(path)
                    .reader(Charset.forName("UTF-8"))
            val lines = reader.readLines()
            if (lines.isEmpty()) return null
            val json = JSONObject(lines[0])
            val lyricContext = json.getString("lyric")
            val m = pattern.matcher(lyricContext)

            val lyric = Lyric()

            while (m.find()) {
                val minute = (m.group(1)?.toInt()) ?: 0

                val second = (m.group(2)?.toInt()) ?: 0
                val ms = (m.group(3)?.toInt()) ?: 0
                val time = minute * 60 * 1000 + second * 1000 + ms
                val lyricLine = m.group(4) ?: ""

                lyric.add(time, lyricLine)
            }

            val translateLyric = json.getString("translateLyric")
            val m2 = pattern.matcher(translateLyric)
            while (m2.find()) {
                val minute = (m2.group(1)?.toInt()) ?: 0

                val second = (m2.group(2)?.toInt()) ?: 0
                val ms = (m2.group(3)?.toInt()) ?: 0
                val time = minute * 60 * 1000 + second * 1000 + ms
                val lyricLine = m2.group(4) ?: ""

                lyric.add(time, lyricLine)
            }

            return lyric
        } catch (e: Exception) {
            Log.e(this::class.simpleName, e.stackTraceToString())
            return null
        }
    }
}