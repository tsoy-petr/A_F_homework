package com.android.hootor.academy.fundamentals.homework

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.hootor.academy.fundamentals.homework.data.MoviesRepository
import com.android.hootor.academy.fundamentals.homework.uiFith.GridAutofitLayoutManager
import com.android.hootor.academy.fundamentals.homework.uiFith.GridSpacesItemDecoration
import com.android.hootor.academy.fundamentals.homework.uiFith.Utils

class FragmentMoviesList : Fragment(R.layout.fragment_movies_list) {

    private var onClickListener: OnClickListener? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var movieAdapter: MoviesAdapter

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
        setupRecyclerView(view)
        movieAdapter.submitList(MoviesRepository.fitchMovies())
    }

    private fun setupRecyclerView(view: View) {
        movieAdapter = MoviesAdapter(::onClickItemMovie)
        recyclerView = view.findViewById(R.id.rv_movies)
        recyclerView.apply {
            adapter = movieAdapter
            layoutManager = GridAutofitLayoutManager(context, resources.getDimensionPixelSize(R.dimen.item_movie_width))
            addItemDecoration(GridSpacesItemDecoration(Utils.dpToPx(requireContext(),14), true))
        }
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