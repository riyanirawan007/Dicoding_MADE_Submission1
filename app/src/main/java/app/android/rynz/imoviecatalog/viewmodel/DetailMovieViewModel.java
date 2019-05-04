package app.android.rynz.imoviecatalog.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import app.android.rynz.imoviecatalog.data.model.DetailMovieModel;
import app.android.rynz.imoviecatalog.data.model.params.DetailMovieParams;
import app.android.rynz.imoviecatalog.data.repository.TMDBRepository;
import app.android.rynz.imoviecatalog.view.interfaces.TMDBService;

public class DetailMovieViewModel extends AndroidViewModel implements TMDBService.DetailMovie.DetailMovieLiveData
{
    private TMDBRepository tmdbRepository;
    private MutableLiveData<DetailMovieModel> mutableLiveData = new MutableLiveData<>();

    public DetailMovieViewModel(@NonNull Application application)
    {
        super(application);
        tmdbRepository = new TMDBRepository();
    }

    @Override
    public MutableLiveData<DetailMovieModel> detailMovieLiveData(@NonNull DetailMovieParams params)
    {
        tmdbRepository.getDetailMovie(params, true, new TMDBService.DetailMovie.DetailMovieListener()
        {
            @Override
            public void onCompleted(@Nullable DetailMovieModel movieDetail)
            {
                mutableLiveData.setValue(movieDetail);
            }

            @Override
            public void onFailed(@Nullable String strError, @Nullable String apiRespons)
            {

            }
        });
        return mutableLiveData;
    }

}
