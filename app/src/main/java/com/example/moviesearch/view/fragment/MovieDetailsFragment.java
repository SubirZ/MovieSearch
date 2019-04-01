package com.example.moviesearch.view.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.moviesearch.R;
import com.example.moviesearch.databinding.FragmentMovieDetailsBinding;
import com.example.moviesearch.model.pojo.movies.movie_details.Genre;
import com.example.moviesearch.model.pojo.movies.movie_details.MoviesDetails;
import com.example.moviesearch.model.repository.ProjectRepository;
import com.example.moviesearch.view.activity.MainActivity;
import com.example.moviesearch.view.util.DateUtils;
import com.example.moviesearch.view.util.NetworkUtils;
import com.example.moviesearch.viewmodel.MovieDetailViewModel;

import static com.example.moviesearch.view.util.Constants.ID;
import static com.example.moviesearch.view.util.Constants.NAME;

/**
 * Created by S.C. on 31/03/19.
 */

public class MovieDetailsFragment extends Fragment {
    private FragmentMovieDetailsBinding binding;
    private int movieId;
    private String movieName;
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
        movieName = getArguments().getString(NAME, "");
        initView();
    }

    void initView() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.fragmentMovieDetailsToolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(movieName);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.fragmentMovieDetailsToolbar.setNavigationIcon(R.drawable.ic_arrow_back);

        setHasOptionsMenu(true);
        Glide.with(this).load(R.drawable.shimmer).into(binding.fragmentMovieDetailsIvLoader);
        if (NetworkUtils.isOn(getActivity())) {
            getMovieDetails();
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
        }
        return super.onOptionsItemSelected(item);
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
                        binding.fragmentMovieDetailsIvLoader.setVisibility(View.GONE);
                    }
                });
            }
        };
        handler.postDelayed(runnable, 100);
    }

    /**
     * update the view after loading the data
     */
    private void updateView() {
        if (movie.getBackdropPath() != null) {
            Glide.with(this).load(ProjectRepository.getInstance().WS_BASE_IMAGE_URL + movie.getBackdropPath()).into(binding.fragmentMovieDetailsIvImage);
        } else {
            Glide.with(this).load(ProjectRepository.getInstance().WS_BASE_IMAGE_URL + movie.getPosterPath()).into(binding.fragmentMovieDetailsIvImage);
        }
        binding.fragmentMovieDetailsTvOverview.setText(movie.getOverview());
        binding.fragmentMovieDetailsTvLength.setText(movie.getRuntime() != null ? String.valueOf(movie.getRuntime()) + " " + getString(R.string.minutes) : getString(R.string.not_available));
        binding.fragmentMovieDetailsTvRating.setText(movie.getVoteAverage() != null ? String.valueOf(movie.getVoteAverage()) : getString(R.string.not_available));
        binding.fragmentMovieDetailsTvLanguage.setText(movie.getOriginalLanguage() != null ? movie.getOriginalLanguage().toUpperCase() : getString(R.string.not_available));
        binding.fragmentMovieDetailsTvReleaseDate.setText(DateUtils.getFormattedDate(DateUtils.DATE_FORMAT_YYYY_MM_DD_OSP, DateUtils.DATE_FORMAT_DD_MMM_YYYY, movie.getReleaseDate()));


        if (movie.getBudget() != null && !movie.getBudget().equals(0)) {
            String budget = getString(R.string.dollar) + String.valueOf(movie.getBudget() / 1000000) + " " + getString(R.string.millions);
            binding.fragmentMovieDetailsTvBudget.setText(budget);
        }
        if (movie.getRevenue() != null && !movie.getRevenue().equals(0)) {
            String revenue = getString(R.string.dollar) + String.valueOf(movie.getRevenue() / 1000000) + " " + getString(R.string.millions);
            binding.fragmentMovieDetailsTvRevenue.setText(revenue);
        }

        if (!movie.getGenres().isEmpty()) {
            String genres = "";
            for (Genre genre : movie.getGenres()) {
                if (genres.isEmpty()) {
                    genres = genre.getName();
                } else {
                    genres = genres + ", " + genre.getName();
                }
            }
            binding.fragmentMovieDetailsTvCategory.setText(genres);
        }

    }
}