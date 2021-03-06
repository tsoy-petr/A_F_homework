package com.android.hootor.academy.fundamentals.homework

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.hootor.academy.fundamentals.homework.data.Movie
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.google.android.material.imageview.ShapeableImageView
import java.util.*

class MoviesAdapter constructor(private val listener: (Int) -> Unit) :
    RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }
    private val differ = AsyncListDiffer(this, diffCallback)
    fun submitList(list: List<Movie>) = differ.submitList(list)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.view_holder_movie,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = differ.currentList[position]
        holder.bind(movie, listener)
    }

    override fun getItemCount() = differ.currentList.size

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val siv: ShapeableImageView = itemView.findViewById(R.id.iv_shapeable)
        private val age: TextView = itemView.findViewById(R.id.tv_age)
        private val tags: TextView = itemView.findViewById(R.id.tv_tags)
        private val reviews: TextView = itemView.findViewById(R.id.tv_reviews)
        private val title: TextView = itemView.findViewById(R.id.tv_name)
        private val ratingBar: RatingBar = itemView.findViewById(R.id.rating_bar)
        private val runtime: TextView = itemView.findViewById(R.id.tv_age_limit)
        private val movieCardView: MaterialCardView = itemView.findViewById(R.id.movie_card_view)

        fun bind(movie: Movie, listener: (Int) -> Unit) {
            Glide.with(itemView).load(movie.poster).into(siv)
            age.text = "${movie.minimumAge} +"
            tags.text =
                movie.genres.joinToString(separator = " ") { it.name.capitalize(Locale.ROOT) }
            reviews.text = "${movie.numberOfRatings} REVIEWS"
            title.text = movie.title
            ratingBar.rating = (movie.ratings * 0.5).toFloat()
            runtime.text = "${movie.runtime} MIN"
            movieCardView.setOnClickListener {
                listener(movie.id)
            }
        }
    }

}