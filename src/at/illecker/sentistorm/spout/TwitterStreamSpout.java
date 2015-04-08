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
package at.illecker.sentistorm.spout;

import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;
import at.illecker.sentistorm.commons.util.TimeUtils;
import backtype.storm.Config;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class TwitterStreamSpout extends BaseRichSpout {
  public static final String ID = "twitter-stream-spout";
  public static final String CONF_STARTUP_SLEEP_MS = ID + ".startup.sleep.ms";
  private static final long serialVersionUID = -4657730220755697034L;
  private SpoutOutputCollector m_collector;
  private LinkedBlockingQueue<Status> m_tweetsQueue = null;
  private TwitterStream m_twitterStream;
  private String m_consumerKey;
  private String m_consumerSecret;
  private String m_accessToken;
  private String m_accessTokenSecret;
  private String[] m_keyWords;
  private String m_filterLanguage;

  public TwitterStreamSpout(String consumerKey, String consumerSecret,
      String accessToken, String accessTokenSecret, String[] keyWords,
      String filterLanguage) {
    this.m_consumerKey = consumerKey;
    this.m_consumerSecret = consumerSecret;
    this.m_accessToken = accessToken;
    this.m_accessTokenSecret = accessTokenSecret;
    this.m_keyWords = keyWords;
    this.m_filterLanguage = filterLanguage; // "en"
  }

  @Override
  public void declareOutputFields(OutputFieldsDeclarer declarer) {
    // key of output tuples
    declarer.declare(new Fields("id", "text", "score"));
  }

  @Override
  public void open(Map config, TopologyContext context,
      SpoutOutputCollector collector) {
    m_collector = collector;
    m_tweetsQueue = new LinkedBlockingQueue<Status>(1000);

    // Optional startup sleep to finish bolt preparation
    // before spout starts emitting
    if (config.get(CONF_STARTUP_SLEEP_MS) != null) {
      long startupSleepMillis = (Long) config.get(CONF_STARTUP_SLEEP_MS);
      TimeUtils.sleepMillis(startupSleepMillis);
    }

    TwitterStream twitterStream = new TwitterStreamFactory(
        new ConfigurationBuilder().setJSONStoreEnabled(true).build())
        .getInstance();

    // Set Listener
    twitterStream.addListener(new StatusListener() {
      @Override
      public void onStatus(Status status) {
        m_tweetsQueue.offer(status); // add tweet into queue
      }

      @Override
      public void onException(Exception arg0) {
      }

      @Override
      public void onDeletionNotice(StatusDeletionNotice arg0) {
      }

      @Override
      public void onScrubGeo(long arg0, long arg1) {
      }

      @Override
      public void onStallWarning(StallWarning arg0) {
      }

      @Override
      public void onTrackLimitationNotice(int arg0) {
      }
    });

    // Set credentials
    twitterStream.setOAuthConsumer(m_consumerKey, m_consumerSecret);
    AccessToken token = new AccessToken(m_accessToken, m_accessTokenSecret);
    twitterStream.setOAuthAccessToken(token);

    // Filter twitter stream
    FilterQuery tweetFilterQuery = new FilterQuery();
    if (m_keyWords != null) {
      tweetFilterQuery.track(m_keyWords);
    }

    // Filter location
    // https://dev.twitter.com/docs/streaming-apis/parameters#locations
    tweetFilterQuery.locations(new double[][] { new double[] { -180, -90, },
        new double[] { 180, 90 } }); // any geotagged tweet

    // Filter language
    tweetFilterQuery.language(new String[] { m_filterLanguage });

    twitterStream.filter(tweetFilterQuery);
  }

  @Override
  public void nextTuple() {
    Status tweet = m_tweetsQueue.poll();
    if (tweet == null) {
      TimeUtils.sleepMillis(50); // sleep 50 ms
    } else {
      // Emit tweet
      m_collector.emit(new Values(tweet.getId(), tweet.getText(), null));
    }
  }

  @Override
  public void close() {
    m_twitterStream.shutdown();
  }

  @Override
  public Map<String, Object> getComponentConfiguration() {
    Config ret = new Config();
    ret.setMaxTaskParallelism(1);
    return ret;
  }

  @Override
  public void ack(Object id) {
  }

  @Override
  public void fail(Object id) {
  }
}
