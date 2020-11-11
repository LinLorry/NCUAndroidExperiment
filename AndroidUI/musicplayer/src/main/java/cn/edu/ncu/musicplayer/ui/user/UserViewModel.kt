package cn.edu.ncu.musicplayer.ui.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cn.edu.ncu.musicplayer.data.User

class UserViewModel : ViewModel() {

    private val user: MutableLiveData<User> by lazy {
        MutableLiveData<User>().also {
            loadUser(it)
        }
    }

    fun getUser(): LiveData<User> {
        return user
    }

    private fun loadUser(mutableLiveData: MutableLiveData<User>): MutableLiveData<User> {
        mutableLiveData.value = User("username")

        return mutableLiveData
    }
}