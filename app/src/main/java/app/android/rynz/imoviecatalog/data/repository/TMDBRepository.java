package app.android.rynz.imoviecatalog.data.repository;

import android.support.annotation.NonNull;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import app.android.rynz.imoviecatalog.data.model.DetailMovieModel;
import app.android.rynz.imoviecatalog.data.model.ResultMovieModel;
import app.android.rynz.imoviecatalog.data.model.SearchMovieModel;
import app.android.rynz.imoviecatalog.data.model.UpComingMovieModel;
import app.android.rynz.imoviecatalog.data.model.moviedetail.BelongToCollectionModel;
import app.android.rynz.imoviecatalog.data.model.moviedetail.GenreModel;
import app.android.rynz.imoviecatalog.data.model.moviedetail.ProductionCompanyModel;
import app.android.rynz.imoviecatalog.data.model.moviedetail.ProductionCountryModel;
import app.android.rynz.imoviecatalog.data.model.moviedetail.SpokenLanguageModel;
import app.android.rynz.imoviecatalog.data.model.params.DetailMovieParams;
import app.android.rynz.imoviecatalog.data.model.params.SearchMovieParams;
import app.android.rynz.imoviecatalog.data.model.params.UpComingMovieParams;
import app.android.rynz.imoviecatalog.view.interfaces.TMDBService;
import cz.msebera.android.httpclient.Header;

public class TMDBRepository implements TMDBService.SearchMovies, TMDBService.UpComingMovies, TMDBService.DetailMovie
{
    private AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
    private SyncHttpClient syncHttpClient = new SyncHttpClient();
    private RequestParams requestParams = new RequestParams();
    private SearchMovieModel searchModel;
    private UpComingMovieModel upComingMovieModel;
    private DetailMovieModel detailMovieModel;

