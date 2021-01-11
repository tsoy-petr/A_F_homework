package com.android.hootor.academy.fundamentals.homework

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.android.hootor.academy.fundamentals.homework.data.loadMovies
import com.android.hootor.academy.fundamentals.homework.uifeature.GridAutofitLayoutManager
import com.android.hootor.academy.fundamentals.homework.uifeature.GridSpacesItemDecoration
import com.android.hootor.academy.fundamentals.homework.uifeature.Utils
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class FragmentMoviesList : Fragment(R.layout.fragment_movies_list) {

    private var onClickListener: OnClickListener? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var movieAdapter: MoviesAdapter
    private lateinit var containerMovieList: LinearLayout

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnClickListener) {
            onClickListener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        onClickListener = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        containerMovieList = view.findViewById(R.id.container_movie_list)
        lifecycleScope.launch {
            setupRecyclerView(view)
            try {
                val movies = loadMovies(requireContext())
                movieAdapter.submitList(movies)
            } catch (e: Exception) {
                showError(e.message)
            }
        }
    }

    private fun setupRecyclerView(view: View) {
        movieAdapter = MoviesAdapter(::onClickItemMovie)
        recyclerView = view.findViewById(R.id.rv_movies)
        recyclerView.apply {
            adapter = movieAdapter
            layoutManager = GridAutofitLayoutManager(
                context,
                resources.getDimensionPixelSize(R.dimen.item_movie_width)
            )
            addItemDecoration(GridSpacesItemDecoration(Utils.dpToPx(requireContext(), 14), true))
        }
    }

    private fun showError(message: String?) {
        Snackbar.make(
            containerMovieList,
            message ?: resources.getString(R.string.error_message),
            Snackbar.LENGTH_LONG
        ).show()
    }

    private fun onClickItemMovie(id: Int) {
        onClickListener?.onDataClicked(id)
    }

    companion object {
        fun newInstance(): FragmentMoviesList {
            return FragmentMoviesList()
        }
    }

    interface OnClickListener {
        fun onDataClicked(id: Int)
    }
}