package cn.edu.ncu.concurrent

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cn.edu.ncu.concurrent.data.Music
import kotlin.random.Random

class PlayerService : Service() {

    private var position: Int = 0

    private val random = Random(System.currentTimeMillis())

    private val _playMusic: MutableLiveData<Music> = MutableLiveData()

    private val _musicList: MutableLiveData<List<Music>> = MutableLiveData()

    private val _play: MutableLiveData<Boolean> = MutableLiveData(false)

    private val _lineSequence: MutableLiveData<LineSequence> = MutableLiveData(LineSequence.ORDER)

    private val playerBinder = PlayerBinder()

    private val mediaPlayer = MediaPlayer()

    override fun onCreate() {
        mediaPlayer.setOnCompletionListener {
            playerBinder.skipForward()
        }
        super.onCreate()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return playerBinder
    }

    inner class PlayerBinder : Binder() {

        val musicList: LiveData<List<Music>>
            get() = _musicList

        val playMusic: LiveData<Music>
            get() = _playMusic

        val play: LiveData<Boolean>
            get() = _play

        val lineSequence: LiveData<LineSequence>
            get() = _lineSequence

        fun setPosition(position: Int) : Boolean {
            val value = _musicList.value

            if (value == null || position > value.size) {
                return false
            }
            this@PlayerService.position = position
            val music = value[position]

            _playMusic.value = music

            val fd = assets.openFd(music.path)
            mediaPlayer.setDataSource(fd.fileDescriptor, fd.startOffset, fd.length)

            return true
        }

        fun setPlayMusicList(playMusicList: List<Music>) {
            _musicList.value = playMusicList
        }

        fun isPlaying(): Boolean = mediaPlayer.isPlaying

        fun prepare() = mediaPlayer.prepare()

        fun start() {
            if (!mediaPlayer.isPlaying) {
                _play.value = true
                mediaPlayer.start()
            }
        }

        fun pause() {
            if (mediaPlayer.isPlaying) {
                _play.value = false
                mediaPlayer.pause()
            }
        }

        fun seekTo(msec: Int) = mediaPlayer.seekTo(msec)

        fun reset() {
            if (_playMusic.value != null) {
                _playMusic.value = null
                mediaPlayer.reset()
            }
        }

        fun release() = mediaPlayer.release()

        fun skipForward(): Boolean = changePlay(true)

        fun skipBack(): Boolean = changePlay(false)

        private fun changePlay(p: Boolean): Boolean {
            val musics = _musicList.value

            if (musics == null) {
                Log.e(this::class.simpleName, "Player service music list is null")
                return false
            }

            var position = this@PlayerService.position

            when (lineSequence.value) {
                LineSequence.ORDER -> {
                    if (p) {
                        if (++position == musics.size) {
                            position = 0
                        }
                    } else if (--position < 0) {
                        position = musics.size - 1
                    }
                }
                LineSequence.REPEAT -> {
                    mediaPlayer.seekTo(0)
                    return true
                }
                LineSequence.SHUFFLE -> {
                    val next = random.nextInt(musics.size)
                    position += next
                    if (position > musics.size) {
                        position -= musics.size
                    }
                }
                else -> {
                    Log.e(this::class.simpleName, "Illegal LineSequence")
                    return false
                }
            }

            mediaPlayer.pause()
            mediaPlayer.reset()

            return if (setPosition(position)) {
                mediaPlayer.prepare()
                mediaPlayer.start()
                true
            } else {
                false
            }
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
}