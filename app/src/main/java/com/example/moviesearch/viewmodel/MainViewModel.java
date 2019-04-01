package com.example.moviesearch.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.os.Build;
import android.support.annotation.NonNull;

import com.example.moviesearch.model.pojo.movies.movies.MovieModel;
import com.example.moviesearch.model.repository.ProjectRepository;
import com.example.moviesearch.view.util.NetworkUtils;

import java.util.Objects;

public class MainViewModel extends AndroidViewModel {
    LiveData<MovieModel> discoverMoviesObservable;


    public MainViewModel(@NonNull Application application) {
        super(application);
        if (NetworkUtils.isOn(application)) {
            discoverMoviesObservable = ProjectRepository.getInstance().discoverMovieList("popularity.desc", "en-US");
        }
    }

    /**
     * Expose the LiveData movies query so the UI can observe it.
     */
    public LiveData<MovieModel> getDiscoverMoviesObservable() {
        return discoverMoviesObservable;
    }
}