package com.carousell.caronews.util;

import android.content.Context;
import android.content.res.Resources;

import com.carousell.caronews.R;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RelativeTimeHelper {

    private static final Map<Integer, Long> times = new LinkedHashMap<>();

    static {
        times.put(R.plurals.years, TimeUnit.DAYS.toMillis(365));
        times.put(R.plurals.months, TimeUnit.DAYS.toMillis(30));
        times.put(R.plurals.weeks, TimeUnit.DAYS.toMillis(7));
        times.put(R.plurals.days, TimeUnit.DAYS.toMillis(1));
        times.put(R.plurals.hours, TimeUnit.HOURS.toMillis(1));
        times.put(R.plurals.minutes, TimeUnit.MINUTES.toMillis(1));
        times.put(R.plurals.seconds, TimeUnit.SECONDS.toMillis(1));
    }

    public static String getRelativeTime(Context context, long duration) {

        long timeDiff= System.currentTimeMillis() - duration;
        Resources resources = context.getResources();
        String res = "";

        for (Map.Entry<Integer, Long> time : times.entrySet()){
            int timeDelta = (int)(timeDiff / time.getValue());
            if (timeDelta > 0){
                res = resources.getQuantityString(time.getKey(), timeDelta, timeDelta)
                        + " " + resources.getString(R.string.ago);
                break;
            }
        }

        return res.isEmpty() ? resources.getString(R.string.just_now) : res;
    }
}
