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
package at.illecker.sentistorm.commons.dict;

import java.util.ArrayList;
import java.util.List;

public class SentimentResult {
  public static final double NEGATIVE_THRESHOLD = 0.45; // < 0.45
  public static final double POSITIVE_THRESHOLD = 0.55; // > 0.55
  private int m_posCount;
  private int m_neutralCount;
  private int m_negCount;
  private double m_sum;
  private Double m_maxPos;
  private Double m_maxNeg;
  private List<Double> m_scores;

  public SentimentResult() {
    this.m_posCount = 0;
    this.m_neutralCount = 0;
    this.m_negCount = 0;
    this.m_sum = 0;
    this.m_maxPos = null;
    this.m_maxNeg = null;
    this.m_scores = new ArrayList<Double>();
  }

  public int getPosCount() {
    return m_posCount;
  }

  public double getAvgPosCount() {
    return m_posCount / (double) m_scores.size();
  }

  public int getNeutralCount() {
    return m_neutralCount;
  }

  public double getAvgNeutralCount() {
    return m_neutralCount / (double) m_scores.size();
  }

  public int getNegCount() {
    return m_negCount;
  }

  public double getAvgNegCount() {
    return m_negCount / (double) m_scores.size();
  }

  public double getSum() {
    return m_sum;
  }

  public double getAvgSum() {
    return m_sum / (double) m_scores.size();
  }

  public int getCount() {
    return m_scores.size();
  }

  public Double getMaxPos() {
    return m_maxPos;
  }

  public Double getMaxNeg() {
    return m_maxNeg;
  }

  public void addScore(double score) {
    this.m_scores.add(score);
    this.m_sum += score;

    // update negative positive neutral counts
    // and max values
    if (score < NEGATIVE_THRESHOLD) { // NEGATIVE
      this.m_negCount++;
      if ((m_maxNeg == null) || (score < m_maxNeg)) { // MAX_NEG_SCORE
        m_maxNeg = score;
      }
    } else if (score > POSITIVE_THRESHOLD) { // POSITIVE
      this.m_posCount++;
      if ((m_maxPos == null) || (score > m_maxPos)) { // MAX_POS_SCORE
        m_maxPos = score;
      }
    } else if ((score >= SentimentResult.NEGATIVE_THRESHOLD)
        && (score <= SentimentResult.POSITIVE_THRESHOLD)) { // NEUTRAL
      this.m_neutralCount++;
    }
  }

  public void add(SentimentResult sentimentResult) {
    this.m_posCount += sentimentResult.getPosCount();
    this.m_neutralCount += sentimentResult.getNeutralCount();
    this.m_negCount += sentimentResult.getNegCount();

    this.m_sum += sentimentResult.getSum();

    Double maxPos = sentimentResult.getMaxPos();
    if ((maxPos != null) && ((m_maxPos == null) || (maxPos > m_maxPos))) { // MAX_POS_SCORE
      m_maxPos = maxPos;
    }

    Double maxNeg = sentimentResult.getMaxNeg();
    if ((maxNeg != null) && ((m_maxNeg == null) || (maxNeg < m_maxNeg))) { // MAX_POS_SCORE
      m_maxNeg = maxPos;
    }

    this.m_scores.addAll(sentimentResult.m_scores);
  }

  @Override
  public String toString() {
    return "SentimentResult [posCount=" + m_posCount + ", neutralCount="
        + m_neutralCount + ", negCount=" + m_negCount + ", sum=" + m_sum
        + ", count=" + m_scores.size() + ", maxPos=" + m_maxPos + ", maxNeg="
        + m_maxNeg + ", scores=" + m_scores.toString() + "]";
  }

}
