package app.android.rynz.imoviecatalog.data.model.params;

import android.support.annotation.NonNull;

public class DetailMovieParams
{
    public static final String KEY_APIKey = "api_key";
    public static final String KEY_MOVIE_ID = "movie_id";
    public static final String KEY_LANGUAGE = "language";
    public static final String KEY_APPEND_TO_RESPONSE = "append_to_response";

    private String apiKey, languageID, appendToRespond;
    private int idMovie;

    public void requiredParams(@NonNull String apiKey, int idMovie)
    {
        this.apiKey = apiKey;
        this.idMovie = idMovie;
    }

    public void clearAll()
    {
        apiKey = null;
        languageID = null;
        appendToRespond = null;
        idMovie = 0;
    }

    public void clearExceptRequiredParams()
    {
        languageID = null;
        appendToRespond = null;
    }

    public void setApiKey(String apiKey)
    {
        this.apiKey = apiKey;
    }

    public void setIdMovie(int idMovie)
    {
        this.idMovie = idMovie;
    }

    public String getApiKey()
    {
        return apiKey;
    }

    public int getIdMovie()
    {
        return idMovie;
    }

    public String getLanguageID()
    {
        return languageID;
    }

    public void setLanguageID(String languageID)
    {
        this.languageID = languageID;
    }

    public String getAppendToRespond()
    {
        return appendToRespond;
    }

    public void setAppendToRespond(String appendToRespond)
    {
        this.appendToRespond = appendToRespond;
    }
}
