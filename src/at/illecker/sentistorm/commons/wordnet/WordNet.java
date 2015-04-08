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
package at.illecker.sentistorm.commons.wordnet;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.illecker.sentistorm.commons.Configuration;
import at.illecker.sentistorm.commons.util.io.IOUtils;
import edu.mit.jwi.IRAMDictionary;
import edu.mit.jwi.RAMDictionary;
import edu.mit.jwi.data.ILoadPolicy;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISenseEntry;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.morph.WordnetStemmer;

public class WordNet {
  public static final int MAX_DEPTH_OF_HIERARCHY = 16;
  private static final Logger LOG = LoggerFactory.getLogger(WordNet.class);
  private static final WordNet INSTANCE = new WordNet();

  private IRAMDictionary m_dict;
  private File m_wordNetDir;
  private WordnetStemmer m_wordnetStemmer;

  private WordNet() {
    try {
      String wordNetDictPath = Configuration.getWordNetDict();
      LOG.info("WordNet Dictionary: " + wordNetDictPath);
      m_wordNetDir = new File(Configuration.TEMP_DIR_PATH + File.separator
          + "dict");
      LOG.info("WordNet Extract Location: " + m_wordNetDir.getAbsolutePath());

      // check if extract location does exist
      if (m_wordNetDir.exists()) {
        IOUtils.delete(m_wordNetDir);
      }

      // extract tar.gz file
      IOUtils.extractTarGz(wordNetDictPath, m_wordNetDir.getParent());

      m_dict = new RAMDictionary(m_wordNetDir, ILoadPolicy.NO_LOAD);
      m_dict.open();

      // load into memory
      long t = System.currentTimeMillis();
      m_dict.load(true);
      LOG.info("Loaded Wordnet into memory in "
          + (System.currentTimeMillis() - t) + " msec");

      m_wordnetStemmer = new WordnetStemmer(m_dict);

    } catch (IOException e) {
      LOG.error("IOException: " + e.getMessage());
    } catch (InterruptedException e) {
      LOG.error("InterruptedException: " + e.getMessage());
    }
  }

  public static WordNet getInstance() {
    return INSTANCE;
  }

  public void close() {
    if (m_dict != null) {
      m_dict.close();
    }
    try {
      IOUtils.delete(m_wordNetDir);
    } catch (IOException e) {
      LOG.error("IOException: " + e.getMessage());
    }
  }

  public boolean contains(String word) {
    for (POS pos : POS.values()) {
      for (String stem : m_wordnetStemmer.findStems(word, pos)) {
        IIndexWord indexWord = m_dict.getIndexWord(stem, pos);
        if (indexWord != null)
          return true;
      }
    }
    return false;
  }

  public boolean isNoun(String word) {
    return m_dict.getIndexWord(word, POS.NOUN) != null;
  }

  public boolean isAdjective(String word) {
    return m_dict.getIndexWord(word, POS.ADJECTIVE) != null;
  }

  public boolean isAdverb(String word) {
    return m_dict.getIndexWord(word, POS.ADVERB) != null;
  }

  public boolean isVerb(String word) {
    return m_dict.getIndexWord(word, POS.VERB) != null;
  }

  public synchronized POS findPOS(String word) {
    int maxCount = 0;
    POS mostLikelyPOS = null;
    for (POS pos : POS.values()) {
      // From JavaDoc: The surface form may or may not contain whitespace or
      // underscores, and may be in mixed case.
      word = word.replaceAll("\\s", "").replaceAll("_", "");

      List<String> stems = m_wordnetStemmer.findStems(word, pos);
      for (String stem : stems) {
        IIndexWord indexWord = m_dict.getIndexWord(stem, pos);
        if (indexWord != null) {
          int count = 0;
          for (IWordID wordId : indexWord.getWordIDs()) {
            IWord aWord = m_dict.getWord(wordId);
            ISenseEntry senseEntry = m_dict.getSenseEntry(aWord.getSenseKey());
            count += senseEntry.getTagCount();
          }

          if (count > maxCount) {
            maxCount = count;
            mostLikelyPOS = pos;
          }
        }
      }
    }

    return mostLikelyPOS;
  }

  public List<String> findStems(String word, POS pos) {
    return m_wordnetStemmer.findStems(word, pos);
  }

}
