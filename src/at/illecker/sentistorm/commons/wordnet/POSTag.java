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
package at.illecker.sentistorm.commons.wordnet;

import edu.mit.jwi.item.POS;

public class POSTag {
  // The WordNet Lemmatizer only knows four part of speech tags
  // Noun, Verb, Adjective, Adverb;
  // Only the Noun and Verb rules are interesting.

  public static POS parseString(String tag) {
    switch (tag.charAt(0)) {
      case 'n':
        return POS.NOUN;
      case 'v':
        return POS.VERB;
      case 'a':
        return POS.ADJECTIVE;
      case 'r':
        return POS.ADVERB;
      default:
        throw new IllegalStateException("Unknown POS tag '" + tag + "'!");
    }
  }

  public static String toString(POS posTag) {
    switch (posTag) {
      case NOUN:
        return "n";
      case VERB:
        return "v";
      case ADJECTIVE:
        return "a";
      case ADVERB:
        return "r";
      default:
        throw new IllegalStateException("Unknown POS tag '" + posTag + "'!");
    }
  }

  public static POS convertPTB(String pennTag) {
    if (pennTag.startsWith("NN")) { // includes proper nouns
      return POS.NOUN;
    }
    if (pennTag.startsWith("VB")) {
      return POS.VERB;
    }
    if (pennTag.startsWith("JJ")) {
      return POS.ADJECTIVE;
    }
    if (pennTag.startsWith("RB")) {
      return POS.ADVERB;
    }
    return null;
  }

  // http://www.ark.cs.cmu.edu/TweetNLP/annot_guidelines.pdf
  public static POS convertArk(String arkTag) {
    if (arkTag.equals("N") || arkTag.equals("O") || arkTag.equals("^")
        || arkTag.equals("S") || arkTag.equals("Z")) {
      return POS.NOUN;
    }
    if (arkTag.equals("V")) {
      return POS.VERB;
    }
    if (arkTag.equals("A")) {
      return POS.ADJECTIVE;
    }
    if (arkTag.equals("R")) {
      return POS.ADVERB;
    }
    return null;
  }

}
