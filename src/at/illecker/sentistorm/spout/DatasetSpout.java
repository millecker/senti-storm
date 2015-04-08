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

import java.util.List;
import java.util.Map;

import at.illecker.sentistorm.commons.Configuration;
import at.illecker.sentistorm.commons.Dataset;
import at.illecker.sentistorm.commons.Tweet;
import at.illecker.sentistorm.commons.util.TimeUtils;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class DatasetSpout extends BaseRichSpout {
  public static final String ID = "dataset-spout";
  public static final String CONF_STARTUP_SLEEP_MS = ID + ".startup.sleep.ms";
  public static final String CONF_TUPLE_SLEEP_MS = ID + ".tuple.sleep.ms";
  public static final String CONF_TUPLE_SLEEP_NS = ID + ".spout.tuple.sleep.ns";
  private static final long serialVersionUID = 3028853846518561027L;
  private Dataset m_dataset;
  private SpoutOutputCollector m_collector;
  private List<Tweet> m_tweets;
  private long m_messageId = 0;
  private int m_index = 0;
  private long m_tupleSleepMs = 0;
  private long m_tupleSleepNs = 0;

  public void declareOutputFields(OutputFieldsDeclarer declarer) {
    // key of output tuples
    declarer.declare(new Fields("id", "score", "text"));
  }

  public void open(Map config, TopologyContext context,
      SpoutOutputCollector collector) {
    this.m_collector = collector;
    this.m_dataset = Configuration.getDataSetSemEval2013();
    this.m_tweets = m_dataset.getTestTweets();

    // Optional sleep between tuples emitting
    if (config.get(CONF_TUPLE_SLEEP_MS) != null) {
      m_tupleSleepMs = (Long) config.get(CONF_TUPLE_SLEEP_MS);
    } else {
      m_tupleSleepMs = 0;
    }
    if (config.get(CONF_TUPLE_SLEEP_NS) != null) {
      m_tupleSleepNs = (Long) config.get(CONF_TUPLE_SLEEP_NS);
    } else {
      m_tupleSleepNs = 0;
    }

    // Optional startup sleep to finish bolt preparation
    // before spout starts emitting
    if (config.get(CONF_STARTUP_SLEEP_MS) != null) {
      long startupSleepMillis = (Long) config.get(CONF_STARTUP_SLEEP_MS);
      TimeUtils.sleepMillis(startupSleepMillis);
    }
  }

  public void nextTuple() {
    Tweet tweet = m_tweets.get(m_index);

    // infinite loop
    m_index++;
    if (m_index >= m_tweets.size()) {
      m_index = 0;
    }
    m_messageId++; // accept possible overflow

    // Emit tweet
    m_collector.emit(
        new Values(tweet.getId(), tweet.getScore(), tweet.getText()),
        m_messageId);

    // Optional sleep
    if (m_tupleSleepMs != 0) {
      TimeUtils.sleepMillis(m_tupleSleepMs);
    }
    if (m_tupleSleepNs != 0) {
      TimeUtils.sleepNanos(m_tupleSleepNs);
    }
  }
}
