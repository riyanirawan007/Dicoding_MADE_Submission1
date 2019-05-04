package app.android.rynz.imoviecatalog.data.model.params;

import android.support.annotation.NonNull;

public class UpComingMovieParams
{
    public static final String KEY_APIKey = "api_key";
    public static final String KEY_LANGUAGE = "language";
    public static final String KEY_PAGE = "page";
    public static final String KEY_REGION = "region";

    private String apiKey, languageID, region;
    private int page;

    public void requiredParams(@NonNull String apiKey)
    {
        this.apiKey = apiKey;
    }

    public void clearAll()
    {
        apiKey = null;
        languageID = null;
        region = null;
        page = 0;
    }

    public void clearExceptRequiredParams()
    {
        languageID = null;
        region = null;
        page = 0;
    }

    public void setApiKey(String apiKey)
    {
        this.apiKey = apiKey;
    }

    public String getApiKey()
    {
        return apiKey;
    }

    public String getLanguageID()
    {
        return languageID;
    }

    public void setLanguageID(String languageID)
    {
        this.languageID = languageID;
    }

    public String getRegion()
    {
        return region;
    }

    public void setRegion(String region)
    {
        this.region = region;
    }

    public int getPage()
    {
        return page;
    }

    public void setPage(int page)
    {
        this.page = page;
    }
}
