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

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.illecker.sentistorm.commons.Configuration;
import at.illecker.sentistorm.commons.dict.SentimentDictionary;
import at.illecker.sentistorm.commons.tfidf.TweetTfIdf;
import cmu.arktweetnlp.Tagger.TaggedToken;

public class TfIdfFeatureVectorGenerator extends FeatureVectorGenerator {
  private static final Logger LOG = LoggerFactory
      .getLogger(TfIdfFeatureVectorGenerator.class);
  private static final boolean LOGGING = Configuration.get(
      "commons.featurevectorgenerator.tfidf.logging", false);

  private TweetTfIdf m_tweetTfIdf = null;
  private SentimentDictionary m_sentimentDict;
  private int m_vectorStartId = 1;

  public TfIdfFeatureVectorGenerator(TweetTfIdf tweetTfIdf) {
    this.m_tweetTfIdf = tweetTfIdf;
    this.m_sentimentDict = SentimentDictionary.getInstance();
    LOG.info("VectorSize: " + getFeatureVectorSize());
  }

  public TfIdfFeatureVectorGenerator(TweetTfIdf tweetTfIdf, int vectorStartId) {
    this(tweetTfIdf);
    this.m_vectorStartId = vectorStartId;
  }

  public SentimentDictionary getSentimentDictionary() {
    return m_sentimentDict;
  }

  @Override
  public int getFeatureVectorSize() {
    return m_tweetTfIdf.getInverseDocFreq().size();
  }

  @Override
  public Map<Integer, Double> generateFeatureVector(List<TaggedToken> tweet) {
    return generateFeatureVector(m_tweetTfIdf.tfIdfFromTaggedTokens(tweet));
  }

  public Map<Integer, Double> generateFeatureVector(Map<String, Double> tfIdf) {
    Map<Integer, Double> resultFeatureVector = new TreeMap<Integer, Double>();

    if (m_tweetTfIdf != null) {
      // Map<String, Double> idf = m_tweetTfIdf.getInverseDocFreq();
      Map<String, Integer> termIds = m_tweetTfIdf.getTermIds();

      for (Map.Entry<String, Double> element : tfIdf.entrySet()) {
        String key = element.getKey();
        if (termIds.containsKey(key)) {
          int vectorId = m_vectorStartId + termIds.get(key);
          resultFeatureVector.put(vectorId, element.getValue());
        }
      }
    }
    if (LOGGING) {
      LOG.info("TfIdsFeatureVector: " + resultFeatureVector);
    }
    return resultFeatureVector;
  }

}
