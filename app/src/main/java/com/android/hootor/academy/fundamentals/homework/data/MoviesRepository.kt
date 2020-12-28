package com.android.hootor.academy.fundamentals.homework.data

object MoviesRepository {

    fun fitchMovies(count: Int = 10) = mutableListOf<Movie>().apply {
        (0 until count).map {
            add(
                Movie(
                    id = it + 1,
                    title = "Rogue City",
                    overview = "Caught in the crosshairs of police corruption and Marseille’s warring gangs, a loyal cop must protect his squad by taking matters into his own hands.",
                    poster = "https://image.tmdb.org/t/p/w1280/zU0htwkhNvBQdVSIKB9s6hgVeFK.jpg",
                    backdrop = "https://image.tmdb.org/t/p/w1066_and_h600_bestv2/34OGjFEbHj0E3lE2w0iTUVq0CBz.jpg",
                    ratings = 6.1F,
                    numberOfRatings = 200,
                    minimumAge = 13,
                    runtime = 116,
                    genres = listOf(
                        getGenresById(53),
                        getGenresById(28),
                        getGenresById(18),
                        getGenresById(80)
                    ),
                    actors = listOf(
                        getActors(84433),
                        getActors(37919),
                        getActors(1407184),
                        getActors(1003),
                        getActors(62439),
                        getActors(70165)
                    )
                )
            )
        }
    }

    fun fitchMovieById(id: Int): Movie? = fitchMovies().first { it.id == id }

    private fun getGenresById(id: Int) = mutableListOf<Genre>().apply {
        add(Genre(id = 28, name = "Action"))
        add(Genre(id = 12, name = "Adventure"))
        add(Genre(id = 53, name = "Thriller"))
        add(Genre(id = 80, name = "Crime"))
        add(Genre(id = 18, name = "Drama"))
    }.first { it.id == id }

    private fun getActors(id: Int) = mutableListOf<Actor>().apply {
        add(
            Actor(
                id = 84433,
                name = "Lannick Gautry",
                picture = "https://image.tmdb.org/t/p/w342/h94QUkKrwUncYhJ1eMz23kBAJuc.jpg"
            )
        )
        add(
            Actor(
                id = 37919,
                name = "Stanislas Merhar",
                picture = "https://image.tmdb.org/t/p/w342/k5XQM1XXG71GLd41hFcx8yhkxRy.jpg"
            )
        )
        add(
            Actor(
                id = 1407184,
                name = "Kaaris",
                picture = "https://image.tmdb.org/t/p/w342/zHxqojtMgk5aBqMQyTqcoPc4TRG.jpg"
            )
        )
        add(
            Actor(
                id = 1003,
                name = "Jean Reno",
                picture = "https://image.tmdb.org/t/p/w342/q7dYamebioHRuvb9EWeSw8yTEfS.jpg"
            )
        )
        add(
            Actor(
                id = 62439,
                name = "David Belle",
                picture = "https://image.tmdb.org/t/p/w342/nxRnf3O6Y3Yldd0pibuo43x3RZ.jpg"
            )
        )
        add(
            Actor(
                id = 70165,
                name = "Gérard Lanvin",
                picture = "https://image.tmdb.org/t/p/w342/3FSr4VW7JF3mBcVwPnJmOeR09NB.jpg"
            )
        )
    }.first { it.id == id }
}