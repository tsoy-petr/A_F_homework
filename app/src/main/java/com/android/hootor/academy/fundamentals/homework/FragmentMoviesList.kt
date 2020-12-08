package com.android.hootor.academy.fundamentals.homework

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.card.MaterialCardView

class FragmentMoviesList : Fragment() {

    private var onClickListener: OnClickListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            onClickListener = context as OnClickListener
        } else {
            throw RuntimeException("$context must implement FragmentMoviesList.OnClickListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_movies_list, container, false)
        val movieCardView = view.findViewById<MaterialCardView>(R.id.movie_card_view)
        movieCardView.setOnClickListener {
            onClickListener?.onClickData()
        }
        return view
    }

    companion object {
        fun newInstance():FragmentMoviesList {
            return FragmentMoviesList()
        }
    }

    interface OnClickListener{
        fun onClickData(): Unit
    }
}