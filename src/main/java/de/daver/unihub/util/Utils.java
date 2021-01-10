package de.daver.unihub.util;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Utils {

    private Utils(){}

    public static <T> List<T> filterValues(Map<?, T> map, Predicate<? super T> filterOption){
        return map.values().stream().filter(filterOption).collect(Collectors.toList());
    }

}
