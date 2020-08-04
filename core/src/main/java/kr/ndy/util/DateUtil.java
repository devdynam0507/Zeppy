package kr.ndy.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    private static final String DATE_FORMAT = "yyyy-MM-dd-hh:mm:ss";
    public static final SimpleDateFormat DATE_FORMAT_INSTANCE = new SimpleDateFormat(DATE_FORMAT);

    public static String generateFormat(Date date)
    {
        return DATE_FORMAT_INSTANCE.format(date);
    }

    public static Date conversion(String dateStr)
    {
        Date date = null;

        try
        {
            date = DATE_FORMAT_INSTANCE.parse(dateStr);
        }catch (ParseException e)
        {
            e.printStackTrace();
        }

        return date;
    }

}
