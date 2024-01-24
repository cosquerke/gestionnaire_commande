package util;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class DateFormat {
    private static volatile SimpleDateFormat instance;

    public static SimpleDateFormat getInstance() {
        SimpleDateFormat dateFormat = instance;
        if (null != dateFormat) {
            return dateFormat;
        }
        synchronized (DateFormat.class) {
            if (instance == null) {
                instance = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
            }
            return instance;
        }
    }

}
