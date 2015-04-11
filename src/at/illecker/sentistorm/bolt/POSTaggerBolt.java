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
package at.illecker.sentistorm.bolt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.illecker.sentistorm.commons.Configuration;
import at.illecker.sentistorm.commons.util.io.SerializationUtils;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import cmu.arktweetnlp.Tagger.TaggedToken;
import cmu.arktweetnlp.impl.Model;
import cmu.arktweetnlp.impl.ModelSentence;
import cmu.arktweetnlp.impl.Sentence;
import cmu.arktweetnlp.impl.features.FeatureExtractor;

public class POSTaggerBolt extends BaseBasicBolt {
  public static final String ID = "pos-tagger-bolt";
  public static final String CONF_LOGGING = ID + ".logging";
  public static final String CONF_MODEL = ID + ".model";
  private static final long serialVersionUID = -7890576107718544088L;
  private static final Logger LOG = LoggerFactory
      .getLogger(POSTaggerBolt.class);
  private boolean m_logging = false;

  private Model m_model;
  private FeatureExtractor m_featureExtractor;

  @Override
  public void declareOutputFields(OutputFieldsDeclarer declarer) {
    // key of output tuples
    declarer.declare(new Fields("text", "taggedTokens"));
  }

  @Override
  public void prepare(Map config, TopologyContext context) {
    // Optional set logging
    if (config.get(CONF_LOGGING) != null) {
      m_logging = (Boolean) config.get(CONF_LOGGING);
    } else {
      m_logging = false;
    }

    // Load POS Tagger
    String taggingModel = Configuration.get("sentistorm.bolt.postagger.model");
    LOG.info("Load POS Tagger model: " + taggingModel + "_model.ser");
    m_model = SerializationUtils.deserialize(taggingModel + "_model.ser");
    LOG.info("Load POS Tagger featureExtractor : " + taggingModel
        + "_featureExtractor.ser");
    m_featureExtractor = SerializationUtils.deserialize(taggingModel
        + "_featureExtractor.ser");
  }

  @Override
  public void execute(Tuple tuple, BasicOutputCollector collector) {
    String text = tuple.getStringByField("text");
    List<String> preprocessedTokens = (List<String>) tuple
        .getValueByField("preprocessedTokens");

    // POS Tagging
    List<TaggedToken> taggedTokens = tag(preprocessedTokens);

    if (m_logging) {
      LOG.info("Tweet: " + taggedTokens);
    }

    // Emit new tuples
    collector.emit(new Values(text, taggedTokens));
  }

  private List<TaggedToken> tag(List<String> tokens) {
    Sentence sentence = new Sentence();
    sentence.tokens = tokens;
    ModelSentence ms = new ModelSentence(sentence.T());
    m_featureExtractor.computeFeatures(sentence, ms);
    m_model.greedyDecode(ms, false);

    List<TaggedToken> taggedTokens = new ArrayList<TaggedToken>();
    for (int t = 0; t < sentence.T(); t++) {
      TaggedToken tt = new TaggedToken(tokens.get(t),
          m_model.labelVocab.name(ms.labels[t]));
      taggedTokens.add(tt);
    }
    return taggedTokens;
  }

}
