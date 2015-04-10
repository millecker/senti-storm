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
package at.illecker.sentistorm.commons.featurevector;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.illecker.sentistorm.commons.Configuration;
import cmu.arktweetnlp.Tagger.TaggedToken;

public class POSFeatureVectorGenerator extends FeatureVectorGenerator {
  private static final Logger LOG = LoggerFactory
      .getLogger(POSFeatureVectorGenerator.class);
  private static final boolean LOGGING = Configuration.get(
      "commons.featurevectorgenerator.pos.logging", false);
  private int m_vectorStartId;
  private final boolean m_normalize;
  private final int m_vectorSize;

  public POSFeatureVectorGenerator(boolean normalize) {
    m_normalize = normalize;
    m_vectorStartId = 1;
    m_vectorSize = 8;
    LOG.info("VectorSize: " + m_vectorSize);
  }

  public POSFeatureVectorGenerator(boolean normalize, int vectorStartId) {
    this(normalize);
    this.m_vectorStartId = vectorStartId;
  }

  @Override
  public int getFeatureVectorSize() {
    return m_vectorSize;
  }

  @Override
  public Map<Integer, Double> generateFeatureVector(
      List<TaggedToken> taggedTokens) {
    Map<Integer, Double> resultFeatureVector = new TreeMap<Integer, Double>();
    double[] posTags = countPOSTagsFromTaggedTokens(taggedTokens, m_normalize);
    if (posTags != null) {
      if (posTags[0] != 0) // nouns
        resultFeatureVector.put(m_vectorStartId, posTags[0]);
      if (posTags[1] != 0) // verb
        resultFeatureVector.put(m_vectorStartId + 1, posTags[1]);
      if (posTags[2] != 0) // adjective
        resultFeatureVector.put(m_vectorStartId + 2, posTags[2]);
      if (posTags[3] != 0) // adverb
        resultFeatureVector.put(m_vectorStartId + 3, posTags[3]);
      if (posTags[4] != 0) // interjection
        resultFeatureVector.put(m_vectorStartId + 4, posTags[4]);
      if (posTags[5] != 0) // punctuation
        resultFeatureVector.put(m_vectorStartId + 5, posTags[5]);
      if (posTags[6] != 0) // hashtag
        resultFeatureVector.put(m_vectorStartId + 6, posTags[6]);
      if (posTags[7] != 0) // emoticon
        resultFeatureVector.put(m_vectorStartId + 7, posTags[7]);
    }
    if (LOGGING) {
      LOG.info("POStags: " + Arrays.toString(posTags));
    }
    return resultFeatureVector;
  }

  private double[] countPOSTagsFromTaggedTokens(List<TaggedToken> taggedTokens,
      boolean normalize) {
    // 8 = [NOUN, VERB, ADJECTIVE, ADVERB, INTERJECTION, PUNCTUATION, HASHTAG,
    // EMOTICON]
    double[] posTags = new double[] { 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d };
    int wordCount = 0;
    for (TaggedToken word : taggedTokens) {
      wordCount++;
      String arkTag = word.tag;
      // http://www.ark.cs.cmu.edu/TweetNLP/annot_guidelines.pdf
      if (arkTag.equals("N") || arkTag.equals("O") || arkTag.equals("^")
          || arkTag.equals("Z")) {
        posTags[0]++;
      } else if (arkTag.equals("V") || arkTag.equals("T")) {
        posTags[1]++;
      } else if (arkTag.equals("A")) {
        posTags[2]++;
      } else if (arkTag.equals("R")) {
        posTags[3]++;
      } else if (arkTag.equals("!")) {
        posTags[4]++;
      } else if (arkTag.equals(",")) {
        posTags[5]++;
      } else if (arkTag.equals("#")) {
        posTags[6]++;
      } else if (arkTag.equals("E")) {
        posTags[7]++;
      }
    }
    if (normalize) {
      for (int i = 0; i < posTags.length; i++) {
        posTags[i] /= wordCount;
      }
    }
    return posTags;
  }

}
