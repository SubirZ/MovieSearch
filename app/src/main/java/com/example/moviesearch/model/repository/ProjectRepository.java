package com.example.moviesearch.model.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.example.moviesearch.BuildConfig;
import com.example.moviesearch.model.pojo.movies.movie_details.MoviesDetails;
import com.example.moviesearch.model.pojo.movies.movies.MovieModel;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by S.C. on 31/03/19.
 */

public class ProjectRepository {
    private static ProjectRepository projectRepository;
    public String WS_BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w500";
    String BASE_URL = "https://api.themoviedb.org/3/";
    String API_KEY = "2e5fb5457cf57cb838d61bbf7b6c7e7d";
    Interceptor interceptor = new Interceptor() {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request.Builder builder = chain.request().newBuilder();
            Request request = builder.build();
            return chain.proceed(request);
        }
    };
    private ServiceCallback serviceCallback;

    private ProjectRepository() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS);

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
            logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logInterceptor);
        }

        if (interceptor != null) {
            builder.addInterceptor(interceptor);
        }
        OkHttpClient client = builder.build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        serviceCallback = retrofit.create(ServiceCallback.class);
    }

    public synchronized static ProjectRepository getInstance() {
        if (projectRepository == null) {
            projectRepository = new ProjectRepository();
        }
        return projectRepository;
    }

    /**
     * @return returns list of movies
     */
    public LiveData<MovieModel> discoverMovieList(String sortBy, String lang) {
        final MutableLiveData<MovieModel> data = new MutableLiveData<>();
        serviceCallback.discoverMovies(API_KEY, lang, sortBy,  1).enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<MovieModel> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    /**
     * @return returns movie details
     */
    public LiveData<MoviesDetails> movieDetails(int movieId) {
        final MutableLiveData<MoviesDetails> data = new MutableLiveData<>();
        serviceCallback.getMovie(movieId, API_KEY).enqueue(new Callback<MoviesDetails>() {
            @Override
            public void onResponse(Call<MoviesDetails> call, Response<MoviesDetails> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<MoviesDetails> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }
}
