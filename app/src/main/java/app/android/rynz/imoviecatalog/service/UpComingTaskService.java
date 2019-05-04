package app.android.rynz.imoviecatalog.service;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.TaskStackBuilder;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import app.android.rynz.imoviecatalog.BuildConfig;
import app.android.rynz.imoviecatalog.R;
import app.android.rynz.imoviecatalog.data.model.ResultMovieModel;
import app.android.rynz.imoviecatalog.data.model.UpComingMovieModel;
import app.android.rynz.imoviecatalog.data.model.params.UpComingMovieParams;
import app.android.rynz.imoviecatalog.data.repository.TMDBRepository;
import app.android.rynz.imoviecatalog.task.UpComingTask;
import app.android.rynz.imoviecatalog.utils.DateFormatConverter;
import app.android.rynz.imoviecatalog.utils.EasyNotif;
import app.android.rynz.imoviecatalog.utils.ExtraKeys;
import app.android.rynz.imoviecatalog.view.interfaces.TMDBService;
import app.android.rynz.imoviecatalog.view.ui.MovieDetailActivity;

public class UpComingTaskService extends GcmTaskService
{
    private TMDBRepository tmdbRepository = new TMDBRepository();
    public static final String TAG_UPCOMING_MOVIE_LOG = "TASK_UPCOMING_MOVIES";
    public static final int DEFAULT_PERIOD = 60, DEFAULT_FLEX = 10;

    @Override
    public int onRunTask(TaskParams taskParams)
    {
        int result = 0;
        if (taskParams.getTag().equals(TAG_UPCOMING_MOVIE_LOG))
        {
            UpComingMovieParams params = new UpComingMovieParams();
            params.requiredParams(BuildConfig.TMDBApiKey);

            tmdbRepository.upComingMovies(params, false, new TMDBService.UpComingMovies.UpComingListener()
            {
                @Override
                public void onCompleted(@Nullable UpComingMovieModel upComingMovies)
                {
                    if (upComingMovies != null)
                    {
                        if (upComingMovies.getMovieList().size() > 0)
                        {

                            //Compare to get real movie upcoming release date from current date
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            Date currentDate = Calendar.getInstance().getTime();
                            Date compareDate = null;

                            ArrayList<ResultMovieModel> movieList = new ArrayList<>();
                            for (int i = 0; i < upComingMovies.getMovieList().size(); i++)
                            {
                                try
                                {
                                    compareDate = simpleDateFormat.parse(upComingMovies.getMovieList().get(i).getRealeseDate());
                                } catch (ParseException e)
                                {
                                    e.printStackTrace();
                                }

                                if (currentDate != null && compareDate != null)
                                {
                                    if (currentDate.compareTo(compareDate) <= 0)
                                    {
                                        movieList.add(upComingMovies.getMovieList().get(i));
                                    }
                                }
                                compareDate = null;
                            }

                            Collections.sort(movieList, Collections.reverseOrder(ResultMovieModel.Comparators.RELEASE_DATE));
                            for (int i = 0; i < movieList.size(); i++)
                            {
                                String releaseDate = new DateFormatConverter()
                                        .withDate(movieList.get(i).getRealeseDate())
                                        .withPatternConvert(DateFormatConverter.PATTERN_DATE_SQL, DateFormatConverter.PATTERN_DATE_SPELL_COMMON, Locale.getDefault())
                                        .doConvert();

                                String title = getResources().getString(R.string.notification_upcoming_movie_title) + "! " + movieList.get(i).getTitle();
                                String desc = getResources().getString(R.string.word_movie_date_upcoming_on)
                                        + " " + releaseDate;
                                //showNotification(getApplicationContext(),title,desc,i);

                                Intent detailIntent = new Intent(getApplicationContext(), MovieDetailActivity.class);
                                detailIntent.putExtra(ExtraKeys.EXTRA_MOVIE_ID, movieList.get(i).getIdMovie());
                                detailIntent.putExtra(ExtraKeys.EXTRA_MOVIE_TITLE, movieList.get(i).getTitle());

                                PendingIntent pendingIntent = TaskStackBuilder.create(getApplicationContext())
                                        .addParentStack(MovieDetailActivity.class)
                                        .addNextIntent(detailIntent)
                                        .getPendingIntent(i, PendingIntent.FLAG_UPDATE_CURRENT);

                                new EasyNotif().with(getApplicationContext(), title, desc, i)
                                        .setContentIntent(pendingIntent)
                                        .show();
                            }
                            movieList.clear();
                        }
                    }
                }

                @Override
                public void onFailed(@Nullable String strError, @Nullable String apiRespons)
                {
                    //Do nothing
                }
            });

            result = GcmNetworkManager.RESULT_SUCCESS;
        }
        return result;
    }

    @Override
    public void onInitializeTasks()
    {
        super.onInitializeTasks();
        UpComingTask upComingTask = new UpComingTask(this);
        upComingTask.createPeriodicTask();
    }

}
