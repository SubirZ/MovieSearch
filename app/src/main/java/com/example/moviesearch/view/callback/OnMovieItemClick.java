package com.example.moviesearch.view.callback;

import com.example.moviesearch.model.pojo.movies.MovieDetails;

/**
 * Created by S.C. on 31-03-19.
 */

public interface OnMovieItemClick {

    void onClick(int position, MovieDetails moviesDetails);
}
