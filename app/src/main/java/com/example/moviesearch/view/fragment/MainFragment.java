package com.example.moviesearch.view.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.moviesearch.R;
import com.example.moviesearch.model.pojo.movies.movies.MovieModel;
import com.example.moviesearch.model.pojo.movies.movies.Result;
import com.example.moviesearch.view.activity.MainActivity;
import com.example.moviesearch.view.adapter.MoviesAdapter;
import com.example.moviesearch.view.callback.OnMovieItemClick;
import com.example.moviesearch.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.example.moviesearch.view.util.Constants.ID;

public class MainFragment extends Fragment {
    private MoviesAdapter movieAdapter;
    private List<Result> moviesList = new ArrayList<>();
    private MainViewModel mViewModel;
    private RecyclerView rvMovies;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return getLayoutInflater().inflate(R.layout.main_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        movieAdapter = new MoviesAdapter(getActivity(), moviesList, movieItemClick);
        rvMovies = view.findViewById(R.id.fragment_main_rvMovies);
        rvMovies.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvMovies.hasFixedSize();
        rvMovies.setAdapter(movieAdapter);
        initMoviesObserver(mViewModel);
    }

    /**
     * initialise observer for getting movies
     *
     * @param viewModel viewModel
     */
    private void initMoviesObserver(MainViewModel viewModel) {
        viewModel.getDiscoverMoviesObservable().observe(this, new Observer<MovieModel>() {
            @Override
            public void onChanged(@Nullable MovieModel discoverMovies) {
                if (!discoverMovies.getResults().isEmpty()) {
                    moviesList.addAll(discoverMovies.getResults());
                    movieAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    /**
     * handle click event of movie
     */
    private OnMovieItemClick movieItemClick = new OnMovieItemClick() {
        @Override
        public void onClick(int position, int id) {
            final MovieDetailsFragment detailsFragment = new MovieDetailsFragment();
            final Bundle bundle = new Bundle();
            bundle.putInt(ID, id);
            detailsFragment.setArguments(bundle);
            ((MainActivity) getActivity()).addFragment(R.id.activity_main_flContainer, MainFragment.this, detailsFragment, false);
        }
    };
}
