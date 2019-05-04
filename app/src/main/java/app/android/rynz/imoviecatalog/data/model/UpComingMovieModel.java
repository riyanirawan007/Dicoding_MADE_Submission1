package app.android.rynz.imoviecatalog.data.model;

import java.util.ArrayList;

public class UpComingMovieModel
{
    public static final String KEY_PAGE = "page";
    public static final String KEY_TOTAL_RESULT = "total_results";
    public static final String KEY_TOTAL_PAGES = "total_pages";
    public static final String KEY_UPCOMING_RESULT = "results";
    public static final String KEY_UPCOMING_DATE = "dates";
    public static final String KEY_MOVIE_UPCOMING_MAX_DATE = "maximum";
    public static final String KEY_MOVIE_UPCOMING_MIN_DATE = "minimum";

    private int page;
    private int total_results;
    private int total_pages;
    private String dateMaximum, dateMinimum;
    private ArrayList<ResultMovieModel> movieList;

    public UpComingMovieModel(int page, int total_results, int total_pages, String dateMaximum, String dateMinimum, ArrayList<ResultMovieModel> movieList)
    {
        this.page = page;
        this.total_results = total_results;
        this.total_pages = total_pages;
        this.dateMaximum = dateMaximum;
        this.dateMinimum = dateMinimum;
        this.movieList = movieList;
    }

    public int getPage()
    {
        return page;
    }

    public int getTotal_results()
    {
        return total_results;
    }

    public int getTotal_pages()
    {
        return total_pages;
    }

    public String getDateMaximum()
    {
        return dateMaximum;
    }

    public String getDateMinimum()
    {
        return dateMinimum;
    }

    public ArrayList<ResultMovieModel> getMovieList()
    {
        return movieList;
    }
}
