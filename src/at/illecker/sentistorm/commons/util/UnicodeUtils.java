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
package at.illecker.sentistorm.commons.util;

public class UnicodeUtils {

  public static boolean containsUnicode(String str) {
    if (str == null) {
      return false;
    }
    return RegexUtils.CONTAINS_UNICODE_SYMBOLS_PATTERN.matcher(str).find();
  }

  public static String replaceUnicodeSymbols(String str) {
    String result = str;

    // Punctuations
    // U+2019 RIGHT SINGLE QUOTATION MARK
    result = result.replaceAll("\u2019|\\\\u2019", "'");
    // U+002C COMMA
    result = result.replaceAll("\u002c|\\\\u002c", ",");

    // Emoticons
    // http://www.fileformat.info/info/unicode/block/emoticons/images.htm
    // http://www.iemoji.com/view/emoji/885/people/grinning-face
    // U+1F600 :grinning:
    result = result.replaceAll("\uD83D\uDE00", ":D");
    // U+1F601 :grin:
    result = result.replaceAll("\uD83D\uDE01", ":D");
    // U+1F602 :joy:
    result = result.replaceAll("\uD83D\uDE02", ":'-)");
    // U+1F603 :smile:
    result = result.replaceAll("\uD83D\uDE03", ":)");
    // U+1F604 :smiley:
    result = result.replaceAll("\uD83D\uDE04", ":)");
    // U+1F605 :sweat_smile:
    result = result.replaceAll("\uD83D\uDE05", ":)");
    // U+1F606 :laughing:
    result = result.replaceAll("\uD83D\uDE06", ":-D");
    // U+1F607 :innocent:
    result = result.replaceAll("\uD83D\uDE07", "O:-)");
    // U+1F608 :smiling_imp:
    result = result.replaceAll("\uD83D\uDE08", ">:-)");
    // U+1F609 :wink:
    result = result.replaceAll("\uD83D\uDE09", ";)");
    // U+1F60A :blush:
    result = result.replaceAll("\uD83D\uDE0A", ":)");
    // U+1F60B :yum:
    result = result.replaceAll("\uD83D\uDE0B", ":p");
    // U+1F60C :relieved:
    result = result.replaceAll("\uD83D\uDE0C", ":)");
    // U+1F60D :heart_eyes:
    result = result.replaceAll("\uD83D\uDE0D", "3>");
    // U+1F60E :sunglasses:
    result = result.replaceAll("\uD83D\uDE0E", "B-)");
    // U+1F60F :smirk:
    result = result.replaceAll("\uD83D\uDE0F", ";-)");
    // U+1F610 :neutral_face:
    result = result.replaceAll("\uD83D\uDE10", ":|");
    // U+1F611 :expressionless:
    result = result.replaceAll("\uD83D\uDE11", ":|");
    // U+1F612 :unamused:
    result = result.replaceAll("\uD83D\uDE12", ":(");
    // U+1F613 :sweat:
    result = result.replaceAll("\uD83D\uDE13", "^_^");
    // U+1F614 :pensive:
    result = result.replaceAll("\uD83D\uDE14", ":(");
    // U+1F615 :confused:
    result = result.replaceAll("\uD83D\uDE15", ">_<");
    // U+1F616 :confounded: // TODO
    result = result.replaceAll("\uD83D\uDE16", ":|");
    // U+1F617 :kissing:
    result = result.replaceAll("\uD83D\uDE17", ":*");
    // U+1F618 :kissing_heart:
    result = result.replaceAll("\uD83D\uDE18", ":*");
    // U+1F619 :kissing_smiling_eyes:
    result = result.replaceAll("\uD83D\uDE19", ":*");
    // U+1F61A :kissing_closed_eyes:
    result = result.replaceAll("\uD83D\uDE1A", ":*");
    // U+1F61B :stuck_out_tongue:
    result = result.replaceAll("\uD83D\uDE1B", ":p");
    // U+1F61C :stuck_out_tongue_winking_eye:
    result = result.replaceAll("\uD83D\uDE1C", ";p");
    // U+1F61D :stuck_out_tongue_closed_eyes:
    result = result.replaceAll("\uD83D\uDE1D", ":p");
    // U+1F61E :disappointed:
    result = result.replaceAll("\uD83D\uDE1E", ":(");
    // U+1F61F :worried:
    result = result.replaceAll("\uD83D\uDE1F", ":-S");
    // U+1F620 :angry:
    result = result.replaceAll("\uD83D\uDE20", ">:(");
    // U+1F621 :rage:
    result = result.replaceAll("\uD83D\uDE21", ":-[");
    // U+1F622 :cry:
    result = result.replaceAll("\uD83D\uDE22", ":'(");
    // U+1F623 :persevere:
    result = result.replaceAll("\uD83D\uDE23", ":(");
    // U+1F624 :triumph: // TODO
    result = result.replaceAll("\uD83D\uDE24", ":|");
    // U+1F625 :disappointed_relieved:
    result = result.replaceAll("\uD83D\uDE25", ":|");
    // U+1F626 :frowning:
    result = result.replaceAll("\uD83D\uDE26", ":(");
    // U+1F627 :anguished:
    result = result.replaceAll("\uD83D\uDE27", ":(");
    // U+1F628 :fearful:
    result = result.replaceAll("\uD83D\uDE28", ":(");
    // U+1F629 :weary:
    result = result.replaceAll("\uD83D\uDE29", "|-)");
    // U+1F62A :sleepy:
    result = result.replaceAll("\uD83D\uDE2A", "|-)");
    // U+1F62B :tired_face:
    result = result.replaceAll("\uD83D\uDE2B", "(:|");
    // U+1F62C :grimacing: // TODO
    result = result.replaceAll("\uD83D\uDE2C", ":(");
    // U+1F62D :sob:
    result = result.replaceAll("\uD83D\uDE2D", ":'(");
    // U+1F62E :face_open_mouth:
    result = result.replaceAll("\uD83D\uDE2E", ":-o");
    // U+1F62F :hushed:
    result = result.replaceAll("\uD83D\uDE2F", ":-x");
    // U+1F630 :cold_sweat:
    result = result.replaceAll("\uD83D\uDE30", ":(");
    // U+1F631 :scream:
    result = result.replaceAll("\uD83D\uDE31", ":-@");
    // U+1F631 :scream:
    result = result.replaceAll("\uD83D\uDE31", ":-@");
    // U+1F632 :astonished:
    result = result.replaceAll("\uD83D\uDE32", ":-o");
    // U+1F633 :flushed:
    result = result.replaceAll("\uD83D\uDE33", "-^_^-");
    // U+1F634 :sleeping:
    result = result.replaceAll("\uD83D\uDE34", "|-)");
    // U+1F635 :dizzy_face:
    result = result.replaceAll("\uD83D\uDE35", "%-)");
    // U+1F636 :no_mouth: // TODO
    result = result.replaceAll("\uD83D\uDE36", ":|");
    // U+1F641 SLIGHTLY FROWNING FACE
    result = result.replaceAll("\uD83D\uDE41", ":-(");
    // U+1F642 SLIGHTLY SMILING FACE
    result = result.replaceAll("\uD83D\uDE42", ":-)");

    return result;
  }

}
