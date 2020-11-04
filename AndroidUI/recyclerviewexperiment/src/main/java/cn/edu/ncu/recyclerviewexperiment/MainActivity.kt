package cn.edu.ncu.recyclerviewexperiment

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.content_main.*
import java.util.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private val movieList = LinkedList<Movie>()
    private lateinit var recyclerViewAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        initMovie()

        val layoutManager = LinearLayoutManager(this)
        recyclerViewAdapter = MovieAdapter(movieList)

        movieRecyclerListView.layoutManager = layoutManager
        movieRecyclerListView.adapter = recyclerViewAdapter

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            movieRecyclerListView.smoothScrollToPosition(0)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val random = Random(System.currentTimeMillis())

        return when (item.itemId) {
            R.id.menuAdd -> {
                val movieNames = resources.getStringArray(R.array.filmName)
                val movieDescriptions = resources.getStringArray(R.array.filmDescription)
                val movieImagesIds = resources.obtainTypedArray(R.array.filmImage)

                val index = random.nextInt(movieNames.size)

                movieList.add(
                    Movie(
                        movieNames[index],
                        movieDescriptions[index],
                        movieImagesIds.getResourceId(index, -1)
                    )
                )
                recyclerViewAdapter.notifyDataSetChanged()
                movieImagesIds.recycle()
                true
            }
            R.id.menuRemove -> {
                if (movieList.size == 0) {
                    Toast.makeText(this, "没有电影可以删除了！", Toast.LENGTH_SHORT).show()
                } else {
                    movieList.removeAt(random.nextInt(movieList.size))
                    recyclerViewAdapter.notifyDataSetChanged()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initMovie() {
        val movieNames = resources.getStringArray(R.array.filmName)
        val movieDescriptions = resources.getStringArray(R.array.filmDescription)
        val movieImagesIds = resources.obtainTypedArray(R.array.filmImage)

        for (i in movieNames.indices) {
            movieList.add(
                Movie(
                    movieNames[i],
                    movieDescriptions[i],
                    movieImagesIds.getResourceId(i, -1)
                )
            )
        }
        movieImagesIds.recycle()
    }
}