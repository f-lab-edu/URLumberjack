package com.flab.urlumberjack.global.Utils;

import java.util.List;

public class CollectionUtils {

    public static <T> boolean isListBlank(List<T> list) {
        return list == null || list.isEmpty();
    }

}
