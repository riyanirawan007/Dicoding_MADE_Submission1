package app.android.rynz.imoviecatalog.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ResultMovieModel implements Parcelable
{
    public static final String KEY_MOVIE_VOTE_COUNT = "vote_count";
    public static final String KEY_MOVIE_ID = "id";
    public static final String KEY_MOVIE_VIDEO = "video";
    public static final String KEY_MOVIE_VOTE_AVERAGE = "vote_average";
    public static final String KEY_MOVIE_TITLE = "title";
    public static final String KEY_MOVIE_POPULARITY = "popularity";
    public static final String KEY_MOVIE_POSTER_PATH = "poster_path";
    public static final String KEY_MOVIE_ORI_LANGUAGE = "original_language";
    public static final String KEY_MOVIE_ORI_TITLE = "original_title";
    public static final String KEY_MOVIE_GENRE_IDs = "genre_ids";
    public static final String KEY_MOVIE_BACKDROP_PATH = "backdrop_path";
    public static final String KEY_MOVIE_FOR_ADULT = "adult";
    public static final String KEY_MOVIE_OVERVIEW = "overview";
    public static final String KEY_MOVIE_REALEASE_DATE = "release_date";

    private int voteCount, idMovie;
    private double voteAverage, popularity;
    private boolean isVideo, isAdult;
    private String title, posterPath, originalLanguage, originalTitle, backDropPath, overview, realeseDate;
    private List<Integer> genreIds = new ArrayList<>();

    public ResultMovieModel(int voteCount, int idMovie, double voteAverage, double popularity, boolean isVideo, boolean isAdult, String title, String posterPath, String originalLanguage, String originalTitle, String backDropPath, String overview, String realeseDate, List<Integer> genreIds)
    {
        this.voteCount = voteCount;
        this.idMovie = idMovie;
        this.voteAverage = voteAverage;
        this.popularity = popularity;
        this.isVideo = isVideo;
        this.isAdult = isAdult;
        this.title = title;
        this.posterPath = posterPath;
        this.originalLanguage = originalLanguage;
        this.originalTitle = originalTitle;
        this.backDropPath = backDropPath;
        this.overview = overview;
        this.realeseDate = realeseDate;
        this.genreIds = genreIds;
    }

    public int getVoteCount()
    {
        return voteCount;
    }

    public int getIdMovie()
    {
        return idMovie;
    }

    public double getVoteAverage()
    {
        return voteAverage;
    }

    public double getPopularity()
    {
        return popularity;
    }

    public boolean isVideo()
    {
        return isVideo;
    }

    public boolean isAdult()
    {
        return isAdult;
    }

    public String getTitle()
    {
        return title;
    }

    public String getPosterPath()
    {
        return posterPath;
    }

    public String getOriginalLanguage()
    {
        return originalLanguage;
    }

    public String getOriginalTitle()
    {
        return originalTitle;
    }

    public String getBackDropPath()
    {
        return backDropPath;
    }

    public String getOverview()
    {
        return overview;
    }

    public String getRealeseDate()
    {
        return realeseDate;
    }

    public List<Integer> getGenreIds()
    {
        return genreIds;
    }


    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(this.voteCount);
        dest.writeInt(this.idMovie);
        dest.writeDouble(this.voteAverage);
        dest.writeDouble(this.popularity);
        dest.writeByte(this.isVideo ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isAdult ? (byte) 1 : (byte) 0);
        dest.writeString(this.title);
        dest.writeString(this.posterPath);
        dest.writeString(this.originalLanguage);
        dest.writeString(this.originalTitle);
        dest.writeString(this.backDropPath);
        dest.writeString(this.overview);
        dest.writeString(this.realeseDate);
        dest.writeList(this.genreIds);
    }

    protected ResultMovieModel(Parcel in)
    {
        this.voteCount = in.readInt();
        this.idMovie = in.readInt();
        this.voteAverage = in.readDouble();
        this.popularity = in.readDouble();
        this.isVideo = in.readByte() != 0;
        this.isAdult = in.readByte() != 0;
        this.title = in.readString();
        this.posterPath = in.readString();
        this.originalLanguage = in.readString();
        this.originalTitle = in.readString();
        this.backDropPath = in.readString();
        this.overview = in.readString();
        this.realeseDate = in.readString();
        this.genreIds = new ArrayList<Integer>();
        in.readList(this.genreIds, Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<ResultMovieModel> CREATOR = new Parcelable.Creator<ResultMovieModel>()
    {
        @Override
        public ResultMovieModel createFromParcel(Parcel source)
        {
            return new ResultMovieModel(source);
        }

        @Override
        public ResultMovieModel[] newArray(int size)
        {
            return new ResultMovieModel[size];
        }
    };

    //Comparators for sorting data by
    public static class Comparators
    {
        public static Comparator<ResultMovieModel> RELEASE_DATE = new Comparator<ResultMovieModel>()
        {
            @Override
            public int compare(ResultMovieModel resultMovieModel, ResultMovieModel t1)
            {
                return resultMovieModel.getRealeseDate().compareTo(t1.getRealeseDate());
            }
        };
        public static Comparator<ResultMovieModel> MOVIE_NAME = new Comparator<ResultMovieModel>()
        {
            @Override
            public int compare(ResultMovieModel resultMovieModel, ResultMovieModel t1)
            {
                return resultMovieModel.getTitle().compareTo(t1.getTitle());
            }
        };
        public static Comparator<ResultMovieModel> MOVIE_POPULARITY = new Comparator<ResultMovieModel>()
        {
            @Override
            public int compare(ResultMovieModel resultMovieModel, ResultMovieModel t1)
            {
                return Double.compare(resultMovieModel.getPopularity(), t1.getPopularity());
            }
        };
    }
}
