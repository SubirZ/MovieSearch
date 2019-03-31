package com.example.moviesearch.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.moviesearch.model.pojo.movies.movie_details.MoviesDetails;
import com.example.moviesearch.model.repository.ProjectRepository;

/**
 * Created by S.C. on 31/03/19.
 */

public class MovieDetailViewModel extends AndroidViewModel {
    private LiveData<MoviesDetails> movieDetailObservable;

    public MovieDetailViewModel(@NonNull Application application) {
        super(application);
    }

    /**
     * Expose the LiveData movies query so the UI can observe it.
     */
    public LiveData<MoviesDetails> getMovieDetailObservable() {
        return movieDetailObservable;
    }

    public void getMovieDetails(int movieId) {
        movieDetailObservable = ProjectRepository.getInstance().movieDetails(movieId);
    }
}
