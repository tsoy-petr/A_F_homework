package com.android.hootor.academy.fundamentals.homework

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.hootor.academy.fundamentals.homework.ui.FragmentMovieDetails
import com.android.hootor.academy.fundamentals.homework.ui.FragmentMoviesList

class MainActivity : AppCompatActivity(), FragmentMoviesList.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        makeStatusBarTransparent()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(
                    R.id.main_container,
                    FragmentMoviesList.newInstance()
                ).commit()
        }
    }

    override fun onMovieClicked(id: Int) {
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .add(
                R.id.main_container,
                FragmentMovieDetails.newInstance(id)
            ).commit()
    }
}