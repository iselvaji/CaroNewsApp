package com.carousell.caronews.util;

import com.carousell.caronews.model.pojo.News;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AppUtils {

    public static List<News> getSortedListByPopular(List<News> dataList) {
        Collections.sort(dataList, new Comparator<News>() {
            @Override
            public int compare(News news1, News news2) {
                int rankby = news1.getRank() - news2.getRank();
                if (rankby != 0)
                    return rankby;

                return news1.getTimeCreated().compareTo(news2.getTimeCreated());
            }
        });
        return dataList;
    }

    public static List<News> getSortedListByDate(List<News> dataList) {
        Collections.sort(dataList, new Comparator<News>() {
            @Override
            public int compare(News news1, News news2) {
                return news1.getTimeCreated().compareTo(news2.getTimeCreated());
            }
        });
        return dataList;
    }
}
