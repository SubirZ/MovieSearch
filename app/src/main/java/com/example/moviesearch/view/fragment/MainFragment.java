package com.example.moviesearch.view.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.moviesearch.R;
import com.example.moviesearch.databinding.FragmentMainBinding;
import com.example.moviesearch.model.pojo.movies.MovieDetails;
import com.example.moviesearch.model.pojo.movies.movies.MovieModel;
import com.example.moviesearch.model.pojo.movies.movies.Result;
import com.example.moviesearch.view.activity.MainActivity;
import com.example.moviesearch.view.adapter.MoviesAdapter;
import com.example.moviesearch.view.callback.OnMovieItemClick;
import com.example.moviesearch.view.util.NetworkUtils;
import com.example.moviesearch.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.example.moviesearch.view.util.Constants.ID;
import static com.example.moviesearch.view.util.Constants.NAME;

public class MainFragment extends Fragment {
    private FragmentMainBinding binding;
    private MoviesAdapter movieAdapter;
    private List<Result> moviesList = new ArrayList<>();
    private MainViewModel mViewModel;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    void initView() {
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.fragmentMainToolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.popular_movies));
        setHasOptionsMenu(true);

        Glide.with(this).load(R.drawable.shimmer).into(binding.fragmentMainIvLoader);

        movieAdapter = new MoviesAdapter(getActivity(), moviesList, movieItemClick);
        binding.fragmentMainRvMovies.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.fragmentMainRvMovies.hasFixedSize();
        binding.fragmentMainRvMovies.setAdapter(movieAdapter);
        initMoviesObserver(mViewModel);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                break;
            case R.id.refresh:
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * initialise observer for getting movies
     *
     * @param viewModel viewModel
     */
    private void initMoviesObserver(MainViewModel viewModel) {
        if (NetworkUtils.isOn(getActivity())) {
            viewModel.getDiscoverMoviesObservable().observe(this, new Observer<MovieModel>() {
                @Override
                public void onChanged(@Nullable MovieModel discoverMovies) {
                    if (!discoverMovies.getResults().isEmpty()) {
                        moviesList.clear();
                        moviesList.addAll(discoverMovies.getResults());
                        movieAdapter.notifyDataSetChanged();
                        binding.fragmentMainIvLoader.setVisibility(View.GONE);
                    }
                }
            });
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * handle click event of movie
     */
    private OnMovieItemClick movieItemClick = new OnMovieItemClick() {
        @Override
        public void onClick(int position, MovieDetails movieDetails) {
            final MovieDetailsFragment detailsFragment = new MovieDetailsFragment();
            final Bundle bundle = new Bundle();
            bundle.putInt(ID, movieDetails.getMovieId());
            bundle.putString(NAME, movieDetails.getMovieName());
            detailsFragment.setArguments(bundle);
            ((MainActivity) getActivity()).addFragment(R.id.activity_main_flContainer, MainFragment.this, detailsFragment, false);
        }
    };
}
