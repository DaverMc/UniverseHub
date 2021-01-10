package de.daver.unihub.util;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractBubbleSorter<T> implements Sorter<T> {

    @Override
    public List<T> sort(List<T> list) {
        List<T> clone = new ArrayList<>(list);
        for (int i = 0; i < clone.size() / 2; i++) {
            T max = null;
            T min = null;
            for (int index = i; index < clone.size() - i; index++) {
                T tmp = clone.get(index);
                if (max == null || compare(tmp, max) == 1) max = tmp;
                if (min == null || compare(tmp, min) == -1) min = tmp;
            }
            switchValues(i, clone.indexOf(min), clone);
            switchValues(clone.size() - i - 1, clone.indexOf(max), clone);
        }
        return clone;
    }
}