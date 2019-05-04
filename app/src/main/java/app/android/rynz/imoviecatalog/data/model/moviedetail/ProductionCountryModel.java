package app.android.rynz.imoviecatalog.data.model.moviedetail;

public class ProductionCountryModel
{

    public static final String KEY_ISO3166v1 = "iso_3166_1";
    public static final String KEY_NAME = "name";

    private String iso3166v1, name;

    public ProductionCountryModel(String iso3166v1, String name)
    {
        this.iso3166v1 = iso3166v1;
        this.name = name;
    }

    public String getIso3166v1()
    {
        return iso3166v1;
    }

    public String getName()
    {
        return name;
    }
}
