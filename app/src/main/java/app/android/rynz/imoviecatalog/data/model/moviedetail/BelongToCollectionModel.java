package app.android.rynz.imoviecatalog.data.model.moviedetail;

public class BelongToCollectionModel
{
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_BACKDROP_PATH = "backdrop_path";
    public static final String KEY_POSTER_PATH = "poster_path";

    private int id;
    private String name, posterPath, backdropPath;

    public BelongToCollectionModel(int id, String name, String posterPath, String backdropPath)
    {
        this.id = id;
        this.name = name;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getPosterPath()
    {
        return posterPath;
    }

    public String getBackdropPath()
    {
        return backdropPath;
    }
}
