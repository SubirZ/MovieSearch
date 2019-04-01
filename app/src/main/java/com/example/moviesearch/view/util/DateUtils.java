package com.example.moviesearch.view.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    public static final String DATE_FORMAT_YYYY_MM_DD_OSP = "yyyy-MM-dd";
    public static final String DATE_FORMAT_DD_MMM_YYYY = "dd MMM yyyy";

    /**
     * @param strSourceFormat      input format
     * @param strDestinationFormat output format
     * @param sourceDate           input string
     * @return formatted output string
     */
    public static String getFormattedDate(final String strSourceFormat, final String strDestinationFormat, final String sourceDate) {
        final SimpleDateFormat sourceFormat = new SimpleDateFormat(strSourceFormat, Locale.US);
        final SimpleDateFormat DesiredFormat = new SimpleDateFormat(strDestinationFormat, Locale.US);

        final Date date;
        try {
            date = sourceFormat.parse(sourceDate);

            return DesiredFormat.format(date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
}