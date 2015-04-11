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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum SentimentClass {
  POSITIVE, NEGATIVE, NEUTRAL;

  private static final Logger LOG = LoggerFactory
      .getLogger(SentimentClass.class);

  public static SentimentClass fromScore(Dataset dataset, int score) {
    if (score == dataset.getPositiveValue()) {
      return POSITIVE;
    } else if (score == dataset.getNegativeValue()) {
      return NEGATIVE;
    } else if (score == dataset.getNeutralValue()) {
      return NEUTRAL;
    } else {
      LOG.error("Score: '" + score + "' is not valid!");
    }
    return null;
  }

}
