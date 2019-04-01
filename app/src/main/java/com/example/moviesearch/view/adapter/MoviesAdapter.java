package com.example.moviesearch.view.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.moviesearch.R;
import com.example.moviesearch.databinding.RowMainBinding;
import com.example.moviesearch.model.pojo.movies.MovieDetails;
import com.example.moviesearch.model.pojo.movies.movies.Result;
import com.example.moviesearch.model.repository.ProjectRepository;
import com.example.moviesearch.view.callback.OnMovieItemClick;
import com.example.moviesearch.view.util.DateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by S.C. on 31-03-19.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {
    private Context mContext;
    private List<Result> moviesList = new ArrayList<>();
    private OnMovieItemClick listener;
    private LayoutInflater inflater;

    public MoviesAdapter(Context mContext, List<Result> moviesList, OnMovieItemClick listener) {
        this.mContext = mContext;
        this.moviesList = moviesList;
        this.listener = listener;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public MoviesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MoviesViewHolder((RowMainBinding) DataBindingUtil.inflate(inflater, R.layout.row_main, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MoviesViewHolder holder, final int position) {
        holder.binding.rowMainTvTitle.setText(moviesList.get(position).getTitle());
        holder.binding.rowMainTvDesc.setText(moviesList.get(position).getOverview());
        holder.binding.rowMainTvRating.setText(String.valueOf(moviesList.get(position).getVoteAverage()));
        holder.binding.rowMainTvLanguage.setText(moviesList.get(position).getOriginalLanguage().toUpperCase());
        holder.binding.rowMainTvReleaseDate.setText(DateUtils.getFormattedDate(DateUtils.DATE_FORMAT_YYYY_MM_DD_OSP, DateUtils.DATE_FORMAT_DD_MMM_YYYY, moviesList.get(position).getReleaseDate()));


        if (!moviesList.get(position).getBackdropPath().isEmpty()) {
            Glide.with(mContext).load(ProjectRepository.getInstance().WS_BASE_IMAGE_URL + moviesList.get(position).getBackdropPath()).into(holder.binding.rowMainIvImage);
        } else {
            Glide.with(mContext).load(ProjectRepository.getInstance().WS_BASE_IMAGE_URL + moviesList.get(position).getPosterPath()).into(holder.binding.rowMainIvImage);
        }

        holder.binding.rowMainCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onClick(holder.getAdapterPosition(), new MovieDetails(moviesList.get(holder.getAdapterPosition()).getId(), moviesList.get(holder.getAdapterPosition()).getOriginalTitle()));
                ;
            }
        });
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    class MoviesViewHolder extends RecyclerView.ViewHolder {
        private RowMainBinding binding;

        public MoviesViewHolder(RowMainBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}