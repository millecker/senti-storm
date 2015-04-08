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
package at.illecker.sentistorm.components;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import at.illecker.sentistorm.commons.Tweet;
import at.illecker.sentistorm.commons.util.HtmlUtils;
import at.illecker.sentistorm.commons.util.RegexUtils;
import at.illecker.sentistorm.commons.util.UnicodeUtils;

public class Tokenizer {

  public static List<List<String>> tokenizeTweets(List<Tweet> tweets) {
    List<List<String>> tokenizedTweets = new ArrayList<List<String>>();
    for (Tweet tweet : tweets) {
      tokenizedTweets.add(tokenize(tweet.getText()));
    }
    return tokenizedTweets;
  }

  public static List<String> tokenize(String str) {
    // Step 1) Trim text
    str = str.trim();

    // Step 2) Replace Unicode symbols \u0000
    if (UnicodeUtils.containsUnicode(str)) {
      str = UnicodeUtils.replaceUnicodeSymbols(str);
    }

    // Step 3) Replace HTML symbols &#[0-9];
    if (HtmlUtils.containsHtml(str)) {
      str = HtmlUtils.replaceHtmlSymbols(str);
    }

    // Step 4) Tokenize
    List<String> tokens = new ArrayList<String>();
    Matcher m = RegexUtils.TOKENIZER_PATTERN.matcher(str);
    while (m.find()) {
      tokens.add(m.group());
    }

    return tokens;
  }

}
