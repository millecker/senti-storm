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

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.illecker.sentistorm.components.Preprocessor;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class PreprocessorBolt extends BaseBasicBolt {
  public static final String ID = "preprocessor-bolt";
  public static final String CONF_LOGGING = ID + ".logging";
  private static final long serialVersionUID = -8518185528053643981L;
  private static final Logger LOG = LoggerFactory
      .getLogger(PreprocessorBolt.class);
  private boolean m_logging = false;
  private Preprocessor m_preprocessor;

  @Override
  public void declareOutputFields(OutputFieldsDeclarer declarer) {
    // key of output tuples
    declarer.declare(new Fields("preprocessedTokens"));
  }

  @Override
  public void prepare(Map config, TopologyContext context) {
    // Optional set logging
    if (config.get(CONF_LOGGING) != null) {
      m_logging = (Boolean) config.get(CONF_LOGGING);
    } else {
      m_logging = false;
    }
    // Load Preprocessor
    m_preprocessor = Preprocessor.getInstance();
  }

  @Override
  public void execute(Tuple tuple, BasicOutputCollector collector) {
    List<String> tokens = (List<String>) tuple.getValueByField("tokens");

    // Preprocess
    List<String> preprocessedTokens = m_preprocessor.preprocess(tokens);

    if (m_logging) {
      LOG.info("Tweet: " + preprocessedTokens);
    }

    // Emit new tuples
    collector.emit(new Values(preprocessedTokens));
  }

}
