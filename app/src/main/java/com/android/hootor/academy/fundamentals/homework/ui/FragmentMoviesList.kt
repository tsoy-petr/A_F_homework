package com.android.hootor.academy.fundamentals.homework.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.hootor.academy.fundamentals.homework.R
import com.android.hootor.academy.fundamentals.homework.uifeature.GridAutofitLayoutManager
import com.android.hootor.academy.fundamentals.homework.uifeature.GridSpacesItemDecoration
import com.android.hootor.academy.fundamentals.homework.uifeature.Utils
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
class FragmentMoviesList : Fragment(R.layout.fragment_movies_list) {

    private val model: MoviesViewModel by activityViewModels()
    private var uiStateJob: Job? = null

    private var onClickListener: OnClickListener? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var movieAdapter: MoviesAdapter
    private lateinit var containerMovieList: FrameLayout
    private lateinit var progressBar: ProgressBar

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnClickListener) {
            onClickListener = context
        }
    }

    override fun onDetach() {
        onClickListener = null
        super.onDetach()
    }

    @ExperimentalSerializationApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        containerMovieList = view.findViewById(R.id.container_movie_list)
        progressBar = view.findViewById(R.id.progressBar)
        setupRecyclerView(view)
        model.apply {
            uiStateJob = lifecycleScope.launch {
                uiState.collect {
                    when (it.fetchStatus) {
                        is FetchStatus.AddMore -> {
                            this@FragmentMoviesList.movieAdapter.submitList(it.data)
                            showLoading(false)
                        }
                        is FetchStatus.Loading -> {
                            showLoading(true)
                        }
                        is FetchStatus.Success -> {
                            showLoading(false)
                        }
                        is FetchStatus.ShowError -> {
                            showError(message = it.fetchStatus.message)
                            showLoading(false)
                        }
                    }
                }
            }
        }
    }

    private fun setupRecyclerView(view: View) {

        movieAdapter = MoviesAdapter(
            listener = ::onClickItemMovie
        )
        recyclerView = view.findViewById(R.id.rv_movies)
        recyclerView.apply {
            adapter = movieAdapter
            layoutManager = GridAutofitLayoutManager(
                context,
                resources.getDimensionPixelSize(R.dimen.item_movie_width)
            )
            addItemDecoration(GridSpacesItemDecoration(Utils.dpToPx(requireContext(), 14), true))
        }

        val scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount

                model.onLoadMoreTriggered(
                    firstVisibleItemPosition,
                    visibleItemCount,
                    totalItemCount
                )
            }
        }
        recyclerView.addOnScrollListener(scrollListener)
    }

    override fun onDestroy() {
        uiStateJob?.cancel()
        super.onDestroy()
    }

    private fun showError(message: String) {
        Snackbar.make(
            containerMovieList,
            message,
            Snackbar.LENGTH_LONG
        ).show()
    }

    private fun showLoading(flag: Boolean) {
        when (flag) {
            true -> progressBar.visibility = View.VISIBLE
            false -> progressBar.visibility = View.INVISIBLE
        }
    }

    private fun onClickItemMovie(id: Int) {
        onClickListener?.onMovieClicked(id)
    }

    companion object {
        fun newInstance(): FragmentMoviesList {
            return FragmentMoviesList()
        }
    }

    interface OnClickListener {
        fun onMovieClicked(movieId: Int)
    }
}
