package app.android.rynz.imoviecatalog.utils;

import android.support.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormatConverter
{
    private SimpleDateFormat simpleDateFormat;
    private String finalPattern, strDateToConvert;
    private Date dateToConvert;
    private boolean isValidPattern;
    private Locale locale;
    public static final String PATTERN_DATE_SQL = "yyyy-MM-dd";
    public static final String PATTERN_DATE_SPELL_COMMON = "EEEE, dd MMMM yyyy";

    public DateFormatConverter withDate(@NonNull String strDateToConvert)
    {
        this.strDateToConvert = strDateToConvert;
        return this;
    }

    public DateFormatConverter withPatternConvert(@NonNull String initPattern, @NonNull String finalPattern, @NonNull Locale locale)
    {
        this.finalPattern = finalPattern;
        this.locale = locale;

        simpleDateFormat = new SimpleDateFormat(initPattern, locale);
        try
        {
            dateToConvert = simpleDateFormat.parse(strDateToConvert);
            isValidPattern = true;
        } catch (ParseException e)
        {
            isValidPattern = false;
        }
        return this;
    }

    public String doConvert()
    {
        if (isValidPattern)
        {
            simpleDateFormat = new SimpleDateFormat(finalPattern, locale);
            return simpleDateFormat.format(dateToConvert);
        } else
        {
            return strDateToConvert;
        }
    }
}
