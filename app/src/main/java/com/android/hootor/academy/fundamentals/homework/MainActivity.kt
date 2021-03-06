package com.android.hootor.academy.fundamentals.homework

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

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

    override fun onDataClicked(id: Int) {
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .add(
                R.id.main_container,
                FragmentMoviesDetails.newInstance(id)
            ).commit()
    }
}