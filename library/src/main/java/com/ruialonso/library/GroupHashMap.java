package com.ruialonso.library;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by rui.alonso on 5/10/16.
 */

public class GroupHashMap<K, V> {
  private HashMap<K, List<V>> groupHashMap;

  public GroupHashMap() {
    this.groupHashMap = new HashMap<>();
  }

  /**
   * Concats element to list if collision
   * @param key
   * @param value
   * @return
   */
  public List<V> add(K key, V value) {
    List<V> listValue = groupHashMap.get(key);
    if (listValue == null) listValue = new ArrayList<>();
    listValue.add(value);
    return listValue;
  }

  /**
   * Get if all elements in hash and in list values are empty
   * @return
   */
  public boolean isEmpty() {
    boolean empty = false;
    if(groupHashMap.size() == 1) {
      Iterator<List<V>> iterator = groupHashMap.values().iterator();
      List<V> submenuList = iterator.next();
      empty = (submenuList.size() == 1);
    }

    return empty;
  }

  /**
   * Get all value elements in Map
   * @return List with all values concatenated
   */
  public List<V> allValues() {
    List<V> values = new ArrayList<>();
    for (Map.Entry<K, List<V>> entry : groupHashMap.entrySet()) {
      for (V entryListItem : entry.getValue()) {
        values.add(entryListItem);
      }
    }
    return values;
  }

  /**
   * Get value for a concrete key
   * @param key
   * @return
   */
  public List<V> get(K key) {
    return groupHashMap.get(key);
  }
}
