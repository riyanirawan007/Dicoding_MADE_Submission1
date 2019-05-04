package app.android.rynz.imoviecatalog.view.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import app.android.rynz.imoviecatalog.BuildConfig;
import app.android.rynz.imoviecatalog.R;
import app.android.rynz.imoviecatalog.data.model.SearchMovieModel;
import app.android.rynz.imoviecatalog.data.model.params.SearchMovieParams;
import app.android.rynz.imoviecatalog.task.UpComingTask;
import app.android.rynz.imoviecatalog.utils.ExtraKeys;
import app.android.rynz.imoviecatalog.utils.SettingsPreferenceHelper;
import app.android.rynz.imoviecatalog.view.adapter.SearchMoviesAdapter;
import app.android.rynz.imoviecatalog.viewmodel.SearchMoviesViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity
{
    private SearchMoviesViewModel moviesViewModel;
    private UpComingTask upComingTask;
    private SettingsPreferenceHelper settingsPreferenceHelper;

    private AlertDialog alertDialog;
    private AlertDialog.Builder builder;
    private int currentPage = 1;
    private SearchMovieParams params = new SearchMovieParams();

    @BindView(R.id.et_search_keywords)
    EditText keyword;
    @BindView(R.id.btn_search)
    Button search;
    @BindView(R.id.lv_result_search)
    ListView listView;
    @BindView(R.id.pbar_search_movie)
    ProgressBar pbarSearch;
    @BindView(R.id.tv_search_tell_stat)
    TextView searchTellStat;

    @OnClick(R.id.btn_search)
    public void doSearch()
    {
        if (keyword.getText().toString().length() > 0)
        {
            searchTellStat.setVisibility(View.GONE);
            pbarSearch.setVisibility(View.VISIBLE);
            params.setQuery(keyword.getText().toString());
            moviesViewModel.searchMoviesLiveData(params);
        } else
        {
            Toast.makeText(this, R.string.search_tell_cant_empty_keywords, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        params.requiredParams(BuildConfig.TMDBApiKey, "");
        setUpSearchDataObserver();
        settingsPreferenceHelper = new SettingsPreferenceHelper(this);
        upComingTask = new UpComingTask(this);
        builder = new AlertDialog.Builder(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_daily_reminder)
        {
            Intent remider = new Intent(MainActivity.this, DailyReminderActivity.class);
            startActivity(remider);

            return true;
        } else if (id == R.id.action_upcoming_notifier)
        {
            upComingSettingDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        if (!outState.isEmpty())
        {
            outState.clear();
        }
        outState.putString(ExtraKeys.EXTRA_SEARCH_KEYWORDS, keyword.getText().toString());
        outState.putInt(ExtraKeys.EXTRA_SEARCH_PAGE, currentPage);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null)
        {
            keyword.setText(savedInstanceState.getString(ExtraKeys.EXTRA_SEARCH_KEYWORDS));
            currentPage = savedInstanceState.getInt(ExtraKeys.EXTRA_SEARCH_PAGE);
        }

        if (moviesViewModel != null)
        {
            pbarSearch.setVisibility(View.VISIBLE);
            searchTellStat.setVisibility(View.GONE);
            params.requiredParams(BuildConfig.TMDBApiKey, keyword.getText().toString());
            moviesViewModel.searchMoviesLiveData(params);
        }
    }

    private void setUpSearchDataObserver()
    {
        moviesViewModel = ViewModelProviders.of(this).get(SearchMoviesViewModel.class);
        Observer<SearchMovieModel> searchMoviesModelObserver = new Observer<SearchMovieModel>()
        {
            @Override
            public void onChanged(@Nullable SearchMovieModel searchMoviesModel)
            {
                loadToListView(searchMoviesModel);
            }
        };
        moviesViewModel.searchMoviesLiveData(params).observe(this, searchMoviesModelObserver);
    }

    private void loadToListView(SearchMovieModel model)
    {
        pbarSearch.setVisibility(View.GONE);
        searchTellStat.setVisibility(View.GONE);
        listView.setVisibility(View.GONE);

        if (model != null)
        {
            if (model.getMovieList().size() > 0)
            {
                if (listView.getAdapter() != null)
                {
                    listView.setAdapter(null);
                }
                SearchMoviesAdapter adapter = new SearchMoviesAdapter(model.getMovieList(), this, R.layout.lv_search_item);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                listView.setVisibility(View.VISIBLE);
            } else
            {
                searchTellStat.setVisibility(View.VISIBLE);
                searchTellStat.setText(R.string.search_result_tell_not_found);
            }
        } else
        {
            searchTellStat.setVisibility(View.VISIBLE);
            searchTellStat.setText(R.string.search_result_tell_not_found);
        }

    }

    private void activateUpComingTask()
    {
        upComingTask.createPeriodicTask();
    }

    private void deactivateUpComingTask()
    {
        upComingTask.cancelPeriodicTask();
    }

    private void upComingSettingDialog()
    {
        builder.setTitle(R.string.dialog_title_upcoming_stat);
        if (!settingsPreferenceHelper.isUpComingNotifierEnabled())
        {
            builder.setMessage(R.string.dialog_msg_confirm_enable_upcoming);
            builder.setPositiveButton(R.string.dialog_confirm_yes, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    activateUpComingTask();
                    settingsPreferenceHelper.setUpComingNotifierStat(true);
                    if (alertDialog.isShowing())
                    {
                        alertDialog.dismiss();
                    }
                    Snackbar.make(getWindow().getDecorView().getRootView(), R.string.info_upcoming_enabled, 3500).show();
                }
            });
            builder.setNegativeButton(R.string.dialog_confirm_no, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    if (alertDialog.isShowing())
                    {
                        alertDialog.dismiss();
                    }
                }
            });
        } else
        {
            builder.setMessage(R.string.dialog_msg_confirm_disable_upcoming);
            builder.setPositiveButton(R.string.dialog_confirm_yes, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    deactivateUpComingTask();
                    settingsPreferenceHelper.setUpComingNotifierStat(false);
                    if (alertDialog.isShowing())
                    {
                        alertDialog.dismiss();
                    }
                    Snackbar.make(getWindow().getDecorView().getRootView(), R.string.info_upcoming_disabled, 3500).show();
                }
            });
            builder.setNegativeButton(R.string.dialog_confirm_no, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    if (alertDialog.isShowing())
                    {
                        alertDialog.dismiss();
                    }
                }
            });
        }

        alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }
}
