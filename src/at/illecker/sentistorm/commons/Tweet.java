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

public class Tweet implements Serializable {
  private static final long serialVersionUID = 3778428208873277421L;
  private final Long m_id;
  private final String m_text;
  private final Double m_score;

  public Tweet(Long id, String text, Double score) {
    this.m_id = id;
    this.m_text = text;
    this.m_score = score;
  }

  public Tweet(Long id, String text) {
    this(id, text, null);
  }

  public Long getId() {
    return m_id;
  }

  public String getText() {
    return m_text;
  }

  public Double getScore() {
    return m_score;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Tweet other = (Tweet) obj;
    // check if id is matching
    if (this.m_id != other.getId()) {
      return false;
    }
    // check if text is matching
    if (!this.m_text.equals(other.getText())) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "Tweet [id=" + m_id + ", text=" + m_text
        + ((m_score != null) ? ", score=" + m_score : "") + "]";
  }

  public static List<Tweet> getTestTweets() {
    List<Tweet> tweets = new ArrayList<Tweet>();
    tweets.add(new Tweet(1L, "This is a first test tweet :)", 1.0));
    tweets.add(new Tweet(2L, "@test This is a second test tweet ;)", 1.0));
    tweets.add(new Tweet(3L, "This is the last test tweet :(", 0.0));
    return tweets;
  }

}
