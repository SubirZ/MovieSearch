package com.example.moviesearch.model.repository;

import com.example.moviesearch.model.pojo.movies.movie_details.MoviesDetails;
import com.example.moviesearch.model.pojo.movies.movies.MovieModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by S.C. on 31/03/19.
 */

public interface ServiceCallback {

    @GET("discover/movie")
    Call<MovieModel> discoverMovies(@Query("api_key") String apiKey, @Query("language") String language,
                                    @Query("sort_by") String sortBy,
                                    @Query("page") int page);

    @GET("movie/{id}")
    Call<MoviesDetails> getMovie(@Path("id") int movieId, @Query("api_key") String apiKey);


}