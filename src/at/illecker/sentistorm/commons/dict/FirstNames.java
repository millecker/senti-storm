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

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.illecker.sentistorm.commons.Configuration;
import at.illecker.sentistorm.commons.util.io.FileUtils;
import at.illecker.sentistorm.commons.util.io.IOUtils;
import at.illecker.sentistorm.commons.util.io.SerializationUtils;

public class FirstNames {
  private static final Logger LOG = LoggerFactory.getLogger(FirstNames.class);
  private static final FirstNames INSTANCE = new FirstNames();

  private Set<String> m_firstNames = null;

  @SuppressWarnings("unchecked")
  private FirstNames() {
    for (String file : Configuration.getFirstNames()) {
      // Try deserialization of file
      String serializationFile = file + ".ser";
      if (IOUtils.exists(serializationFile)) {
        LOG.info("Deserialize FirstNames from: " + serializationFile);
        if (m_firstNames == null) {
          m_firstNames = SerializationUtils.deserialize(serializationFile);
        } else {
          m_firstNames.addAll((Set<String>) SerializationUtils
              .deserialize(serializationFile));
        }
      } else {
        LOG.info("Load FirstNames from: " + file);
        if (m_firstNames == null) {
          m_firstNames = FileUtils.readFile(file, true);
          SerializationUtils.serializeCollection(m_firstNames,
              serializationFile);
        } else {
          Set<String> firstNames = FileUtils.readFile(file, true);
          SerializationUtils.serializeCollection(firstNames, serializationFile);
          m_firstNames.addAll(firstNames);
        }
      }
    }
  }

  public static FirstNames getInstance() {
    return INSTANCE;
  }

  public boolean isFirstName(String value) {
    return m_firstNames.contains(value.toLowerCase());
  }

  public static void main(String[] args) {
    FirstNames firstNames = FirstNames.getInstance();
    // Test FirstNames
    String[] testFirstNames = new String[] { "Kevin", "Martin", "martin",
        "justin" };
    for (String s : testFirstNames) {
      System.out
          .println("isFirstName(" + s + "): " + firstNames.isFirstName(s));
    }
  }

}