    @Override
    public void searchMovies(@NonNull SearchMovieParams params, boolean isAsyncProcess, @NonNull final SearchListener listener)
    {
        requestParams.put(SearchMovieParams.KEY_APIKey, params.getApiKey());
        requestParams.put(SearchMovieParams.KEY_QUERY, params.getQuery());

        if (params.getLanguageID() != null)
        {
            requestParams.put(SearchMovieParams.KEY_LANGUAGE, params.getLanguageID());
        }
        if (params.getPage() != 0)
        {
            requestParams.put(SearchMovieParams.KEY_PAGE, params.getPage());
        }
        if (params.isIncludeAdult())
        {
            requestParams.put(SearchMovieParams.KEY_INCLUDE_ADULT, params.isIncludeAdult());
        }
        if (params.getRegion() != null)
        {
            requestParams.put(SearchMovieParams.KEY_REGION, params.getRegion());
        }
        if (params.getYear() != 0)
        {
            requestParams.put(SearchMovieParams.KEY_YEAR, params.getYear());
        }
        if (params.getPrimaryReleaseYear() != 0)
        {
            requestParams.put(SearchMovieParams.KEY_PRIMARY_RELEASE_YEAR, params.getPrimaryReleaseYear());
        }

        requestParams.setAutoCloseInputStreams(true);
        if (isAsyncProcess)
        {
            asyncHttpClient.get(TMDBApiReference.TMDB_API_URL_SEARCH_MOVIE, requestParams, new AsyncHttpResponseHandler()
            {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody)
                {
                    parsingSearchMovieOnSuccess(statusCode, headers, responseBody, listener);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error)
                {
                    searchModel = null;
                    String apiResponse;
                    if (responseBody != null)
                    {
                        apiResponse = new String(responseBody);
                    } else
                    {
                        apiResponse = null;
                    }
                    listener.onFailed(error.getMessage(), apiResponse);
                }
            });
        } else
        {
            syncHttpClient.get(TMDBApiReference.TMDB_API_URL_SEARCH_MOVIE, requestParams, new AsyncHttpResponseHandler()
            {
                @Override
                public void onStart()
                {
                    setUseSynchronousMode(true);
                    super.onStart();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody)
                {
                    parsingSearchMovieOnSuccess(statusCode, headers, responseBody, listener);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error)
                {
                    searchModel = null;
                    String apiResponse;
                    if (responseBody != null)
                    {
                        apiResponse = new String(responseBody);
                    } else
                    {
                        apiResponse = null;
                    }
                    listener.onFailed(error.getMessage(), apiResponse);
                }
            });
        }
    }

    private void parsingSearchMovieOnSuccess(int statusCode, Header[] headers, byte[] responseBody, @NonNull SearchListener listener)
    {
        String response = null;
        if (responseBody != null)
        {
            response = new String(responseBody);
        }

        int page, total_results, total_pages;
        List<Integer> genreIDs = new ArrayList<>();
        ArrayList<ResultMovieModel> movieList = new ArrayList<>();

        try
        {
            JSONObject jsonObject = new JSONObject(response);
            page = jsonObject.getInt(SearchMovieModel.KEY_PAGE);
            total_results = jsonObject.getInt(SearchMovieModel.KEY_TOTAL_RESULT);
            total_pages = jsonObject.getInt(SearchMovieModel.KEY_TOTAL_PAGES);

            JSONArray jsonArray = jsonObject.getJSONArray(SearchMovieModel.KEY_SEARCH_RESULT);
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject object = jsonArray.getJSONObject(i);

                JSONArray genreids = object.getJSONArray(ResultMovieModel.KEY_MOVIE_GENRE_IDs);
                if (genreids.length() > 0)
                {
                    for (int j = 0; j < genreids.length(); j++)
                    {
                        genreIDs.add(Integer.valueOf(genreids.getString(j)));
                    }
                }

                ResultMovieModel movieModel = new ResultMovieModel(
                        object.getInt(ResultMovieModel.KEY_MOVIE_VOTE_COUNT)
                        , object.getInt(ResultMovieModel.KEY_MOVIE_ID)
                        , object.getDouble(ResultMovieModel.KEY_MOVIE_VOTE_AVERAGE)
                        , object.getDouble(ResultMovieModel.KEY_MOVIE_POPULARITY)
                        , object.getBoolean(ResultMovieModel.KEY_MOVIE_VIDEO)
                        , object.getBoolean(ResultMovieModel.KEY_MOVIE_FOR_ADULT)
                        , object.getString(ResultMovieModel.KEY_MOVIE_TITLE)
                        , object.getString(ResultMovieModel.KEY_MOVIE_POSTER_PATH)
                        , object.getString(ResultMovieModel.KEY_MOVIE_ORI_LANGUAGE)
                        , object.getString(ResultMovieModel.KEY_MOVIE_ORI_TITLE)
                        , object.getString(ResultMovieModel.KEY_MOVIE_BACKDROP_PATH)
                        , object.getString(ResultMovieModel.KEY_MOVIE_OVERVIEW)
                        , object.getString(ResultMovieModel.KEY_MOVIE_REALEASE_DATE)
                        , genreIDs
                );
                movieList.add(movieModel);
            }
            searchModel = new SearchMovieModel(page, total_results, total_pages, movieList);
            listener.onCompleted(searchModel);
        } catch (JSONException e)
        {
            searchModel = null;
            e.printStackTrace();
            listener.onFailed(e.getMessage(), response);
        }
    }

    @Override
    public void upComingMovies(@NonNull UpComingMovieParams params, boolean isAsyncProcess, @NonNull final UpComingListener listener)
    {
        requestParams.put(UpComingMovieParams.KEY_APIKey, params.getApiKey());
        if (params.getLanguageID() != null)
        {
            requestParams.put(UpComingMovieParams.KEY_LANGUAGE, params.getLanguageID());
        }
        if (params.getPage() != 0)
        {
            requestParams.put(UpComingMovieParams.KEY_PAGE, params.getPage());
        }
        if (params.getRegion() != null)
        {
            requestParams.put(UpComingMovieParams.KEY_REGION, params.getRegion());
        }

        requestParams.setAutoCloseInputStreams(true);
        if (isAsyncProcess)
        {
            asyncHttpClient.get(TMDBApiReference.TMDB_API_URL_UPCOMING_MOVIE, requestParams, new AsyncHttpResponseHandler()
            {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody)
                {
                    parsingUpComingOnSuccess(statusCode, headers, responseBody, listener);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error)
                {
                    upComingMovieModel = null;
                    String apiResponse;
                    if (responseBody != null)
                    {
                        apiResponse = new String(responseBody);
                    } else
                    {
                        apiResponse = null;
                    }
                    listener.onFailed(error.getMessage(), apiResponse);
                }
            });

        } else
        {
            syncHttpClient.get(TMDBApiReference.TMDB_API_URL_UPCOMING_MOVIE, requestParams, new AsyncHttpResponseHandler()
            {
                @Override
                public void onStart()
                {
                    setUseSynchronousMode(true);
                    super.onStart();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody)
                {
                    parsingUpComingOnSuccess(statusCode, headers, responseBody, listener);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error)
                {
                    upComingMovieModel = null;
                    String apiResponse;
                    if (responseBody != null)
                    {
                        apiResponse = new String(responseBody);
                    } else
                    {
                        apiResponse = null;
                    }
                    listener.onFailed(error.getMessage(), apiResponse);
                }
            });
        }
    }

    private void parsingUpComingOnSuccess(int statusCode, Header[] headers, byte[] responseBody, @NonNull UpComingListener listener)
    {
        String response = null;
        if (responseBody != null)
        {
            response = new String(responseBody);
        }
        int page, total_results, total_pages;
        List<Integer> genreIDs = new ArrayList<>();
        ArrayList<ResultMovieModel> movieList = new ArrayList<>();
        String dateMaximum, dateMinimum;

        try
        {
            JSONObject jsonObject = new JSONObject(response);
            page = jsonObject.getInt(UpComingMovieModel.KEY_PAGE);
            total_results = jsonObject.getInt(UpComingMovieModel.KEY_TOTAL_RESULT);
            total_pages = jsonObject.getInt(UpComingMovieModel.KEY_TOTAL_PAGES);

            JSONObject dates = jsonObject.getJSONObject(UpComingMovieModel.KEY_UPCOMING_DATE);
            dateMaximum = dates.getString(UpComingMovieModel.KEY_MOVIE_UPCOMING_MAX_DATE);
            dateMinimum = dates.getString(UpComingMovieModel.KEY_MOVIE_UPCOMING_MIN_DATE);

            JSONArray jsonArray = jsonObject.getJSONArray(UpComingMovieModel.KEY_UPCOMING_RESULT);
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject object = jsonArray.getJSONObject(i);

                JSONArray genreids = object.getJSONArray(ResultMovieModel.KEY_MOVIE_GENRE_IDs);
                if (genreids.length() > 0)
                {
                    for (int j = 0; j < genreids.length(); j++)
                    {
                        genreIDs.add(Integer.valueOf(genreids.getString(j)));
                    }
                }

                ResultMovieModel movieModel = new ResultMovieModel(
                        object.getInt(ResultMovieModel.KEY_MOVIE_VOTE_COUNT)
                        , object.getInt(ResultMovieModel.KEY_MOVIE_ID)
                        , object.getDouble(ResultMovieModel.KEY_MOVIE_VOTE_AVERAGE)
                        , object.getDouble(ResultMovieModel.KEY_MOVIE_POPULARITY)
                        , object.getBoolean(ResultMovieModel.KEY_MOVIE_VIDEO)
                        , object.getBoolean(ResultMovieModel.KEY_MOVIE_FOR_ADULT)
                        , object.getString(ResultMovieModel.KEY_MOVIE_TITLE)
                        , object.getString(ResultMovieModel.KEY_MOVIE_POSTER_PATH)
                        , object.getString(ResultMovieModel.KEY_MOVIE_ORI_LANGUAGE)
                        , object.getString(ResultMovieModel.KEY_MOVIE_ORI_TITLE)
                        , object.getString(ResultMovieModel.KEY_MOVIE_BACKDROP_PATH)
                        , object.getString(ResultMovieModel.KEY_MOVIE_OVERVIEW)
                        , object.getString(ResultMovieModel.KEY_MOVIE_REALEASE_DATE)
                        , genreIDs
                );
                movieList.add(movieModel);
            }
            upComingMovieModel = new UpComingMovieModel(page, total_results, total_pages, dateMaximum, dateMinimum, movieList);
            listener.onCompleted(upComingMovieModel);
        } catch (JSONException e)
        {
            upComingMovieModel = null;
            e.printStackTrace();
            listener.onFailed(e.getMessage(), response);
        }
    }

    @Override
    public void getDetailMovie(@NonNull DetailMovieParams params, boolean isAsyncProcess, @NonNull final DetailMovieListener listener)
    {
        requestParams.put(DetailMovieParams.KEY_APIKey, params.getApiKey());
        requestParams.put(DetailMovieParams.KEY_MOVIE_ID, params.getIdMovie());

        if (params.getLanguageID() != null)
        {
            requestParams.put(DetailMovieParams.KEY_LANGUAGE, params.getLanguageID());
        }
        if (params.getAppendToRespond() != null)
        {
            requestParams.put(DetailMovieParams.KEY_APPEND_TO_RESPONSE, params.getAppendToRespond());
        }

        requestParams.setAutoCloseInputStreams(true);
        if (isAsyncProcess)
        {
            asyncHttpClient.get(TMDBApiReference.TMDB_API_URL_MOVIE_DETAIL + String.valueOf(params.getIdMovie()), requestParams, new AsyncHttpResponseHandler()
            {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody)
                {
                    parsingDetailMovieOnSuccess(statusCode, headers, responseBody, listener);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error)
                {
                    detailMovieModel = null;
                    String apiResponse;
                    if (responseBody != null)
                    {
                        apiResponse = new String(responseBody);
                    } else
                    {
                        apiResponse = null;
                    }
                    listener.onFailed(error.getMessage(), apiResponse);

                }
            });
        } else
        {
            asyncHttpClient.get(TMDBApiReference.TMDB_API_URL_MOVIE_DETAIL + String.valueOf(params.getIdMovie()), requestParams, new AsyncHttpResponseHandler()
            {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody)
                {
                    parsingDetailMovieOnSuccess(statusCode, headers, responseBody, listener);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error)
                {
                    detailMovieModel = null;
                    String apiResponse;
                    if (responseBody != null)
                    {
                        apiResponse = new String(responseBody);
                    } else
                    {
                        apiResponse = null;
                    }
                    listener.onFailed(error.getMessage(), apiResponse);
                }
            });
        }
    }

    private void parsingDetailMovieOnSuccess(int statusCode, Header[] headers, byte[] responseBody, @NonNull DetailMovieListener listener)
    {
        String response = null;
        if (responseBody != null)
        {
            response = new String(responseBody);
        }

        try
        {
            BelongToCollectionModel belongsToCollection = null;
            ArrayList<GenreModel> genreList = new ArrayList<>();
            ArrayList<ProductionCompanyModel> productionCompanyList = new ArrayList<>();
            ArrayList<ProductionCountryModel> productionCountryList = new ArrayList<>();
            ArrayList<SpokenLanguageModel> spokenLanguageList = new ArrayList<>();


            JSONObject detail = new JSONObject(response);
            if (!detail.isNull(DetailMovieModel.KEY_MOVIE_BELONG_COLLECTION))
            {
                JSONObject belongCollection = detail.getJSONObject(DetailMovieModel.KEY_MOVIE_BELONG_COLLECTION);
                belongsToCollection = new BelongToCollectionModel(
                        belongCollection.getInt(BelongToCollectionModel.KEY_ID)
                        , belongCollection.getString(BelongToCollectionModel.KEY_NAME)
                        , belongCollection.getString(BelongToCollectionModel.KEY_POSTER_PATH)
                        , belongCollection.getString(BelongToCollectionModel.KEY_BACKDROP_PATH));
            }
            if (!detail.isNull(DetailMovieModel.KEY_MOVIE_GENRES))
            {
                JSONArray genres = detail.getJSONArray(DetailMovieModel.KEY_MOVIE_GENRES);
                for (int i = 0; i < genres.length(); i++)
                {
                    JSONObject object = genres.getJSONObject(i);
                    GenreModel genreModel = new GenreModel(object.getInt(GenreModel.KEY_ID), object.getString(GenreModel.KEY_NAME));
                    genreList.add(genreModel);
                }
            }
            if (!detail.isNull(DetailMovieModel.KEY_MOVIE_PRODUCTION_COMPANIES))
            {
                JSONArray companies = detail.getJSONArray(DetailMovieModel.KEY_MOVIE_PRODUCTION_COMPANIES);
                for (int i = 0; i < companies.length(); i++)
                {
                    JSONObject object = companies.getJSONObject(i);
                    ProductionCompanyModel productionCompanyModel = new ProductionCompanyModel(object.getInt(ProductionCompanyModel.KEY_ID)
                            , object.getString(ProductionCompanyModel.KEY_LOGO_PATH)
                            , object.getString(ProductionCompanyModel.KEY_NAME)
                            , object.getString(ProductionCompanyModel.KEY_ORIGIN_COUNTRY));
                    productionCompanyList.add(productionCompanyModel);
                }
            }
            if (!detail.isNull(DetailMovieModel.KEY_MOVIE_PRODUCTION_COUNTRIES))
            {
                JSONArray countries = detail.getJSONArray(DetailMovieModel.KEY_MOVIE_PRODUCTION_COUNTRIES);
                for (int i = 0; i < countries.length(); i++)
                {
                    JSONObject object = countries.getJSONObject(i);
                    ProductionCountryModel productionCountryModel = new ProductionCountryModel(
                            object.getString(ProductionCountryModel.KEY_ISO3166v1)
                            , object.getString(ProductionCountryModel.KEY_NAME)
                    );
                    productionCountryList.add(productionCountryModel);
                }
            }
            if (!detail.isNull(DetailMovieModel.KEY_MOVIE_SPOKEN_LANGUAGE))
            {
                JSONArray languages = detail.getJSONArray(DetailMovieModel.KEY_MOVIE_SPOKEN_LANGUAGE);
                for (int i = 0; i < languages.length(); i++)
                {
                    JSONObject object = languages.getJSONObject(i);
                    SpokenLanguageModel spokenLanguageModel = new SpokenLanguageModel(
                            object.getString(SpokenLanguageModel.KEY_ISO639v1)
                            , object.getString(SpokenLanguageModel.KEY_NAME)
                    );
                    spokenLanguageList.add(spokenLanguageModel);
                }
            }

            int runtime=0;
            if(!detail.isNull(DetailMovieModel.KEY_MOVIE_RUNTIME))
            {
                runtime=detail.getInt(DetailMovieModel.KEY_MOVIE_RUNTIME);
            }

            detailMovieModel = new DetailMovieModel(
                    detail.getBoolean(DetailMovieModel.KEY_MOVIE_FOR_ADULT)
                    , detail.getString(DetailMovieModel.KEY_MOVIE_BACKDROP_PATH)
                    , belongsToCollection
                    , detail.getInt(DetailMovieModel.KEY_MOVIE_BUDGET)
                    , genreList
                    , detail.getString(DetailMovieModel.KEY_MOVIE_HOMEPAGE)
                    , detail.getString(DetailMovieModel.KEY_MOVIE_ID)
                    , detail.getString(DetailMovieModel.KEY_MOVIE_IMDB_ID)
                    , detail.getString(DetailMovieModel.KEY_MOVIE_ORI_LANGUAGE)
                    , detail.getString(DetailMovieModel.KEY_MOVIE_ORI_TITLE)
                    , detail.getString(DetailMovieModel.KEY_MOVIE_OVERVIEW)
                    , detail.getDouble(DetailMovieModel.KEY_MOVIE_POPULARITY)
                    , detail.getString(DetailMovieModel.KEY_MOVIE_POSTER_PATH)
                    , productionCompanyList
                    , productionCountryList
                    , detail.getString(DetailMovieModel.KEY_MOVIE_REALEASE_DATE)
                    , detail.getInt(DetailMovieModel.KEY_MOVIE_REVENUE)
                    , runtime
                    , spokenLanguageList
                    , detail.getString(DetailMovieModel.KEY_MOVIE_STATUS)
                    , detail.getString(DetailMovieModel.KEY_MOVIE_TAGLINE)
                    , detail.getString(DetailMovieModel.KEY_MOVIE_TITLE)
                    , detail.getBoolean(DetailMovieModel.KEY_MOVIE_VIDEO)
                    , detail.getInt(DetailMovieModel.KEY_MOVIE_VOTE_COUNT)
                    , detail.getDouble(DetailMovieModel.KEY_MOVIE_VOTE_AVERAGE)
            );
            listener.onCompleted(detailMovieModel);
        } catch (JSONException e)
        {
            detailMovieModel = null;
            e.printStackTrace();
            listener.onFailed(e.getMessage(), response);
        }
    }
}
