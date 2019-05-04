package app.android.rynz.imoviecatalog.data.model.moviedetail;

public class SpokenLanguageModel
{
    public static final String KEY_ISO639v1 = "iso_639_1";
    public static final String KEY_NAME = "name";

    private String iso639v1, name;

    public SpokenLanguageModel(String iso639v1, String name)
    {
        this.iso639v1 = iso639v1;
        this.name = name;
    }

    public String getIso639v1()
    {
        return iso639v1;
    }

    public String getName()
    {
        return name;
    }
}
