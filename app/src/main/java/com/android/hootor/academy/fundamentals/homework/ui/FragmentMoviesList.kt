package com.android.hootor.academy.fundamentals.homework.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.hootor.academy.fundamentals.homework.R
import com.android.hootor.academy.fundamentals.homework.ui.pagination.MoviesPaginationScrollListener
import com.android.hootor.academy.fundamentals.homework.uifeature.GridAutofitLayoutManager
import com.android.hootor.academy.fundamentals.homework.uifeature.GridSpacesItemDecoration
import com.android.hootor.academy.fundamentals.homework.uifeature.Utils
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.serialization.ExperimentalSerializationApi

class FragmentMoviesList : Fragment(R.layout.fragment_movies_list) {

    private val model: MoviesViewModel by activityViewModels()
    private var uiStateJob: Job? = null
    private var isLoading = false

    private var onClickListener: OnClickListener? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var movieAdapter: MoviesAdapter
    private lateinit var containerMovieList: FrameLayout
    private lateinit var progressBar: ProgressBar

    private var scrollListener: MoviesPaginationScrollListener? = null

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

    @ExperimentalSerializationApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        containerMovieList = view.findViewById(R.id.container_movie_list)
        progressBar = view.findViewById(R.id.progressBar)
        setupRecyclerView(view, savedInstanceState)
        model.apply {
            uiStateJob = lifecycleScope.launchWhenCreated {
                uiState.collect {
                    when (it.fetchStatus) {
                        is FetchStatus.AddMore -> {
                            movieAdapter.submitList(it.data)
                            showLoading(false)
                        }
                        is FetchStatus.Loading -> {
                            showLoading(true)
                        }
                        is FetchStatus.Success -> {
                            showLoading(false)
                        }
                        is FetchStatus.ShowError -> {
                            showError(it.fetchStatus.message)
                            showLoading(false)
                        }
                    }
                }
            }
        }
    }

    private fun setupRecyclerView(view: View, bundle: Bundle?) {

        var currentPage = 1
        var previousTotalItemCount = 0
        var startingPageIndex = 1

        if (bundle != null) {
            currentPage = bundle.getInt(CURRENT_PAGE_KEY, currentPage)
            previousTotalItemCount = bundle.getInt(PREVIOUS_TOTAL_ITEM_COUNT_KEY, previousTotalItemCount)
            startingPageIndex = bundle.getInt(STARTING_PAGE_INDEX_KEY, startingPageIndex)
        }

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
        scrollListener = object :
            MoviesPaginationScrollListener(
                currentPage, previousTotalItemCount, startingPageIndex, recyclerView.layoutManager as GridLayoutManager
            ) {
            override fun isLoading() = isLoading

            override fun loadMoreItems(currentPage: Int) {
                isLoading = true
                model.loadPagePopular(currentPage)
            }

        }
        recyclerView.addOnScrollListener(scrollListener as MoviesPaginationScrollListener)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        scrollListener?.apply {
            outState.putInt(CURRENT_PAGE_KEY, currentPage)
            outState.putInt(PREVIOUS_TOTAL_ITEM_COUNT_KEY, previousTotalItemCount)
            outState.putInt(STARTING_PAGE_INDEX_KEY, startingPageIndex)
        }
    }

    override fun onStop() {
        uiStateJob?.cancel()
        super.onStop()
    }

    private fun showError(message: String) {
        Snackbar.make(
            containerMovieList,
            message,
            Snackbar.LENGTH_LONG
        ).show()
    }

    private fun showLoading(flag: Boolean) {
        isLoading = flag
        when (flag) {
            true -> progressBar.visibility = View.VISIBLE
            false -> progressBar.visibility = View.INVISIBLE
        }
    }

    private fun onClickItemMovie(id: Int) {
        onClickListener?.onDataClicked(id)
    }

    companion object {

        const val CURRENT_PAGE_KEY = "currentPage"
        const val PREVIOUS_TOTAL_ITEM_COUNT_KEY = "previousTotalItemCount"
        const val STARTING_PAGE_INDEX_KEY = "startingPageIndex"

        fun newInstance(): FragmentMoviesList {
            return FragmentMoviesList()
        }
    }

    interface OnClickListener {
        fun onDataClicked(id: Int)
    }
}
