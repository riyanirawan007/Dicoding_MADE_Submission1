package app.android.rynz.imoviecatalog.data.model.moviedetail;

public class ProductionCompanyModel
{

    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_LOGO_PATH = "logo_path";
    public static final String KEY_ORIGIN_COUNTRY = "origin_country";

    private int id;
    private String logoPath, name, originCountry;

    public ProductionCompanyModel(int id, String logoPath, String name, String originCountry)
    {
        this.id = id;
        this.logoPath = logoPath;
        this.name = name;
        this.originCountry = originCountry;
    }

    public int getId()
    {
        return id;
    }

    public String getLogoPath()
    {
        return logoPath;
    }

    public String getName()
    {
        return name;
    }

    public String getOriginCountry()
    {
        return originCountry;
    }
}
