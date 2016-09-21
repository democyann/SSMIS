package com.systop.core.util;

import java.text.Collator;
import java.util.*;

/**
 * User: slimx
 * Date: 12-7-24
 * Time: 下午4:13
 */
public class MapUtil {
    /**
     * 对map根据值以拼音的顺序排列
     * @param map
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        try {
            List<Map.Entry<K, V>> list =
                    new LinkedList<Map.Entry<K, V>>(map.entrySet());
            Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
                public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                    return Collator.getInstance(Locale.CHINESE).compare(o1.getValue(), o2.getValue());
                }
            });

            Map<K, V> result = new LinkedHashMap<K, V>();
            for (Map.Entry<K, V> entry : list) {
                result.put(entry.getKey(), entry.getValue());
            }
            return result;
        } catch (Exception e) {
            return map;
        }
    }

    /**
     * 对map根据key以拼音排序
     * @param map
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByKey(Map<K, V> map) {
        try {
            List<Map.Entry<K, V>> list =
                    new LinkedList<Map.Entry<K, V>>(map.entrySet());
            Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
                public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                    return Collator.getInstance(Locale.CHINESE).compare(o1.getKey(), o2.getKey());
                }
            });

            Map<K, V> result = new LinkedHashMap<K, V>();
            for (Map.Entry<K, V> entry : list) {
                result.put(entry.getKey(), entry.getValue());
            }
            return result;
        } catch (Exception e) {
            return map;
        }
    }
}
