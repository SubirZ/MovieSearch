package com.example.moviesearch.view.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.moviesearch.R;
import com.example.moviesearch.databinding.FragmentMovieDetailsBinding;
import com.example.moviesearch.model.pojo.movies.movie_details.Genre;
import com.example.moviesearch.model.pojo.movies.movie_details.MoviesDetails;
import com.example.moviesearch.model.repository.ProjectRepository;
import com.example.moviesearch.viewmodel.MovieDetailViewModel;

import static com.example.moviesearch.view.util.Constants.ID;

/**
 * Created by S.C. on 31/03/19.
 */

public class MovieDetailsFragment extends Fragment {
    private FragmentMovieDetailsBinding binding;
    private int movieId;
    private MoviesDetails movie;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie_details, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        movieId = getArguments().getInt(ID, 0);
        getMovieDetails();
    }

    /**
     * initialise observer to fetch movie details
     */
    private void getMovieDetails() {
        final MovieDetailViewModel viewModel = ViewModelProviders.of(this).get(MovieDetailViewModel.class);
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                viewModel.getMovieDetails(movieId);
                viewModel.getMovieDetailObservable().observe(MovieDetailsFragment.this, new Observer<MoviesDetails>() {
                    @Override
                    public void onChanged(@Nullable MoviesDetails moviesDetails) {
                        movie = moviesDetails;
                        binding.setMovie(moviesDetails);
                        updateView();
                    }
                });
            }
        };
        handler.postDelayed(runnable, 100);
    }

    private void updateView() {
        Glide.with(this).load(ProjectRepository.getInstance().WS_BASE_IMAGE_URL + movie.getPosterPath()).into(binding.fragmentMovieDetailsIvImage);
        if (movie.getBackdropPath() != null) {
            Glide.with(this).load(ProjectRepository.getInstance().WS_BASE_IMAGE_URL + movie.getBackdropPath()).into(binding.fragmentMovieDetailsIvImage);
        } else {
            Glide.with(this).load(ProjectRepository.getInstance().WS_BASE_IMAGE_URL + movie.getPosterPath()).into(binding.fragmentMovieDetailsIvImage);
        }
        binding.fragmentMovieDetailsTvOverview.setText(movie.getOverview());
        binding.fragmentMovieDetailsTvLength.setText(movie.getRuntime());
        binding.fragmentMovieDetailsTvReleaseDate.setText(movie.getReleaseDate());

        binding.fragmentMovieDetailsTvRating.setText(String.valueOf(movie.getVoteAverage()));
        binding.fragmentMovieDetailsTvLanguage.setText(movie.getOriginalLanguage());

        binding.fragmentMovieDetailsTvBudget.setText(movie.getBudget());
        binding.fragmentMovieDetailsTvRevenue.setText(movie.getRevenue());

        if (!movie.getGenres().isEmpty()) {
            String genres = "";
            for (Genre genre : movie.getGenres()) {
                if (genres.isEmpty()) {
                    genres = genre.getName();
                } else {
                    genres = genres + ", " + genre.getName();
                }
            }
        }

    }
}