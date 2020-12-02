package cn.edu.ncu.concurrent

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.setPadding
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView

class MainActivity : BaseActivity() {

    lateinit var playerBinder: PlayerService.PlayerBinder

    private lateinit var mainViewModel: MainViewModel

    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var musicListDialog: MusicListDialog

    private val connection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            playerBinder = service as PlayerService.PlayerBinder

            musicListDialog = playerBinder.musicList.value.let { musicList ->
                val dialog = MusicListDialog(this@MainActivity, musicList!!)
                dialog.setOnItemClickListener {
                    val position = dialog.musicListRecyclerView.getChildAdapterPosition(it)
                    if (playerBinder.setPosition(position)) {
                        playerBinder.prepare()
                        playerBinder.start()
                    }
                }
                dialog.setOnRemoveClickListener {
                    playerBinder.removeByPosition(it)
                }
                return@let dialog
            }

            val navHostFragment: ViewGroup = findViewById(R.id.nav_host_fragment)
            val bottomBarLayout: ConstraintLayout = findViewById(R.id.bottomBarLayout)
            val title: TextView = bottomBarLayout.findViewById(R.id.title)
            val artist: TextView = bottomBarLayout.findViewById(R.id.artist)
            val musicImageView: ImageView = bottomBarLayout.findViewById(R.id.musicImageView)
            val playImageView: ImageView = bottomBarLayout.findViewById(R.id.playImageView)
            val skipForwardImageView: ImageView = bottomBarLayout.findViewById(R.id.skipForwardImageView)
            val menuImageView: ImageView = bottomBarLayout.findViewById(R.id.menuImageView)
            val bottomBarHeight: Int = resources.getDimension(R.dimen.bottom_height).toInt()

            bottomBarLayout.setOnClickListener {
                findNavController(R.id.nav_host_fragment).navigate(R.id.playerFragment)
            }
            skipForwardImageView.setOnClickListener { playerBinder.skipForward() }
            menuImageView.setOnClickListener { musicListDialog.show() }
            playImageView.setOnClickListener {
                if (playerBinder.isPlaying()) {
                    playerBinder.pause()
                } else {
                    playerBinder.start()
                }
            }

            playerBinder.playMusic.observe(this@MainActivity) {
                title.text = it.title
                artist.text = it.artist
                musicImageView.setImageBitmap(it.imgBitMap)
            }
            playerBinder.play.observe(this@MainActivity) {
                if (it) {
                    playImageView.setImageResource(R.drawable.pause_line)
                } else {
                    playImageView.setImageResource(R.drawable.play_line)
                }
            }

            findNavController(R.id.nav_host_fragment).addOnDestinationChangedListener { _, destination, _ ->
                playerBinder.playMusic.value?.let {
                    when (destination.id) {
                        R.id.playerFragment -> {
                            bottomBarLayout.visibility = View.INVISIBLE
                            navHostFragment.setPadding(0)
                        }
                        else -> {
                            bottomBarLayout.visibility = View.VISIBLE
                            navHostFragment.setPadding(0, 0, 0, bottomBarHeight)
                        }
                    }
                }
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {}
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

        bindService(Intent(this, PlayerService::class.java),
            connection, Context.BIND_AUTO_CREATE)

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        mainViewModel.loadMusics(assets)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}