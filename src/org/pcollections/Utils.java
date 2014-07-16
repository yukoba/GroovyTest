package org.pcollections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Yu Kobayashi
 */
class Utils {
    static <T> Collection<T> asCollection(Iterable<T> self) {
        if (self instanceof Collection) {
            return (Collection<T>) self;
        } else {
            return toList(self);
        }
    }

    static <T> List<T> toList(Iterable<T> self) {
        List<T> answer = new ArrayList<T>();
        for (T t : self) {
            answer.add(t);
        }
        return answer;
    }
}
