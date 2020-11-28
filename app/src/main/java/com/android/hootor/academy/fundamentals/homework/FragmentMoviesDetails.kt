package com.android.hootor.academy.fundamentals.homework

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView

class FragmentMoviesDetails : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_movies_details, container, false)

        val imButtonBack = view.findViewById<ImageView>(R.id.iv_button_back)
        imButtonBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        return view
    }

}