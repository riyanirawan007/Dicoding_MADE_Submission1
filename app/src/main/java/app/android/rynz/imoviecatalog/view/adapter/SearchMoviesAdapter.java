package app.android.rynz.imoviecatalog.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.Locale;

import app.android.rynz.imoviecatalog.R;
import app.android.rynz.imoviecatalog.data.model.ResultMovieModel;
import app.android.rynz.imoviecatalog.data.repository.TMDBApiReference;
import app.android.rynz.imoviecatalog.utils.DateFormatConverter;
import app.android.rynz.imoviecatalog.utils.ExtraKeys;
import app.android.rynz.imoviecatalog.view.ui.MovieDetailActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchMoviesAdapter extends ArrayAdapter<ResultMovieModel>
{
    private ArrayList<ResultMovieModel> movieList;
    private Context context;
    private int viewResourceId;

    public SearchMoviesAdapter(@NonNull ArrayList<ResultMovieModel> movieList, @NonNull Context context, int viewResourceId)
    {
        super(context, viewResourceId, movieList);
        this.movieList = movieList;
        this.context = context;
        this.viewResourceId = viewResourceId;
    }

    @BindView(R.id.lv_item_poster)
    ImageView poster;
    @BindView(R.id.lv_item_master)
    RelativeLayout master;
    @BindView(R.id.lv_item_title)
    TextView title;
    @BindView(R.id.lv_item_desc)
    TextView desc;
    @BindView(R.id.lv_item_release_date)
    TextView releaseDate;


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        View v = convertView;
        if (v == null)
        {
            v = LayoutInflater.from(context).inflate(context.getResources().getLayout(viewResourceId), parent, false);
        }
        ButterKnife.bind(this, v);
        final ResultMovieModel movie = movieList.get(position);
        Glide.with(context)
                .load(TMDBApiReference.TMDB_POSTER_342px + movie.getPosterPath())
                .listener(new RequestListener<Drawable>()
                {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource)
                    {
                        poster.setImageResource(R.drawable.no_images);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource)
                    {
                        return false;
                    }
                })
                .into(poster);
        String titleSnipset = movie.getTitle();
        if (titleSnipset.length() > 40)
        {
            titleSnipset = titleSnipset.substring(0, 40) + "...";
        }
        title.setText(titleSnipset);

        String overviewSnipset = movie.getOverview();
        if (overviewSnipset.length() > 150)
        {
            overviewSnipset = overviewSnipset.substring(0, 150) + "...";
        }
        desc.setText(overviewSnipset);
        releaseDate.setText(
                new DateFormatConverter()
                        .withDate(movie.getRealeseDate())
                        .withPatternConvert(DateFormatConverter.PATTERN_DATE_SQL, DateFormatConverter.PATTERN_DATE_SPELL_COMMON, Locale.getDefault())
                        .doConvert()
        );

        master.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent detail = new Intent(context, MovieDetailActivity.class);
                detail.putExtra(ExtraKeys.EXTRA_MOVIE_ID, movie.getIdMovie());
                detail.putExtra(ExtraKeys.EXTRA_MOVIE_TITLE, movie.getTitle());
                context.startActivity(detail);
            }
        });
        return v;
    }
}
