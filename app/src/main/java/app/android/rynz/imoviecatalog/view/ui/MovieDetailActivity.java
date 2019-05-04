package app.android.rynz.imoviecatalog.view.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.Locale;

import app.android.rynz.imoviecatalog.BuildConfig;
import app.android.rynz.imoviecatalog.R;
import app.android.rynz.imoviecatalog.data.model.DetailMovieModel;
import app.android.rynz.imoviecatalog.data.model.params.DetailMovieParams;
import app.android.rynz.imoviecatalog.data.repository.TMDBApiReference;
import app.android.rynz.imoviecatalog.utils.DateFormatConverter;
import app.android.rynz.imoviecatalog.utils.ExtraKeys;
import app.android.rynz.imoviecatalog.viewmodel.DetailMovieViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity
{

    private int movieID = 0;
    private DetailMovieParams params = new DetailMovieParams();
    private DetailMovieViewModel detailViewModel;


    @BindView(R.id.srl_detail)
    SwipeRefreshLayout srlDetail;
    @BindView(R.id.tv_detail_load_info)
    TextView tvLoadInfo;
    @BindView(R.id.rl_detail_container)
    RelativeLayout rlDetailContainer;

    @BindView(R.id.Iv_detail_poster)
    ImageView ivPoster;
    @BindView(R.id.tv_detail_title)
    TextView tvTitle;
    @BindView(R.id.tv_detail_release_date)
    TextView tvReleaseDate;
    @BindView(R.id.tv_detail_score)
    TextView tvScore;
    @BindView(R.id.tv_detail_runtime)
    TextView tvRuntime;
    @BindView(R.id.tv_detail_status)
    TextView tvStatus;
    @BindView(R.id.tv_detail_budget)
    TextView tvBudget;
    @BindView(R.id.tv_detail_genres)
    TextView tvGenres;
    @BindView(R.id.tv_detail_companies)
    TextView tvCompanies;
    @BindView(R.id.tv_detail_countries)
    TextView tvCountries;
    @BindView(R.id.tv_detail_languages)
    TextView tvLanguages;
    @BindView(R.id.tv_detail_overview)
    TextView tvOverview;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail_activity);
        ButterKnife.bind(this);

        srlDetail.setRefreshing(true);
        srlDetail.setColorSchemeColors(getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorAccent));
        rlDetailContainer.setVisibility(View.GONE);
        tvLoadInfo.setVisibility(View.VISIBLE);
        tvLoadInfo.setText(R.string.loading_movie_detail);

        if (getIntent().getExtras() != null)
        {
            movieID = getIntent().getIntExtra(ExtraKeys.EXTRA_MOVIE_ID, 0);
            String movieTitle = getIntent().getStringExtra(ExtraKeys.EXTRA_MOVIE_TITLE);
            if(movieTitle!=null)
            {
                setTitle(movieTitle);
            }
        }
        srlDetail.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                rlDetailContainer.setVisibility(View.GONE);
                tvLoadInfo.setVisibility(View.VISIBLE);
                tvLoadInfo.setText(R.string.loading_movie_detail);
                params.requiredParams(BuildConfig.TMDBApiKey, movieID);
                if (detailViewModel != null)
                {
                    detailViewModel.detailMovieLiveData(params);
                }
            }
        });
        params.requiredParams(BuildConfig.TMDBApiKey, movieID);
        setUpDetailMovieDataObserver();
    }

    private void setUpDetailMovieDataObserver()
    {
        detailViewModel = ViewModelProviders.of(this).get(DetailMovieViewModel.class);
        Observer<DetailMovieModel> observer = new Observer<DetailMovieModel>()
        {
            @Override
            public void onChanged(@Nullable DetailMovieModel detailMovieModel)
            {
                displayDetailMovie(detailMovieModel);
            }
        };
        detailViewModel.detailMovieLiveData(params).observe(this, observer);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        if (!outState.isEmpty())
        {
            outState.clear();
        }
        outState.putInt(DetailMovieModel.KEY_MOVIE_ID, movieID);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null)
        {
            movieID = savedInstanceState.getByte(DetailMovieModel.KEY_MOVIE_ID);
        }
    }

    private void displayDetailMovie(DetailMovieModel model)
    {
        if (srlDetail.isRefreshing())
        {
            srlDetail.setRefreshing(false);
        }
        if (model != null)
        {
            rlDetailContainer.setVisibility(View.VISIBLE);
            tvLoadInfo.setVisibility(View.GONE);
            Glide.with(this)
                    .load(TMDBApiReference.TMDB_POSTER_500px + model.getPosterPath())
                    .listener(new RequestListener<Drawable>()
                    {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource)
                        {
                            ivPoster.setImageResource(R.drawable.no_images);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource)
                        {
                            return false;
                        }
                    })
                    .into(ivPoster);
            tvTitle.setText(model.getTitle());
            tvReleaseDate.setText(
                    new DateFormatConverter().withDate(model.getReleaseDate())
                            .withPatternConvert(DateFormatConverter.PATTERN_DATE_SQL, DateFormatConverter.PATTERN_DATE_SPELL_COMMON, Locale.getDefault())
                            .doConvert());
            String score = model.getVoteAverage() + "/10 (" + model.getVoteCount() + " votes)";
            tvScore.setText(score);
            String runtime = getString(R.string.undefined_content);
            if (model.getRuntime() != 0)
            {
                runtime = model.getRuntime() + " minutes";
            }
            tvRuntime.setText(runtime);
            tvStatus.setText(model.getStatus());
            String budget = model.getBuget() + " USD";
            tvBudget.setText(budget);
            String genres = "-";
            if (model.getGenres().size() > 0)
            {
                genres = "";
                for (int i = 0; i < model.getGenres().size(); i++)
                {
                    if (i != 0)
                    {
                        genres = genres.concat(", ");
                    }
                    genres = genres.concat(model.getGenres().get(i).getName());

                }
            }
            tvGenres.setText(genres);

            String companies = "-";
            if (model.getProductionCompanies().size() > 0)
            {
                companies = "";
                for (int i = 0; i < model.getProductionCompanies().size(); i++)
                {
                    if (i != 0)
                    {
                        companies = companies.concat(", ");
                    }
                    companies = companies.concat(model.getProductionCompanies().get(i).getName());

                }
            }
            tvCompanies.setText(companies);

            String countries = "-";
            if (model.getProductionCountries().size() > 0)
            {
                countries = "";
                for (int i = 0; i < model.getProductionCountries().size(); i++)
                {
                    if (i != 0)
                    {
                        countries = companies.concat(", ");
                    }
                    countries = countries.concat(model.getProductionCountries().get(i).getName());

                }
            }
            tvCountries.setText(countries);

            String languages = "-";
            if (model.getSpokenLanguages().size() > 0)
            {
                languages = "";
                for (int i = 0; i < model.getSpokenLanguages().size(); i++)
                {
                    if (i != 0)
                    {
                        languages = companies.concat(", ");
                    }
                    languages = languages.concat(model.getSpokenLanguages().get(i).getName());

                }
            }
            tvLanguages.setText(languages);

            tvOverview.setText(model.getOverview());
        } else
        {
            rlDetailContainer.setVisibility(View.GONE);
            tvLoadInfo.setVisibility(View.VISIBLE);
            tvLoadInfo.setText(R.string.loading_detail_failed);
        }
    }

}
