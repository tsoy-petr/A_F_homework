package com.android.hootor.academy.fundamentals.homework.ui.pagination

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.hootor.academy.fundamentals.homework.R
import com.android.hootor.academy.fundamentals.homework.domain.models.Movie
import com.android.hootor.academy.fundamentals.homework.ui.MoviesAdapter

class MoviesPaginationAdapter(
    private val movies: MutableList<Movie> = mutableListOf(),
    private val listener: (Int) -> Unit
) : RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MoviesAdapter.MovieViewHolder {
        return MoviesAdapter.MovieViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.view_holder_movie,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MoviesAdapter.MovieViewHolder, position: Int) {
        val movie: Movie = movies[position]
        holder.bind(movie, listener)
    }

    override fun getItemCount() = movies.size

    fun addAll(nextPart: List<Movie>) {
        movies.addAll(nextPart)
        notifyItemRangeChanged(
            movies.size - nextPart.size,
            movies.size
        )
    }
}