package cn.edu.ncu.listviewexperiment

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class MovieAdapter(activity: Activity, private val resourceId: Int, data: List<Movie>) :
    ArrayAdapter<Movie>(activity, resourceId, data) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(resourceId, parent, false)

        getItem(position)?.let {
            val movieName: TextView = view.findViewById(R.id.movieName)
            val movieDescription: TextView = view.findViewById(R.id.movieDescription)
            val movieImage: ImageView = view.findViewById(R.id.movieImage)

            movieName.text = it.name
            movieDescription.text = it.description
            movieImage.setImageResource(it.imageId)
        }

        return view
    }
}