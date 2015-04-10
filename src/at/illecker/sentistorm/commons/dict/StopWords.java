package at.illecker.sentistorm.commons.dict;

import java.util.List;
import java.util.Set;

import org.apache.storm.guava.collect.Sets;
import org.slf4j.Logger;
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
import org.slf4j.LoggerFactory;

import at.illecker.sentistorm.commons.Configuration;
import at.illecker.sentistorm.commons.util.io.FileUtils;
import at.illecker.sentistorm.commons.util.io.IOUtils;
import at.illecker.sentistorm.commons.util.io.SerializationUtils;

public class StopWords {
  // default stop words from nltk.corpus
  public static final String[] STOP_WORDS = new String[] { "i", "me", "my",
      "myself", "we", "our", "ours", "ourselves", "you", "your", "yours",
      "yourself", "yourselves", "he", "him", "his", "himself", "she", "her",
      "hers", "herself", "it", "its", "itself", "they", "them", "their",
      "theirs", "themselves", "what", "which", "who", "whom", "this", "that",
      "these", "those", "am", "is", "are", "was", "were", "be", "been",
      "being", "have", "has", "had", "having", "do", "does", "did", "doing",
      "a", "an", "the", "and", "but", "if", "or", "because", "as", "until",
      "while", "of", "at", "by", "for", "with", "about", "against", "between",
      "into", "through", "during", "before", "after", "above", "below", "to",
      "from", "up", "down", "in", "out", "on", "off", "over", "under", "again",
      "further", "then", "once", "here", "there", "when", "where", "why",
      "how", "all", "any", "both", "each", "few", "more", "most", "other",
      "some", "such", "no", "nor", "not", "only", "own", "same", "so", "than",
      "too", "very", "s", "t", "can", "will", "just", "don", "should", "now" };

  private static final Logger LOG = LoggerFactory.getLogger(StopWords.class);
  private static final StopWords INSTANCE = new StopWords();

  private Set<String> m_stopwords = null;

  @SuppressWarnings("unchecked")
  private StopWords() {
    m_stopwords = Sets.newHashSet(STOP_WORDS);

    List<String> files = Configuration.getStopWords();
    if (files != null) {
      for (String file : files) {
        // Try deserialization of file
        String serializationFile = file + ".ser";
        if (IOUtils.exists(serializationFile)) {
          LOG.info("Deserialize FirstNames from: " + serializationFile);
          m_stopwords.addAll((Set<String>) SerializationUtils
              .deserialize(serializationFile));
        } else {
          LOG.info("Load StopWords from: " + file);
          Set<String> stopWords = FileUtils.readFile(file, true);
          SerializationUtils.serializeCollection(stopWords, serializationFile);
          m_stopwords.addAll(stopWords);
        }
      }
    }
  }

  public static StopWords getInstance() {
    return INSTANCE;
  }

  public boolean isStopWord(String value) {
    return m_stopwords.contains(value.toLowerCase());
  }

  public static void main(String[] args) {
    StopWords stopWords = StopWords.getInstance();
    // Test StopWords
    String[] testStopWords = new String[] { "i", "me", };
    for (String s : testStopWords) {
      System.out.println("isStopWord(" + s + "): " + stopWords.isStopWord(s));
    }
  }

}
