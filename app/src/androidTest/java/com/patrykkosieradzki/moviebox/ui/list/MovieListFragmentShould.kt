package com.patrykkosieradzki.moviebox.ui.list

import org.junit.Test

class MovieListFragmentShould : RobotTest<MovieListFragmentRobot>() {

    @Test
    fun showMovieListScreen() {
        withRobot {
            startFragment()
            capture("01_Movie_List")
        }
    }

    override fun createRobot() = MovieListFragmentRobot()
}

class MovieListFragmentRobot() : FragmentScenarioRobot<MovieListViewState, MovieListViewModel>() {
    fun startFragment() {
        startFragment { MovieListFragment() }
    }
}
