package cn.edu.ncu.concurrent

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import cn.edu.ncu.musicplayer.BaseActivity
import com.google.android.material.navigation.NavigationView

class MainActivity : BaseActivity() {

    lateinit var playerBinder: PlayerService.PlayerBinder

    private var bind = false

    private lateinit var mainViewModel: MainViewModel

    private lateinit var appBarConfiguration: AppBarConfiguration

    private val connection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            playerBinder = service as PlayerService.PlayerBinder

            /*val music = mainViewModel.playMusic.value
                    ?: throw  NullPointerException("Start player with null music")

            val fd = assets.openFd(music.path)

            playerBinder.reset()
            playerBinder.setDataSource(fd)
            playerBinder.prepare()*/
            bind = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            bind = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_home, R.id.nav_user, R.id.nav_music_list
        ), drawerLayout)

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        bindService(Intent(this, PlayerService::class.java), connection, Context.BIND_AUTO_CREATE)

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        mainViewModel.loadMusics(assets)
        mainViewModel.playMusic.observe(this, {
            if (bind) {
                val fd = assets.openFd(it.path)

                playerBinder.reset()
                playerBinder.setDataSource(fd)
                playerBinder.prepare()
                if (mainViewModel.isPlaying()) {
                    playerBinder.start()
                } else {
                    mainViewModel.start()
                }

            }
        })

        mainViewModel.play.observe(this, {
            if (bind) {
                if (it && !playerBinder.isPlaying()) {
                    playerBinder.start()
                } else if (playerBinder.isPlaying()) {
                    playerBinder.pause()
                }
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}