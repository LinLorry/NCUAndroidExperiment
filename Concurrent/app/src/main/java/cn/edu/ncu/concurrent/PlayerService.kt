package cn.edu.ncu.concurrent

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cn.edu.ncu.concurrent.data.Music
import java.util.*
import kotlin.random.Random

class PlayerService : Service() {

    companion object {
        const val NOTIFY_ID = 1
    }

    private val random = Random(System.currentTimeMillis())

    private val _position: MutableLiveData<Int> = MutableLiveData(null)

    private val _playMusic: MutableLiveData<Music> = MutableLiveData()

    private val _musicList: MutableLiveData<List<Music>> = MutableLiveData(LinkedList())

    private val _play: MutableLiveData<Boolean> = MutableLiveData(false)

    private val _lineSequence: MutableLiveData<LineSequence> = MutableLiveData(LineSequence.ORDER)

    private val _currentPosition: MutableLiveData<Int> = MutableLiveData()

    private val _duration: MutableLiveData<Int> = MutableLiveData()

    private val playerBinder = PlayerBinder()

    private val mediaPlayer = MediaPlayer()

    private val playerReceiver = PlayerReceiver()

    private var notify = true

    private lateinit var views: RemoteViews

    private val notificationManager: NotificationManager by lazy {
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    private val notificationBuilder: NotificationCompat.Builder by lazy {
        NotificationCompat.Builder(this, "player")
                .setShowWhen(false)
                .setAutoCancel(false)
                .setOnlyAlertOnce(true)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
    }

    private val changeCurrentRunnable = Runnable {
        while (true) {
            _currentPosition.postValue(mediaPlayer.currentPosition)
            try {
                Thread.sleep(300)
            } catch (e: InterruptedException) {
                _currentPosition.postValue(mediaPlayer.currentPosition)
                break
            }
        }
    }

    private var changeCurrentThread: Thread = Thread(changeCurrentRunnable)

    private val playIntent: PendingIntent by lazy {
        PendingIntent.getBroadcast(this, 1,
                Intent("cn.edy.ncu.concurrent.PlayService.play").apply { setPackage(packageName) },
                PendingIntent.FLAG_UPDATE_CURRENT)
    }
    private val skipForwardIntent: PendingIntent by lazy {
        PendingIntent.getBroadcast(this, 1,
                Intent("cn.edy.ncu.concurrent.PlayService.skipForward").apply { setPackage(packageName) },
                PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private val skipBackIntent: PendingIntent by lazy {
        PendingIntent.getBroadcast(this, 1,
                Intent("cn.edy.ncu.concurrent.PlayService.skipBack").apply { setPackage(packageName) },
                PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun initRemoteViews() {
        views = RemoteViews(packageName, R.layout.player_notification)

        views.setOnClickPendingIntent(R.id.playImg, playIntent)
        views.setOnClickPendingIntent(R.id.skipForwardImg, skipForwardIntent)
        views.setOnClickPendingIntent(R.id.skipBackImg, skipBackIntent)
        _playMusic.value?.let {
            views.setTextViewText(R.id.title, it.title)
            views.setTextViewText(R.id.artist, it.artist)
            views.setImageViewBitmap(R.id.musicImg, it.imgBitMap)
        }
        notificationBuilder.setContent(views)
    }

    override fun onCreate() {
        mediaPlayer.setOnCompletionListener {
            playerBinder.skipForward()
        }
        val channel = NotificationChannel("player", "播放器",
                NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(channel)

        val intentFilter =  IntentFilter().apply {
            addAction("cn.edy.ncu.concurrent.PlayService.play")
            addAction("cn.edy.ncu.concurrent.PlayService.skipForward")
            addAction("cn.edy.ncu.concurrent.PlayService.skipBack")
        }

        registerReceiver(playerReceiver, intentFilter)

        super.onCreate()
    }

    override fun onBind(intent: Intent?): IBinder {
        return playerBinder
    }

    override fun onDestroy() {
        super.onDestroy()
        changeCurrentThread.interrupt()
        mediaPlayer.release()
        unregisterReceiver(playerReceiver)
        stopForeground(true)
    }

    inner class PlayerBinder : Binder() {

        val position: LiveData<Int>
            get() = _position

        val musicList: LiveData<List<Music>>
            get() = _musicList

        val playMusic: LiveData<Music>
            get() = _playMusic

        val play: LiveData<Boolean>
            get() = _play

        val lineSequence: LiveData<LineSequence>
            get() = _lineSequence

        val currentPosition: LiveData<Int>
            get() = _currentPosition

        val duration: LiveData<Int>
            get() = _duration

        fun setPosition(position: Int) : Boolean {
            val value = _musicList.value

            if (value == null || position > value.size) {
                return false
            }
            _position.value = position
            val music = value[position]

            _playMusic.value = music

            val fd = assets.openFd(music.path)

            mediaPlayer.reset()
            mediaPlayer.setDataSource(fd.fileDescriptor, fd.startOffset, fd.length)

            initRemoteViews()

            return true
        }

        fun setPlayMusicList(playMusicList: List<Music>) {
            val musics = _musicList.value as LinkedList<Music>
            musics.clear()
            for (music in playMusicList) {
                musics.add(music)
            }
        }

        fun isPlaying(): Boolean = mediaPlayer.isPlaying

        fun prepare() = mediaPlayer.prepare()

        fun start(): Boolean {
            if (!mediaPlayer.isPlaying) {
                _duration.value = mediaPlayer.duration
                _play.value = true
                mediaPlayer.start()

                changeCurrentThread.interrupt()
                changeCurrentThread = Thread(changeCurrentRunnable)
                changeCurrentThread.start()
                views.setImageViewResource(R.id.playImg, R.drawable.pause_line)
                if (notify) {
                    startForeground(NOTIFY_ID, notificationBuilder.build())
                    notify = !notify
                } else {
                    notificationManager.notify(NOTIFY_ID, notificationBuilder.build())
                }
            }
            return true
        }

        fun pause() {
            if (mediaPlayer.isPlaying) {
                _play.value = false
                mediaPlayer.pause()
                changeCurrentThread.interrupt()
                views.setImageViewResource(R.id.playImg, R.drawable.play_line)
                notificationManager.notify(NOTIFY_ID, notificationBuilder.build())
            }
        }

        fun seekTo(msec: Int) = mediaPlayer.seekTo(msec)

        fun skipForward(): Boolean = changePlay(true)

        fun skipBack(): Boolean = changePlay(false)

        private fun changePlay(p: Boolean): Boolean {
            val musics = _musicList.value

            if (musics == null) {
                Log.e(this::class.simpleName, "Player service music list is null")
                return false
            }

            var position = this.position.value!!

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
                prepare()
                start()
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

    inner class PlayerReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                "cn.edy.ncu.concurrent.PlayService.play" -> {
                    if (mediaPlayer.isPlaying) {
                        playerBinder.pause()
                    } else {
                        playerBinder.start()
                    }
                }
                "cn.edy.ncu.concurrent.PlayService.skipForward" -> {
                    playerBinder.skipForward()
                }
                "cn.edy.ncu.concurrent.PlayService.skipBack" -> {
                    playerBinder.skipBack()
                }
            }
        }
    }
}