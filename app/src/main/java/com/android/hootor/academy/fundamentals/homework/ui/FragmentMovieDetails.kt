package com.android.hootor.academy.fundamentals.homework.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.hootor.academy.fundamentals.homework.R
import com.android.hootor.academy.fundamentals.homework.domain.models.Movie
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import java.util.*

class FragmentMovieDetails : Fragment() {

    private val model: MovieDetailViewModel by viewModels()
    private var uiStateJob: Job? = null
    private lateinit var actorsAdapter: ActorsAdapter
    private lateinit var containerMovieDetail: ConstraintLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.fragment_movies_details,
            container,
            false
        )
        view.findViewById<ImageView>(R.id.iv_button_back).setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        view.findViewById<TextView>(R.id.tv_back).setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        containerMovieDetail = view.findViewById(R.id.container_movie_detail)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView(view.findViewById(R.id.rv_actors))
        if (arguments?.containsKey(KEY_ID) == true) {
            val idMovie = arguments?.getInt(KEY_ID, -1) ?: -1
            idMovie.let { id ->
                model.fetchMovie(id)
                uiStateJob = lifecycleScope.launchWhenCreated{
                    model.flow.collectLatest { movie ->
                        movie?.also {
                            setupView(view, it)
                            actorsAdapter.submitList(it.actors)
                        }
                    }
                    model.error.collectLatest {
                        showError(it)
                    }
                }
            }
        } else {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun showError(message: String?) {
        Snackbar.make(
            containerMovieDetail,
            message ?: resources.getString(R.string.error_message),
            Snackbar.LENGTH_LONG
        ).show()
    }

    private fun setupRecyclerView(rv_actors: RecyclerView) {
        actorsAdapter = ActorsAdapter()
        rv_actors.apply {
            adapter = actorsAdapter
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            val decorator = DividerItemDecoration(requireContext(), LinearLayout.HORIZONTAL)
            ContextCompat.getDrawable(requireContext(), R.drawable.divider_drawable)?.let {
                decorator.setDrawable(it)
            }
            addItemDecoration(decorator)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupView(view: View, movie: Movie) {
        val header = view.findViewById<ImageView>(R.id.iv_header)
        val name = view.findViewById<TextView>(R.id.tv_name)
        val age = view.findViewById<TextView>(R.id.tv_age)
        val tagLine = view.findViewById<TextView>(R.id.tv_tagLine)
        val ratingBar = view.findViewById<RatingBar>(R.id.rating_bar)
        val reviews = view.findViewById<TextView>(R.id.tv_reviews)
        val description = view.findViewById<TextView>(R.id.tv_description)
        val cast = view.findViewById<TextView>(R.id.tv_cast)

        movie.backdrop?.also {
            val backdropUrl = "https://image.tmdb.org/t/p/original$it"
            Glide.with(requireContext())
                .load(backdropUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(header)

        }
        name.text = movie.title
        age.text = "${movie.minimumAge} +"
        tagLine.text =
            movie.genres.joinToString(separator = " ") { it.name.capitalize(Locale.ROOT) }
        ratingBar.rating = (movie.ratings * 0.5).toFloat()
        reviews.text = "${movie.numberOfRatings} Reviews"
        description.text = movie.overview
        cast.visibility = when (movie.actors.size) {
            0 -> View.GONE
            else -> View.VISIBLE
        }
    }

    override fun onStop() {
        uiStateJob?.cancel()
        super.onStop()
    }

    companion object {

        private const val KEY_ID = "id"

        fun newInstance(id: Int): FragmentMovieDetails {
            val args = Bundle()
            args.putInt(KEY_ID, id)
            return FragmentMovieDetails().apply {
                arguments = args
            }
        }
    }
}