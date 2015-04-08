/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package at.illecker.sentistorm.commons.dict;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

public class WordListMap<V> extends TreeMap<String, V> {
  private static final long serialVersionUID = -8818638227574467468L;
  private List<String> m_startStrings = new ArrayList<String>();
  private boolean m_startStringsIsSorted = false;

  @Override
  public V put(String key, V value) {
    if (key.endsWith("*")) {
      String startString = key.substring(0, key.length() - 1);
      // LOG.info("Add startStrings: '" + key + "' startingWith: '" +
      // startString + "'");
      m_startStrings.add(startString);
      m_startStringsIsSorted = false;
    }
    return super.put(key, value);
  }

  private String searchForMatchingKey(String key) {
    if (!m_startStringsIsSorted) {
      Collections.sort(m_startStrings);
      m_startStringsIsSorted = true;
    }
    // Comparator checks if a key starts with given key string
    Comparator<String> startsWithComparator = new Comparator<String>() {
      public int compare(String currentItem, String key) {
        if (key.startsWith(currentItem)) {
          return 0;
        }
        return currentItem.compareTo(key);
      }
    };

    // binarySearch in sorted list
    int index = Collections.binarySearch(m_startStrings, key,
        startsWithComparator);
    if (index >= 0) {
      return m_startStrings.get(index);
    }
    return null;
  }

  public V matchKey(String key) {
    V result = super.get(key);
    if (result == null) {
      String matchingKey = searchForMatchingKey(key);
      if (matchingKey != null) {
        // LOG.info("Found match: " + key + "*" + " for " + key);
        result = super.get(key + "*");
      }
    }
    return result;
  }
}
