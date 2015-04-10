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
package at.illecker.sentistorm.commons;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import at.illecker.sentistorm.commons.featurevector.CombinedFeatureVectorGenerator;
import at.illecker.sentistorm.commons.featurevector.FeatureVectorGenerator;
import at.illecker.sentistorm.commons.tfidf.TfIdfNormalization;
import at.illecker.sentistorm.commons.tfidf.TfType;
import at.illecker.sentistorm.commons.tfidf.TweetTfIdf;
import at.illecker.sentistorm.commons.util.io.SerializationUtils;
import at.illecker.sentistorm.components.POSTagger;
import at.illecker.sentistorm.components.Preprocessor;
import at.illecker.sentistorm.components.Tokenizer;
import cmu.arktweetnlp.Tagger.TaggedToken;

public final class FeaturedTweet extends Tweet implements Serializable {
  private static final long serialVersionUID = -1917356433934166756L;
  private final List<String> m_tokens;
  private final List<String> m_preprocessedTokens;
  private final List<TaggedToken> m_taggedTokens;
  private final Map<Integer, Double> m_featureVector; // dense vector

  public FeaturedTweet(long id, String text, double score, List<String> tokens,
      List<String> preprocessedTokens, List<TaggedToken> taggedTokens,
      Map<Integer, Double> featureVector) {
    super(id, text, score);
    m_tokens = tokens;
    m_preprocessedTokens = preprocessedTokens;
    m_taggedTokens = taggedTokens;
    m_featureVector = featureVector;
  }

  public FeaturedTweet(Tweet tweet, List<String> tokens,
      List<String> preprocessedTokens, List<TaggedToken> taggedTokens,
      Map<Integer, Double> featureVector) {
    this(tweet.getId(), tweet.getText(), tweet.getScore(), tokens,
        preprocessedTokens, taggedTokens, featureVector);
  }

  public List<String> getTokens() {
    return m_tokens;
  }

  public List<String> getPreprocessedTokens() {
    return m_preprocessedTokens;
  }

  public List<TaggedToken> getTaggedTokens() {
    return m_taggedTokens;
  }

  public Map<Integer, Double> getFeatureVector() {
    return m_featureVector;
  }

  @Override
  public boolean equals(Object obj) {
    return super.equals(obj);
  }

  @Override
  public String toString() {
    return super.toString();
  }

  public static final List<List<TaggedToken>> getTaggedTokensFromTweets(
      List<FeaturedTweet> featuredTweets) {
    List<List<TaggedToken>> taggedTweets = new ArrayList<List<TaggedToken>>();
    for (FeaturedTweet tweet : featuredTweets) {
      taggedTweets.add(tweet.getTaggedTokens());
    }
    return taggedTweets;
  }

  public static final List<FeaturedTweet> generateFeatureTweets(
      List<Tweet> tweets) {
    final Preprocessor preprocessor = Preprocessor.getInstance();
    final POSTagger postTagger = POSTagger.getInstance();

    // Tokenize
    List<List<String>> tokenizedTweets = Tokenizer.tokenizeTweets(tweets);

    // Preprocess
    List<List<String>> preprocessedTweets = preprocessor
        .preprocessTweets(tokenizedTweets);

    // POS Tagging
    List<List<TaggedToken>> taggedTweets = postTagger
        .tagTweets(preprocessedTweets);

    // Load Feature Vector Generator
    TweetTfIdf tweetTfIdf = TweetTfIdf.createFromTaggedTokens(taggedTweets,
        TfType.LOG, TfIdfNormalization.COS, true);
    FeatureVectorGenerator fvg = new CombinedFeatureVectorGenerator(true,
        tweetTfIdf);

    // Feature Vector Generation
    List<FeaturedTweet> featuredTweets = new ArrayList<FeaturedTweet>();
    for (int i = 0; i < tweets.size(); i++) {
      List<TaggedToken> taggedTweet = taggedTweets.get(i);
      Map<Integer, Double> featureVector = fvg
          .generateFeatureVector(taggedTweet);

      featuredTweets.add(new FeaturedTweet(tweets.get(i), tokenizedTweets
          .get(i), preprocessedTweets.get(i), taggedTweet, featureVector));
    }

    return featuredTweets;
  }

  public static void main(String[] args) {
    Dataset dataset = Configuration.getDataSetSemEval2013();
    boolean includeDevTweets = true;

    // Generate features of train tweets
    List<FeaturedTweet> featuredTrainTweets = generateFeatureTweets(dataset
        .getTrainTweets(includeDevTweets));

    SerializationUtils.serializeCollection(featuredTrainTweets,
        dataset.getTrainDataSerializationFile());

    // Generate features of test tweets
    List<FeaturedTweet> featuredTestTweets = generateFeatureTweets(dataset
        .getTestTweets());

    SerializationUtils.serializeCollection(featuredTestTweets,
        dataset.getTestDataSerializationFile());
  }

}
