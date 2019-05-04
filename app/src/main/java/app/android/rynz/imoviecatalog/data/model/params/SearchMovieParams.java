package app.android.rynz.imoviecatalog.data.model.params;

import android.support.annotation.NonNull;

public class SearchMovieParams
{
    public static final String KEY_APIKey = "api_key";
    public static final String KEY_LANGUAGE = "language";
    public static final String KEY_QUERY = "query";
    public static final String KEY_PAGE = "page";
    public static final String KEY_INCLUDE_ADULT = "include_adult";
    public static final String KEY_REGION = "region";
    public static final String KEY_YEAR = "year";
    public static final String KEY_PRIMARY_RELEASE_YEAR = "primary_release_year";

    private String apiKey, languageID, query, region;
    private int page = 0, year = 0, primaryReleaseYear = 0;
    private boolean includeAdult;

    public void requiredParams(@NonNull String apiKey, @NonNull String query)
    {
        this.apiKey = apiKey;
        this.query = query;
    }

    public void setApiKey(String apiKey)
    {
        this.apiKey = apiKey;
    }

    public void setQuery(String query)
    {
        this.query = query;
    }

    public void clearAll()
    {
        apiKey = null;
        languageID = null;
        query = null;
        region = null;
        page = 0;
        year = 0;
        primaryReleaseYear = 0;
        includeAdult = true;
    }

    public void clearExceptRequiredParams()
    {
        languageID = null;
        region = null;
        page = 0;
        year = 0;
        primaryReleaseYear = 0;
        includeAdult = true;
    }

    public String getApiKey()
    {
        return apiKey;
    }

    public String getQuery()
    {
        return query;
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

    public int getYear()
    {
        return year;
    }

    public void setYear(int year)
    {
        this.year = year;
    }

    public int getPrimaryReleaseYear()
    {
        return primaryReleaseYear;
    }

    public void setPrimaryReleaseYear(int primaryReleaseYear)
    {
        this.primaryReleaseYear = primaryReleaseYear;
    }

    public boolean isIncludeAdult()
    {
        return includeAdult;
    }

    public void setIncludeAdult(boolean includeAdult)
    {
        this.includeAdult = includeAdult;
    }
}
