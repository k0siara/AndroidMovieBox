package com.patrykkosieradzki.moviebox.ui.details

import org.junit.Test

class MovieDetailsDialogFragmentShould : RobotTest<MovieDetailsDialogFragmentRobot>() {

    @Test
    fun showMovieListScreen() {
        withRobot {
            startFragment()
            capture("02_Movie_Details")
        }
    }

    override fun createRobot() =
        MovieDetailsDialogFragmentRobot()
}

class MovieDetailsDialogFragmentRobot :
    FragmentScenarioRobot<MovieDetailsViewState, MovieDetailsViewModel>() {
    fun startFragment() {
        startDialogFragment { MovieDetailsDialogFragment() }
    }
}
