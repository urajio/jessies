package e.util;

import java.text.*;
import java.util.*;

/**
 * Utilities to make it easier to work with ISO 8601 dates and times.
 * FIXME: support for RFC2822 format would be good, too.
 */
public class TimeUtilities {
    private static final SimpleDateFormat ISO_8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SZ");

    /**
     * Returns the Date corresponding to the given ISO 8601-format String.
     */
    public static Date fromIsoString(String date) throws ParseException {
        return ISO_8601.parse(date);
    }

    /**
     * Returns the given Date as a String in ISO 8601 format.
     */
    public static String toIsoString(Date date) {
        return ISO_8601.format(date);
    }

    /**
     * Returns the current time as a String in ISO 8601 format.
     */
    public static String currentIsoString() {
        return toIsoString(new Date());
    }

    /**
     * Returns the ISO 8601-format String corresponding to the
     * given duration (measured in milliseconds).
     */
    public static String durationToIsoString(long duration) {
        long milliseconds = duration % 1000;
        duration /= 1000;
        long seconds = duration % 60;
        duration /= 60;
        long minutes = duration % 60;
        duration /= 60;
        long hours = duration;

        StringBuilder result = new StringBuilder("P");
        if (hours != 0) {
            result.append(hours);
            result.append('H');
        }
        if (result.length() > 1 || minutes != 0) {
            result.append(minutes);
            result.append('M');
        }
        result.append(seconds);
        if (milliseconds != 0) {
            result.append('.');
            result.append(milliseconds);
        }
        result.append('S');
        return result.toString();
    }

    /**
     * Returns the number of milliseconds between the given Dates.
     */
    public static long millisecondsBetween(Date start, Date finish) {
        Calendar calendar1 = new GregorianCalendar();
        calendar1.setTime(start);
        Calendar calendar2 = new GregorianCalendar();
        calendar2.setTime(finish);
        return calendar2.getTimeInMillis() - calendar1.getTimeInMillis();
    }

    private TimeUtilities() {
    }

    public static void main(String[] arguments) {
        java.io.PrintStream out = System.out;
        Date startDate = new Date();
        long start = System.currentTimeMillis();
        out.println(currentIsoString());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            ex.printStackTrace(out);
        }
        Date endDate = new Date();
        long end = System.currentTimeMillis();
        out.println(currentIsoString());
        out.println(durationToIsoString(end - start));
        out.println(durationToIsoString(millisecondsBetween(startDate, endDate)));
        for (String argument : arguments) {
            out.println(durationToIsoString(Long.parseLong(argument)));
        }
    }
}
