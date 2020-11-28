package com.android.hootor.academy.fundamentals.homework

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.card.MaterialCardView

class FragmentMoviesList : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_movies_list, container, false)

        val movieCardView = view.findViewById<MaterialCardView>(R.id.movie_card_view)
        movieCardView.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .addToBackStack(null)
                .add(
                    R.id.main_container,
                    FragmentMoviesDetails()
                )
                .commit()
        }


        return view
    }
}