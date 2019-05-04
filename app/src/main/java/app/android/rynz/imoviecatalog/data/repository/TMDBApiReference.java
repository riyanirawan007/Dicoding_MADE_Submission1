package app.android.rynz.imoviecatalog.data.repository;

public class TMDBApiReference
{
    private static final String TMDB_API_HOST = "https://api.themoviedb.org";
    private static final String TMDB_POSTER_HOST = "http://image.tmdb.org";

    public static final String TMDB_API_URL_SEARCH_MOVIE = TMDB_API_HOST + "/3/search/movie";
    public static final String TMDB_API_URL_UPCOMING_MOVIE = TMDB_API_HOST + "/3/movie/upcoming";
    public static final String TMDB_API_URL_MOVIE_DETAIL = TMDB_API_HOST + "/3/movie/"; //required when implement: movieId
    public static final String TMDB_POSTER_92px = TMDB_POSTER_HOST + "/t/p/w92";
    public static final String TMDB_POSTER_154px = TMDB_POSTER_HOST + "/t/p/w154";
    public static final String TMDB_POSTER_342px = TMDB_POSTER_HOST + "/t/p/w342";
    public static final String TMDB_POSTER_500px = TMDB_POSTER_HOST + "/t/p/w500";
    public static final String TMDB_POSTER_780px = TMDB_POSTER_HOST + "/t/p/w780";
    public static final String ID_LANGUAGE_US = "en-US";
}
