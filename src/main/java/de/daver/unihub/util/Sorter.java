package de.daver.unihub.util;

import java.util.List;

public interface Sorter<T>{

    List<T> sort(List<T> list);

    /**
     * Compares to values of the same type
     * @param value the value to compare
     * @param comparable value of the same type
     * @return
     * 1 -> value is bigger than comparable
     * 0 -> both are equal
     * -1 -> value is smaller than comparable
     */
    int compare(T value, T comparable);

    default void switchValues(int index1, int index2, List<T> list){
        T tmp = list.get(index1);
        list.set(index1, list.get(index2));
        list.set(index2, tmp);
    }
}
