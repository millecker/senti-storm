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

public class StringUtils {

  public static boolean isURL(String str) {
    if (str == null) {
      return false;
    }
    return RegexUtils.URL_PATTERN.matcher(str).matches();
  }

  public static boolean isEmail(String str) {
    if (str == null) {
      return false;
    }
    return RegexUtils.EMAIL_ADDRESS_PATTERN.matcher(str).matches();
  }

  public static boolean isPhone(String str) {
    if (str == null) {
      return false;
    }
    return RegexUtils.PHONE_PATTERN.matcher(str).matches();
  }

  public static boolean isEmoticon(String str) {
    if (str == null) {
      return false;
    }
    return RegexUtils.EMOTICON_PATTERN.matcher(str).matches();
  }

  public static boolean isHashTag(String str) {
    if (str == null) {
      return false;
    }
    return RegexUtils.HASH_TAG_PATTERN.matcher(str).matches();
  }

  public static boolean isUser(String str) {
    if (str == null) {
      return false;
    }
    return RegexUtils.USER_NAME_PATTERN.matcher(str).matches();
  }

  public static boolean isRetweet(String str) {
    if (str == null) {
      return false;
    }
    return RegexUtils.RETWEET_PATTERN.matcher(str).matches();
  }

  public static boolean consitsOfPunctuations(String str) {
    if (str == null) {
      return false;
    }
    return RegexUtils.PUNCTUATIONS_PATTERN.matcher(str).matches();
  }

  public static boolean consitsOfUnderscores(String str) {
    if (str == null) {
      return false;
    }
    return RegexUtils.UNDERSCORES_PATTERN.matcher(str).matches();
  }

  public static boolean isNumeric(String str) {
    if (str == null) {
      return false;
    }
    return RegexUtils.NUMBER_PATTERN.matcher(str).matches();
  }

  public static boolean isSpecialNumeric(String str) {
    if (str == null) {
      return false;
    }
    return RegexUtils.SPECIAL_NUMBER_PATTERN.matcher(str).matches();
  }

  public static boolean isSeparatedNumeric(String str) {
    if (str == null) {
      return false;
    }
    return RegexUtils.SEPARATED_NUMBER_PATTERN.matcher(str).matches();
  }

  public static boolean isSlang(String str) {
    if (str == null) {
      return false;
    }
    return RegexUtils.SLANG_PATTERN.matcher(str).matches();
  }

  public static boolean startsWithAlphabeticChar(String str) {
    if (str == null) {
      return false;
    }
    return RegexUtils.STARTS_WITH_ALPHABETIC_CHAR_PATTERN.matcher(str)
        .matches();
  }

}
