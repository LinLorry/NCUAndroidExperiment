package cn.edu.ncu.concurrent.ui.player

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PlayerViewModel : ViewModel() {

    val showImgOrLyric:LiveData<Boolean>
        get() = _showImgOrLyric

    private val _showImgOrLyric = MutableLiveData(false)

    fun changeShow() {
        _showImgOrLyric.value = !_showImgOrLyric.value!!
    }
}