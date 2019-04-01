package com.example.moviesearch.model.pojo.movies;

public class MovieDetails {
    private Integer movieId;
    private String movieName;

    public MovieDetails(Integer movieId, String movieName) {
        this.movieId = movieId;
        this.movieName = movieName;
    }

    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }
}
