package cn.edu.ncu.concurrent

import android.app.Service
import android.content.Intent
import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder

class PlayerService : Service() {

    private val playerBinder = PlayerBinder()

    private val mediaPlayer = MediaPlayer()

    override fun onBind(intent: Intent?): IBinder? {
        return playerBinder
    }

    inner class PlayerBinder: Binder() {

        fun isPlaying(): Boolean = mediaPlayer.isPlaying

        fun setDataSource(fd: AssetFileDescriptor) {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(fd.fileDescriptor, fd.startOffset, fd.length)
        }

        fun prepare() = mediaPlayer.prepare()

        fun start() {
            if (!mediaPlayer.isPlaying) {
                mediaPlayer.start()
            }
        }

        fun pause() {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
            }
        }

        fun seekTo(msec: Int) = mediaPlayer.seekTo(msec)

        fun reset() = mediaPlayer.reset()

        fun release() = mediaPlayer.release()
    }
}