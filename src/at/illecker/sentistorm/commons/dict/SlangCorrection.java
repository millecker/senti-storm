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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.illecker.sentistorm.commons.Configuration;
import at.illecker.sentistorm.commons.util.io.FileUtils;
import at.illecker.sentistorm.commons.util.io.IOUtils;
import at.illecker.sentistorm.commons.util.io.SerializationUtils;

public class SlangCorrection {
  private static final Logger LOG = LoggerFactory
      .getLogger(SlangCorrection.class);
  private static final SlangCorrection INSTANCE = new SlangCorrection();

  private Map<String, String[]> m_slangWordList = new HashMap<String, String[]>();

  private SlangCorrection() {
    List<Map> slangWordLists = Configuration.getSlangWordlists();
    for (Map slangWordListEntry : slangWordLists) {
      String file = (String) slangWordListEntry.get("path");
      boolean isEnabled = (Boolean) slangWordListEntry.get("enabled");
      if (isEnabled) {
        Map<String, String> slangWordList = null;
        // Try deserialization of file
        String serializationFile = file + ".ser";
        if (IOUtils.exists(serializationFile)) {
          LOG.info("Deserialize SlangLookupTable from: " + serializationFile);
          slangWordList = SerializationUtils.deserialize(serializationFile);
        } else {
          String separator = (String) slangWordListEntry.get("delimiter");
          LOG.info("Load SlangLookupTable from: " + file);
          slangWordList = FileUtils.readFile(file, separator);
          SerializationUtils.serializeMap(slangWordList, serializationFile);
        }
        // Insert new entries
        for (Map.Entry<String, String> entry : slangWordList.entrySet()) {
          if (!m_slangWordList.containsKey(entry.getKey())) {
            m_slangWordList.put(entry.getKey(), entry.getValue().split(" "));
          }
        }
      }
    }
  }

  public static SlangCorrection getInstance() {
    return INSTANCE;
  }

  public String[] getCorrection(String token) {
    return m_slangWordList.get(token);
  }

  public static void main(String[] args) {
    SlangCorrection slangCorrection = SlangCorrection.getInstance();
    // Test SlangCorrection
    String[] testSlangCorrection = new String[] { "omg", "afk" };
    for (String s : testSlangCorrection) {
      System.out.println("getCorrection(" + s + "): "
          + Arrays.toString(slangCorrection.getCorrection(s)));
    }
  }

}
