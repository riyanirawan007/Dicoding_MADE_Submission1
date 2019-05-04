package app.android.rynz.imoviecatalog.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import app.android.rynz.imoviecatalog.data.model.SearchMovieModel;
import app.android.rynz.imoviecatalog.data.model.params.SearchMovieParams;
import app.android.rynz.imoviecatalog.data.repository.TMDBRepository;
import app.android.rynz.imoviecatalog.view.interfaces.TMDBService;

public class SearchMoviesViewModel extends AndroidViewModel implements TMDBService.SearchMovies.SearchMovieLiveData
{
    private TMDBRepository tmdbRepository;
    private MutableLiveData<SearchMovieModel> mutableLiveData = new MutableLiveData<>();

    public SearchMoviesViewModel(@NonNull Application application)
    {
        super(application);
        this.tmdbRepository = new TMDBRepository();
    }

    @Override
    public MutableLiveData<SearchMovieModel> searchMoviesLiveData(@NonNull SearchMovieParams params)
    {
        tmdbRepository.searchMovies(params, true, new TMDBService.SearchMovies.SearchListener()
        {
            @Override
            public void onCompleted(@Nullable SearchMovieModel searchResult)
            {
                mutableLiveData.setValue(searchResult);
            }

            @Override
            public void onFailed(@Nullable String strError, @Nullable String apiRespons)
            {

            }
        });

        return mutableLiveData;
    }
}
