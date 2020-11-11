package cn.edu.ncu.musicplayer.ui.player

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cn.edu.ncu.musicplayer.data.Music

class PlayerViewModel : ViewModel() {

    private val music: MutableLiveData<Music> by lazy {
        MutableLiveData<Music>()
    }

    fun getMusic(): LiveData<Music> {
        return music
    }
}